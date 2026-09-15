package org.kwn.suricata.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI suricataOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Suricata API")
                .description("API REST para el monitoreo de precios de productos")
                .version("0.0.1-SNAPSHOT"));
    }
}