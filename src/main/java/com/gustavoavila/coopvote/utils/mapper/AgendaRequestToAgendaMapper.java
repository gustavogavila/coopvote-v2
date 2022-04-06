package com.gustavoavila.coopvote.utils.mapper;

import com.gustavoavila.coopvote.domain.model.Agenda;
import com.gustavoavila.coopvote.domain.model.AgendaRequest;
import org.springframework.stereotype.Component;

@Component
public class AgendaRequestToAgendaMapper implements CoopVoteMapper<AgendaRequest, Agenda> {

    @Override
    public Agenda transform(AgendaRequest source) {
        return new Agenda(source.getDescription());
    }
}
