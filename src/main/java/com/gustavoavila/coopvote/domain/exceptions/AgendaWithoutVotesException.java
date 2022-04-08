package com.gustavoavila.coopvote.domain.exceptions;

public class AgendaWithoutVotesException extends RuntimeException {

    public AgendaWithoutVotesException(String message) {
        super(message);
    }

    public AgendaWithoutVotesException() {
        this("There are no votes for this agenda");
    }
}
