package com.gustavoavila.coopvote.core.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI coopVoteOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("CoopVote API")
                        .description("API for managing cooperative voting sessions")
                        .version("v0.0.1"))
                .addTagsItem(new Tag().name("Agendas").description("Cooperative agendas"));
    }
}
