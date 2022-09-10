package com.example.modularmonoliths.api;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customOpenAPI(@Value("${application.name}") String applicationName,
                                 @Value("${application.version}") String applicationVersion) {
        return new OpenAPI().info(new Info().title(applicationName).version(applicationVersion));
    }

}
