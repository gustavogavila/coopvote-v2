package com.gustavoavila.coopvote.domain.service;

import com.gustavoavila.coopvote.domain.model.Agenda;
import com.gustavoavila.coopvote.domain.model.AgendaRequest;
import com.gustavoavila.coopvote.domain.repository.AgendaRepository;
import com.gustavoavila.coopvote.utils.mapper.AgendaRequestToAgendaMapper;
import org.springframework.stereotype.Service;

@Service
public class AgendaService {

    private final AgendaRequestToAgendaMapper mapper;
    private final AgendaRepository repository;

    public AgendaService(AgendaRequestToAgendaMapper mapper, AgendaRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    public void registerNewAgenda(AgendaRequest agendaRequest) {
        Agenda agenda = mapper.transform(agendaRequest);
        repository.save(agenda);
    }
}
