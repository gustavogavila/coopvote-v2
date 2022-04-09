package com.gustavoavila.coopvote.domain.model;

import com.gustavoavila.coopvote.domain.exceptions.DuplicatedVoteException;
import com.gustavoavila.coopvote.domain.exceptions.VotingSessionClosedException;
import com.gustavoavila.coopvote.domain.exceptions.VotingSessionNotFoundException;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import static java.util.Objects.isNull;

@Entity
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String description;

    @OneToOne
    private VotingSession votingSession;

    @Deprecated
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

    public void setId(long id) {
        this.id = id;
    }

    public void addVote(Vote vote) throws VotingSessionClosedException {
        if (thereIsNotVotingSession()) {
            throw new VotingSessionNotFoundException();
        }
        if (isSessionClosed()) {
            throw new VotingSessionClosedException();
        }
        if (isDuplicatedVote(vote)) {
            throw new DuplicatedVoteException();
        }
        this.votingSession.getVotes().add(vote);
    }

    private boolean isDuplicatedVote(Vote vote) {
        return this.votingSession.getVotes()
                .stream().anyMatch(v -> v.getAssociateCPF().equals(vote.getAssociateCPF()));
    }

    private boolean isSessionClosed() {
        return this.votingSession.getStatus().equals(StatusVotingSession.CLOSED);
    }

    private boolean thereIsNotVotingSession() {
        return isNull(this.votingSession);
    }

}
