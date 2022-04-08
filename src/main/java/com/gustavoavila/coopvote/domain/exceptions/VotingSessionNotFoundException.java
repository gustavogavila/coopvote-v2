package com.gustavoavila.coopvote.domain.exceptions;

public class VotingSessionNotFoundException extends RuntimeException {

    public VotingSessionNotFoundException(String message) {
        super(message);
    }

    public VotingSessionNotFoundException() {
        this("There is no voting session for the informed agenda. First open a voting session");
    }
}
