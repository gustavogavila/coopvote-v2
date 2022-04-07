package com.gustavoavila.coopvote.domain.exceptions;

public class DuplicatedVoteException extends RuntimeException{
    public DuplicatedVoteException(String message) {
        super(message);
    }

    public DuplicatedVoteException() {
        this("The associate has already voted on this agenda");
    }
}
