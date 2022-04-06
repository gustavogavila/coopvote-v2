package com.gustavoavila.coopvote.domain.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String associateCPF;

    @NotNull
    @Enumerated(EnumType.STRING)
    private VoteValue voteValue;

    @ManyToOne
    private Agenda agenda;

    public Vote() {
    }

    public Vote(String associateCPF, VoteValue voteValue, Agenda agenda) {
        this.associateCPF = associateCPF;
        this.voteValue = voteValue;
        this.agenda = agenda;
    }
}
