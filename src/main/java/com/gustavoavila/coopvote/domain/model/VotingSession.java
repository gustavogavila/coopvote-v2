package com.gustavoavila.coopvote.domain.model;

import com.gustavoavila.coopvote.domain.exceptions.VotingSessionClosedException;

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
    private List<Vote> votes = new ArrayList<>();

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

    public void close() {
        this.status = StatusVotingSession.CLOSED;
    }

    public void setDurationInMinutes(Integer durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public OffsetDateTime getSessionClosingTime() {
        return sessionClosingTime;
    }

    public Long getId() {
        return id;
    }

    public void addVote(Vote vote) throws VotingSessionClosedException {
        if (this.status.equals(StatusVotingSession.CLOSED)) {
            throw new VotingSessionClosedException();
        }
        this.votes.add(vote);
    }
}
