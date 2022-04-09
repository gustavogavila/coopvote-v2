package com.gustavoavila.coopvote.domain.model;


import com.gustavoavila.coopvote.domain.exceptions.DuplicatedVoteException;
import com.gustavoavila.coopvote.domain.exceptions.VotingSessionClosedException;
import com.gustavoavila.coopvote.domain.exceptions.VotingSessionNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AgendaTest {

    Agenda agenda;

    @BeforeEach
    void setup() {
        agenda = new Agenda("Agenda 01");
        agenda.setId(1L);
    }

    @Test
    void shouldAllowAddingVoteToAnAgendaWithVotingSession() {
        var votingSession = new VotingSession(2);
        agenda.setVotingSession(votingSession);
        var vote = new Vote("38823834023", VoteValue.YES);
        agenda.addVote(vote);
        assertEquals(1, agenda.getVotingSession().getVotes().size());
    }

    @Test
    void shouldNotAllowAddingVoteToAnAgendaWithoutVotingSession() {
        var vote = new Vote("38823834023", VoteValue.YES);
        var exception = assertThrows(VotingSessionNotFoundException.class,
                () -> agenda.addVote(vote));
        assertEquals("There is no voting session for the informed agenda. First open a voting session",
                exception.getMessage());
    }

    @Test
    void shouldAddAssociateSingleVote() {
        var votingSession = new VotingSession(5);
        agenda.setVotingSession(votingSession);
        var firstVote = new Vote("38823834023", VoteValue.YES);
        agenda.addVote(firstVote);
        var secondVote = new Vote("27646725005", VoteValue.YES);
        agenda.addVote(secondVote);
        assertEquals(2, votingSession.getVotes().size());
    }

    @Test()
    void shouldNotAddAssociateSecondVote() {
        var votingSession = new VotingSession(5);
        agenda.setVotingSession(votingSession);
        var firstVote = new Vote("38823834023", VoteValue.YES);
        agenda.addVote(firstVote);
        var secondVote = new Vote("38823834023", VoteValue.YES);
        var exception = assertThrows(DuplicatedVoteException.class, () -> agenda.addVote(secondVote));
        assertEquals("The associate has already voted on this agenda", exception.getMessage());
    }

    @Test
    void shouldOnlyAddVoteInAnOpenVotingSession() {
        var votingSession = new VotingSession(5);
        agenda.setVotingSession(votingSession);
        var firstVote = new Vote("38823834023", VoteValue.YES);
        agenda.addVote(firstVote);
        var secondVote = new Vote("27646725005", VoteValue.YES);
        agenda.addVote(secondVote);
        assertEquals(2, votingSession.getVotes().size());
    }

    @Test
    void shouldNotAddVoteInAnClosedVotingSession() {
        var votingSession = new VotingSession(5);
        agenda.setVotingSession(votingSession);
        var firstVote = new Vote("38823834023", VoteValue.YES);
        agenda.addVote(firstVote);
        votingSession.close();
        var secondVote = new Vote("27646725005", VoteValue.YES);
        var exception = assertThrows(VotingSessionClosedException.class,
                () -> agenda.addVote(secondVote));
        assertEquals("This voting session is now closed", exception.getMessage());
    }
}
