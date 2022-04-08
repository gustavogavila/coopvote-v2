package com.gustavoavila.coopvote.utils.mapper;

import com.gustavoavila.coopvote.domain.model.Agenda;
import com.gustavoavila.coopvote.domain.model.AgendaResponse;
import org.springframework.stereotype.Component;

@Component
public class AgendaToAgendaResponseMapper implements CoopVoteMapper<Agenda, AgendaResponse> {

    @Override
    public AgendaResponse transform(Agenda source) {
        AgendaResponse agendaResponse = new AgendaResponse(source.getId(), source.getDescription());
        return agendaResponse;
    }
}
