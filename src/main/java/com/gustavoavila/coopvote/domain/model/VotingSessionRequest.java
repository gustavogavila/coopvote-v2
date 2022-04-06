package com.gustavoavila.coopvote.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public class VotingSessionRequest {

    private Integer sessionTimeInMinutes;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public VotingSessionRequest(Integer sessionTimeInMinutes) {
        this.sessionTimeInMinutes = sessionTimeInMinutes;
    }

    public Integer getSessionTimeInMinutes() {
        return sessionTimeInMinutes;
    }
}
