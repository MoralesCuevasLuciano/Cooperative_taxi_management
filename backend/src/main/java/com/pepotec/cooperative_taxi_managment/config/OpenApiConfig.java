package com.pepotec.cooperative_taxi_managment.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI/Swagger para la documentación de la API
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API de Gestión de Cooperativa de Taxis")
                .version("1.0.0")
                .description("API REST para la gestión integral de una cooperativa de taxis. " +
                    "Incluye gestión de miembros, conductores, suscriptores y más.")
                .contact(new Contact()
                    .name("Luciano David Morales Cuevas (Pepo)")
                    .url("https://www.linkedin.com/in/luciano-morales-cuevas-413342251/"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")))
            .servers(List.of(
                new Server()
                    .url("http://localhost:8080")
                    .description("Servidor de desarrollo"),
                new Server()
                    .url("https://api.cooperative-taxi.com")
                    .description("Servidor de producción")
            ));
    }
}

