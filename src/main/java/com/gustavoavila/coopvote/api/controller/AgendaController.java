package com.gustavoavila.coopvote.api.controller;

import com.gustavoavila.coopvote.domain.exceptions.AgendaNotFoundException;
import com.gustavoavila.coopvote.domain.model.AgendaRequest;
import com.gustavoavila.coopvote.domain.model.VotingSessionRequest;
import com.gustavoavila.coopvote.domain.service.AgendaService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("agendas")
public class AgendaController {

    private final AgendaService agendaService;

    public AgendaController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    @PostMapping
    public void registerNewAgenda(@Valid @RequestBody AgendaRequest agendaRequest) {
        agendaService.registerNewAgenda(agendaRequest);
    }

    @PostMapping("{agendaId}/open-voting-session")
    public void openVotingSession(@PathVariable Long agendaId, @RequestBody VotingSessionRequest votingSessionRequest) throws AgendaNotFoundException {
        agendaService.openVotingSession(agendaId, votingSessionRequest);
    }
}
