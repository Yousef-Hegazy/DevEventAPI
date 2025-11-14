package com.example.DevEvent.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("DevEvent API")
                    .description("REST API for managing developer events")
                    .version("1.0.0")
                    .contact(
                        Contact()
                            .name("DevEvent Team")
                            .email("support@devevent.com")
                    )
                    .license(
                        License()
                            .name("Apache 2.0")
                            .url("https://www.apache.org/licenses/LICENSE-2.0.html")
                    )
            )
            .servers(
                listOf(
                    Server()
                        .url("http://localhost:8080/api")
                        .description("Local Development Server")
                )
            )
    }
}

