package com.gustavoavila.coopvote.domain.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

import static java.util.Objects.nonNull;

@Entity
public class VotingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusVotingSession status;

    @NotNull
    private Integer durationInMinutes;

    private OffsetDateTime sessionOpeningTime;
    private OffsetDateTime sessionClosingTime;

    @ManyToOne
    private Agenda agenda;

    public VotingSession() {
    }

    public VotingSession(Integer durationInMinutes) {
        this.durationInMinutes = 1;
        if (nonNull(durationInMinutes)) {
            this.durationInMinutes = durationInMinutes;
        }
        this.status = StatusVotingSession.OPENED;
        this.sessionOpeningTime = OffsetDateTime.now();
        this.sessionClosingTime = this.sessionOpeningTime.plus(this.durationInMinutes, ChronoUnit.MINUTES);
        System.out.println("Teste");
    }

    public void close() {
        this.status = StatusVotingSession.CLOSED;
    }

    public void setDurationInMinutes(Integer durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }

    public OffsetDateTime getSessionClosingTime() {
        return sessionClosingTime;
    }

    public Long getId() {
        return id;
    }
}
