package org.kwn.suricata.web.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwn.suricata.configuration.TestContainerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestContainerConfig.class)
@AutoConfigureRestTestClient
@Sql(value = "/db/test/SolicitudMonitoreoTestsData.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(value = "/db/test/SolicitudMonitoreoTestsDataCleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
public class SolicitudMonitoreoControllerIT {

    static final String ENDPOINT = "/solicitud-monitoreo";

    @Autowired
    RestTestClient restClient;

    @Test
    @DisplayName("Test Obtener solicitudes de monitoreo con paginación por defecto, cuando no se especifica filtro ni paginación")
    void testObtenerSolicitud_conPaginacionPorDefecto() {
        restClient
                .get()
                .uri(ENDPOINT)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.page").exists()
                .jsonPath("$.page.totalElements").isEqualTo(2)
                .jsonPath("$.page.number").isEqualTo(0)
                .jsonPath("$.page.totalPages").isEqualTo(1)
                .jsonPath("$.page.size").isEqualTo(20);
    }

    @Test
    @DisplayName("Test Obtener Solicitudes de Monitoreo Debe dar paginación especificada en la petición: Sort: created_at and size 1")
    void testObtenerSolicitud_DadaPaginacion() {
        Map<String, String> map = Map.of("created_at", "created_at,desc",
                "size", "1");
        restClient
                .get()
                .uri(ENDPOINT + "?sort={created_at}&size={size}", map)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.page").exists()
                .jsonPath("$.page.totalElements").isEqualTo(2)
                .jsonPath("$.page.number").isEqualTo(0)
                .jsonPath("$.page.totalPages").isEqualTo(2)
                .jsonPath("$.page.size").isEqualTo(1)
                .jsonPath("$.content[0]").exists()
                .jsonPath("$.content[0].nombreProducto").isEqualTo("Carro Mazda 2");
    }

}
