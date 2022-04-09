package com.gustavoavila.coopvote.domain.model;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VotingSessionTest {

    VotingSession votingSession;

    @BeforeEach
    void setup() {
        var agenda = new Agenda("Agenda 01");
        agenda.setId(1L);
        agenda.setVotingSession(votingSession);
    }

    @Test
    void shouldOpenAOneMinuteVotingSessionByDefault() {
        votingSession = new VotingSession(null);
        var sessionOpeningTime = votingSession.getSessionOpeningTime();
        var expectedSessionClosingTime = sessionOpeningTime.plusMinutes(1L);
        assertEquals(expectedSessionClosingTime, votingSession.getSessionClosingTime());
    }

    @Test
    void shouldOpenAVotingSessionWithTimeInformedByTheUser() {
        votingSession = new VotingSession(10);
        var sessionOpeningTime = votingSession.getSessionOpeningTime();
        var expectedSessionClosingTime = sessionOpeningTime.plusMinutes(10L);
        assertEquals(expectedSessionClosingTime, votingSession.getSessionClosingTime());
    }
}
