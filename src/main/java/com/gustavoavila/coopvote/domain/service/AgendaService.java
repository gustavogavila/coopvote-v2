package com.gustavoavila.coopvote.domain.service;

import com.gustavoavila.coopvote.domain.exceptions.AgendaNotFoundException;
import com.gustavoavila.coopvote.domain.exceptions.VotingSessionNotFoundException;
import com.gustavoavila.coopvote.domain.model.*;
import com.gustavoavila.coopvote.domain.repository.AgendaRepository;
import com.gustavoavila.coopvote.domain.repository.VoteRepository;
import com.gustavoavila.coopvote.domain.repository.VotingSessionRepository;
import com.gustavoavila.coopvote.infra.connections.RabbitMQConstants;
import com.gustavoavila.coopvote.utils.mapper.AgendaRequestToAgendaMapper;
import com.gustavoavila.coopvote.utils.mapper.AgendaToAgendaResponseMapper;
import com.gustavoavila.coopvote.utils.mapper.VoteRequestToVoteMapper;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class AgendaService {

    private final AgendaRequestToAgendaMapper mapper;
    private final AgendaRepository repository;
    private final VotingSessionRepository votingSessionRepository;
    private final VoteRequestToVoteMapper voteMapper;
    private final VoteRepository voteRepository;
    private final AgendaToAgendaResponseMapper agendaResponseMapper;
    private final RabbitMQService rabbitMQService;

    public AgendaService(AgendaRequestToAgendaMapper mapper,
                         AgendaRepository repository,
                         VotingSessionRepository votingSessionRepository,
                         VoteRequestToVoteMapper voteMapper,
                         VoteRepository voteRepository,
                         AgendaToAgendaResponseMapper agendaResponseMapper,
                         RabbitMQService rabbitMQService) {
        this.mapper = mapper;
        this.repository = repository;
        this.votingSessionRepository = votingSessionRepository;
        this.voteMapper = voteMapper;
        this.voteRepository = voteRepository;
        this.agendaResponseMapper = agendaResponseMapper;
        this.rabbitMQService = rabbitMQService;
    }

    public AgendaResponse registerNewAgenda(AgendaRequest agendaRequest) {
        Agenda agenda = mapper.transform(agendaRequest);
        repository.save(agenda);
        return agendaResponseMapper.transform(agenda);
    }

    @Transactional
    public void openVotingSession(Long agendaId, VotingSessionRequest votingSessionRequest) {
        Agenda agenda = findAgendaById(agendaId);
        int durationInMinutes = getDurationInMinutesIfExist(votingSessionRequest);
        VotingSession votingSession = new VotingSession(durationInMinutes);
        votingSessionRepository.save(votingSession);

        agenda.setVotingSession(votingSession);
        repository.save(agenda);
        closeVotingSession(votingSession, agenda.getId());
    }

    private int getDurationInMinutesIfExist(VotingSessionRequest votingSessionRequest) {
        var durationInMinutes = 1;
        if (sessionTimeHasBeenReported(votingSessionRequest)) {
            durationInMinutes = votingSessionRequest.getSessionTimeInMinutes();
        }
        return durationInMinutes;
    }

    private Agenda findAgendaById(Long agendaId) {
        return repository.findById(agendaId)
                .orElseThrow(() -> new AgendaNotFoundException(agendaId));
    }

    @Async
    private void closeVotingSession(VotingSession votingSession, Long agendaId) {
        ScheduledExecutorService localExecutor = Executors.newSingleThreadScheduledExecutor();
        TaskScheduler scheduler = new ConcurrentTaskScheduler(localExecutor);

        OffsetDateTime sessionClosingTime = votingSession.getSessionClosingTime();
        Date closingDate = Date.from(sessionClosingTime.toInstant());

        scheduler.schedule(() -> {
            votingSession.close();
            votingSessionRepository.save(votingSession);
            sendNotification(agendaId);
        }, closingDate);
    }

    private void sendNotification(Long agendaId) {
        VotingResult votingResult = votingResult(agendaId);
        var message = buildNotificationMessage(votingResult);
        rabbitMQService.sendMessage(RabbitMQConstants.VOTE_QUEUE, message);
    }

    private String buildNotificationMessage(VotingResult votingResult) {
        var agendaDescription = votingResult.getAgendaDescription();
        var finalResult = votingResult.getFinalResult();
        var totalVotesYes = votingResult.getTotalVotesYes();
        var totalVotesNo = votingResult.getTotalVotesNo();
        return String.format("The agenda %s was %s with %s NO votes and %s YES votes",
                agendaDescription,
                finalResult,
                totalVotesNo,
                totalVotesYes);
    }

    @Transactional
    public void vote(Long agendaId, VoteRequest voteRequest) {
        Agenda agenda = findAgendaById(agendaId);
        Vote vote = voteMapper.transform(voteRequest);
        voteRepository.save(vote);
        agenda.addVote(vote);
    }

    public VotingResult votingResult(Long agendaId) {
        Agenda agenda = findAgendaById(agendaId);
        VotingSession votingSession = agenda.getVotingSession();

        if (isNull(votingSession)) {
            throw new VotingSessionNotFoundException();
        }

        List<Vote> votes = agenda.getVotingSession().getVotes();
        Map<VoteValue, Long> collect = votes.stream().map(Vote::getVoteValue)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Long numberNO = nonNull(collect.get(VoteValue.NO)) ? collect.get(VoteValue.NO) : 0L;
        Long numberYES = nonNull(collect.get(VoteValue.YES)) ? collect.get(VoteValue.YES) : 0L;

        FinalResult finalResult = calculateResult(numberYES, numberNO);

        return new VotingResult(agenda.getDescription(), numberYES, numberNO, finalResult);
    }

    private FinalResult calculateResult(Long numberYES, Long numberNO) {
        if (numberYES == 0L && numberNO == 0L) {
            return FinalResult.DRAW;
        }
        if (numberYES > numberNO) {
            return FinalResult.APPROVED;
        }
        if (numberYES < numberNO) {
            return FinalResult.REJECTED;
        }
        return FinalResult.DRAW;
    }

    private boolean sessionTimeHasBeenReported(VotingSessionRequest votingSessionRequest) {
        return nonNull(votingSessionRequest) && nonNull(votingSessionRequest.getSessionTimeInMinutes());
    }
}
