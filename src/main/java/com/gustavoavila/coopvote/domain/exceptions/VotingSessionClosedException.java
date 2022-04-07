package com.gustavoavila.coopvote.domain.exceptions;

public class VotingSessionClosedException extends Exception {
    public VotingSessionClosedException(String message) {
        super(message);
    }
}
