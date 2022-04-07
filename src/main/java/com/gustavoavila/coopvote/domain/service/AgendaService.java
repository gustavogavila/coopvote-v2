package com.gustavoavila.coopvote.domain.service;

import com.gustavoavila.coopvote.domain.exceptions.AgendaNotFoundException;
import com.gustavoavila.coopvote.domain.exceptions.DuplicatedVoteException;
import com.gustavoavila.coopvote.domain.exceptions.VotingSessionClosedException;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
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

    @Transactional
    public void openVotingSession(Long agendaId, VotingSessionRequest votingSessionRequest) throws AgendaNotFoundException {
        Agenda agenda = findAgendaById(agendaId);
        VotingSession votingSession = new VotingSession(votingSessionRequest.getSessionTimeInMinutes());
        votingSessionRepository.save(votingSession);

        agenda.setVotingSession(votingSession);
        repository.save(agenda);
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

    @Transactional
    public void vote(Long agendaId, VoteRequest voteRequest) throws AgendaNotFoundException, DuplicatedVoteException,
            VotingSessionClosedException {

        Agenda agenda = findAgendaById(agendaId);
        verifyDuplicatedVote(voteRequest, agenda);

        VotingSession votingSession = agenda.getVotingSession();
        Vote vote = voteMapper.transform(voteRequest);
        voteRepository.save(vote);
        votingSession.addVote(vote);
        votingSessionRepository.save(votingSession);
    }

    private void verifyDuplicatedVote(VoteRequest voteRequest, Agenda agenda) throws DuplicatedVoteException {
        Optional<List<Vote>> possibleVote = voteRepository.findByAssociateCPFAndAgenda(voteRequest.getAssociateCPF(), agenda.getId());
        if (possibleVote.isPresent() && !possibleVote.get().isEmpty()) {
            throw new DuplicatedVoteException("The associate has already voted on this agenda");
        }
    }


}
