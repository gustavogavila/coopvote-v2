package com.gustavoavila.coopvote.domain.model;

import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class VoteRequest {
    @NotBlank
    @CPF
    private String associateCPF;

    @NotNull
    private VoteValue voteValue;

    public VoteRequest(String associateCPF, VoteValue voteValue) {
        this.associateCPF = associateCPF;
        this.voteValue = voteValue;
    }

    public String getAssociateCPF() {
        return associateCPF;
    }

    public VoteValue getVoteValue() {
        return voteValue;
    }
}
