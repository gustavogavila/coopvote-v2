package com.gustavoavila.coopvote.domain.exceptions;

public class DuplicatedVoteException extends Exception{
    public DuplicatedVoteException(String message) {
        super(message);
    }
}
