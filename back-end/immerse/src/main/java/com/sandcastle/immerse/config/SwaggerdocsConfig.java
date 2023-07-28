package com.sandcastle.immerse.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger springdoc-ui 구성 파일
 */
@Configuration
public class SwaggerdocsConfig {
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Immerse API Document")
                .version("v0.0.1")
                .description("Immerse API 명세서입니다.");
        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
