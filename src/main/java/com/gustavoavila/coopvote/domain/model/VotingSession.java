package com.gustavoavila.coopvote.domain.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany
    @JoinColumn(name = "voting_session_id")
    private final List<Vote> votes = new ArrayList<>();

    @Deprecated
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
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public OffsetDateTime getSessionClosingTime() {
        return sessionClosingTime;
    }

    public OffsetDateTime getSessionOpeningTime() {
        return sessionOpeningTime;
    }

    public StatusVotingSession getStatus() {
        return status;
    }

    public void close() {
        this.status = StatusVotingSession.CLOSED;
    }
}
