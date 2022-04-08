package com.gustavoavila.coopvote.domain.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String description;

    @OneToOne
    private VotingSession votingSession;

    public Agenda() {
    }

    public Agenda(String description) {
        this.description = description;
    }

    public void setVotingSession(VotingSession votingSession) {
        this.votingSession = votingSession;
    }

    public VotingSession getVotingSession() {
        return votingSession;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
