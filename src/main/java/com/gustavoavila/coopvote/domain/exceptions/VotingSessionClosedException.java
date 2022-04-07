package com.gustavoavila.coopvote.domain.exceptions;

public class VotingSessionClosedException extends RuntimeException {
    public VotingSessionClosedException(String message) {
        super(message);
    }

    public VotingSessionClosedException() {
        this("This voting session is now closed");
    }
}
