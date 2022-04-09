package com.gustavoavila.coopvote.api.controller;

import com.gustavoavila.coopvote.api.controller.openapi.AgendaControllerOpenApi;
import com.gustavoavila.coopvote.domain.model.*;
import com.gustavoavila.coopvote.domain.service.AgendaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("agendas")
public class AgendaController implements AgendaControllerOpenApi {

    private final AgendaService agendaService;

    public AgendaController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    @PostMapping
    public ResponseEntity<AgendaResponse> registerNewAgenda(@Valid @RequestBody AgendaRequest agendaRequest) {
        AgendaResponse agenda = agendaService.registerNewAgenda(agendaRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(agenda.getId())
                .toUri();
        return ResponseEntity.created(location).body(agenda);
    }

    @PostMapping("{agendaId}/open-voting-session")
    public void openVotingSession(@PathVariable Long agendaId,
                                  @RequestBody(required = false) VotingSessionRequest votingSessionRequest) {
        agendaService.openVotingSession(agendaId, votingSessionRequest);
    }

    @PostMapping("{agendaId}/vote")
    public void vote(@PathVariable Long agendaId, @Valid @RequestBody VoteRequest voteRequest) {
        agendaService.vote(agendaId, voteRequest);
    }

    @GetMapping("{agendaId}/voting-result")
    public ResponseEntity<VotingResult> votingResult(@PathVariable Long agendaId) {
        VotingResult votingResult = agendaService.votingResult(agendaId);
        return ResponseEntity.ok(votingResult);
    }
}
