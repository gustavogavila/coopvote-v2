package com.gustavoavila.coopvote.domain.service;

import com.gustavoavila.coopvote.domain.exceptions.AgendaNotFoundException;
import com.gustavoavila.coopvote.domain.exceptions.DuplicatedVoteException;
import com.gustavoavila.coopvote.domain.model.*;
import com.gustavoavila.coopvote.domain.repository.AgendaRepository;
import com.gustavoavila.coopvote.domain.repository.VoteRepository;
import com.gustavoavila.coopvote.domain.repository.VotingSessionRepository;
import com.gustavoavila.coopvote.utils.mapper.AgendaRequestToAgendaMapper;
import com.gustavoavila.coopvote.utils.mapper.VoteRequestToVoteMapper;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service
public class AgendaService {

    private final AgendaRequestToAgendaMapper mapper;
    private final AgendaRepository repository;
    private final VotingSessionRepository votingSessionRepository;
    private final VoteRequestToVoteMapper voteMapper;
    private final VoteRepository voteRepository;

    public AgendaService(AgendaRequestToAgendaMapper mapper,
                         AgendaRepository repository,
                         VotingSessionRepository votingSessionRepository,
                         VoteRequestToVoteMapper voteMapper,
                         VoteRepository voteRepository) {
        this.mapper = mapper;
        this.repository = repository;
        this.votingSessionRepository = votingSessionRepository;
        this.voteMapper = voteMapper;
        this.voteRepository = voteRepository;
    }

    public void registerNewAgenda(AgendaRequest agendaRequest) {
        Agenda agenda = mapper.transform(agendaRequest);
        repository.save(agenda);
    }

    public void openVotingSession(Long agendaId, VotingSessionRequest votingSessionRequest) throws AgendaNotFoundException {
        Agenda agenda = findAgendaById(agendaId);
        VotingSession votingSession = new VotingSession(votingSessionRequest.getSessionTimeInMinutes());
        votingSession.setAgenda(agenda);
        votingSessionRepository.save(votingSession);
        closeVotingSession(votingSession);
    }

    private Agenda findAgendaById(Long agendaId) throws AgendaNotFoundException {
        return repository.findById(agendaId)
                .orElseThrow(() -> new AgendaNotFoundException("Agenda not found"));
    }

    @Async
    private void closeVotingSession(VotingSession votingSession) {
        ScheduledExecutorService localExecutor = Executors.newSingleThreadScheduledExecutor();
        TaskScheduler scheduler = new ConcurrentTaskScheduler(localExecutor);

        OffsetDateTime sessionClosingTime = votingSession.getSessionClosingTime();
        Date closingDate = Date.from(sessionClosingTime.toInstant());

        scheduler.schedule(() -> {
            votingSession.close();
            votingSessionRepository.save(votingSession);
        }, closingDate);
    }

    public void vote(Long agendaId, VoteRequest voteRequest) throws AgendaNotFoundException, DuplicatedVoteException {
        Agenda agenda = findAgendaById(agendaId);
        Optional<Vote> possibleVote = voteRepository.findByAssociateCPFAndAgenda(voteRequest.getAssociateCPF(), agenda);
        if (possibleVote.isPresent()) {
            throw new DuplicatedVoteException("The associate has already voted on this agenda");
        }
        voteRequest.setAgenda(agenda);
        Vote vote = voteMapper.transform(voteRequest);
        voteRepository.save(vote);
    }
}
