package com.gustavoavila.coopvote.domain.exceptions;

public class AgendaNotFoundException extends RuntimeException {

    public AgendaNotFoundException(String message) {
        super(message);
    }

    public AgendaNotFoundException(Long agendaId) {
        this("Agenda not found. Id: " + agendaId);
    }
}
