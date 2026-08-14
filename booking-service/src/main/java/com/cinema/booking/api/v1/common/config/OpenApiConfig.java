package com.cinema.booking.api.v1.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "Booking Service API",
        version = "v1",
        description = "Booking Service"
))
public class OpenApiConfig {
}
