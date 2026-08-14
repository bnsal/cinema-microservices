package com.cinema.event.api.v1.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "Event Service API",
        version = "v1",
        description = "Manages Theater"
))
public class OpenApiConfig {
}
