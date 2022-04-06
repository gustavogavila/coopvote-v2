package com.gustavoavila.coopvote.domain.service;

import com.gustavoavila.coopvote.domain.exceptions.AgendaNotFoundException;
import com.gustavoavila.coopvote.domain.model.Agenda;
import com.gustavoavila.coopvote.domain.model.AgendaRequest;
import com.gustavoavila.coopvote.domain.model.VotingSession;
import com.gustavoavila.coopvote.domain.model.VotingSessionRequest;
import com.gustavoavila.coopvote.domain.repository.AgendaRepository;
import com.gustavoavila.coopvote.domain.repository.VotingSessionRepository;
import com.gustavoavila.coopvote.utils.mapper.AgendaRequestToAgendaMapper;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service
public class AgendaService {

    private final AgendaRequestToAgendaMapper mapper;
    private final AgendaRepository repository;
    private final VotingSessionRepository votingSessionRepository;

    public AgendaService(AgendaRequestToAgendaMapper mapper,
                         AgendaRepository repository,
                         VotingSessionRepository votingSessionRepository) {
        this.mapper = mapper;
        this.repository = repository;
        this.votingSessionRepository = votingSessionRepository;
    }

    public void registerNewAgenda(AgendaRequest agendaRequest) {
        Agenda agenda = mapper.transform(agendaRequest);
        repository.save(agenda);
    }

    public void openVotingSession(Long agendaId, VotingSessionRequest votingSessionRequest) throws AgendaNotFoundException {
        Agenda agenda = repository.findById(agendaId)
                .orElseThrow(() -> new AgendaNotFoundException("Agenda not found"));

        VotingSession votingSession = new VotingSession(votingSessionRequest.getSessionTimeInMinutes());
        votingSession.setAgenda(agenda);
        votingSessionRepository.save(votingSession);
        closeVotingSession(votingSession);
    }

    @Async
    public void closeVotingSession(VotingSession votingSession) {
        ScheduledExecutorService localExecutor = Executors.newSingleThreadScheduledExecutor();
        TaskScheduler scheduler = new ConcurrentTaskScheduler(localExecutor);

        OffsetDateTime sessionClosingTime = votingSession.getSessionClosingTime();
        Date closingDate = Date.from(sessionClosingTime.toInstant());

        scheduler.schedule(() -> {
            votingSession.close();
            votingSessionRepository.save(votingSession);
        }, closingDate);
    }
}
