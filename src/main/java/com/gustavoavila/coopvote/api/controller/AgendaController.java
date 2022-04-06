package com.gustavoavila.coopvote.api.controller;

import com.gustavoavila.coopvote.domain.model.AgendaRequest;
import com.gustavoavila.coopvote.domain.service.AgendaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
