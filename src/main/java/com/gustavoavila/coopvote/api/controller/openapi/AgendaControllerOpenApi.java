package com.gustavoavila.coopvote.api.controller.openapi;

import com.gustavoavila.coopvote.api.controller.exceptionhandler.Problem;
import com.gustavoavila.coopvote.domain.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Tag(name = "Agendas")
public interface AgendaControllerOpenApi {

    @Operation(summary = "Register new agenda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New agenda created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AgendaResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Entity not found",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class))
                    }
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class))
                    }
            )
    })
    ResponseEntity<AgendaResponse> registerNewAgenda(@Valid @RequestBody AgendaRequest agendaRequest);

    @Operation(summary = "Open voting session for an agenda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Voting session opened"),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Agenda not found",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class))
                    }
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class))
                    }
            )
    })
    void openVotingSession(@PathVariable Long agendaId, @RequestBody VotingSessionRequest votingSessionRequest);

    @Operation(summary = "Vote for an agenda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vote added to agenda"),
            @ApiResponse(responseCode = "400", description = "Voting Session Closed",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Agenda not found",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class))
                    }
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class))
                    }
            )
    })
    void vote(@PathVariable Long agendaId, @Valid @RequestBody VoteRequest voteRequest);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Result of voting on the agenda",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VotingResult.class)) }),
            @ApiResponse(responseCode = "400", description = "Agenda without votes",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Agenda not found",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class))
                    }
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class))
                    }
            )
    })
    @Operation(summary = "Result of voting an agenda")
    ResponseEntity<VotingResult> votingResult(@PathVariable Long agendaId);
}
