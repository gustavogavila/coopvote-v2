package com.gustavoavila.coopvote.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.NotBlank;

public class AgendaRequest {

    @NotBlank
    private String description;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public AgendaRequest(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
