package org.oyabun.suricata.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.oyabun.suricata.services.SolicitudMonitoreoService;
import org.oyabun.suricata.web.model.SolicitudMonitoreoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SolicitudMonitoreoController.class)
class SolicitudMonitoreControllerTest {


    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private SolicitudMonitoreoService solicitudMonitoreoService;


    static final String NOMBRE_PRODUCTO = "RTX 5070";
    static final String URL_PRODUCTO = "http://productos.com/203";
    static final String ID_SOLICITUD = UUID.randomUUID().toString();
    public static final String NOMBRE_DE_USUARIO = "Kazuma";

    static final String ENDPOINT = "/solicitud-monitoreo";

    @Nested
    @DisplayName("Tests crear solicitud productos")
    class testCrearSolicitudDeProductos {

        @DisplayName("Deberia crear la solicitud con el codigo correcto cuando el request es correcto.")
        @Test
        void testCrearSolicitudDeProductos() throws Exception {
            // arrange
            when(solicitudMonitoreoService.crearSolicitudMonitoreo(any(SolicitudMonitoreoDto.class)))
                    .thenReturn(SolicitudMonitoreoDto.builder()
                            .id(ID_SOLICITUD)
                            .nombreDeUsuario(NOMBRE_DE_USUARIO)
                            .nombreProducto(NOMBRE_PRODUCTO)
                            .urlProducto(URL_PRODUCTO)
                            .build());

            SolicitudMonitoreoDto nuevaSolicitud = SolicitudMonitoreoDto.builder()
                    .nombreProducto(NOMBRE_PRODUCTO)
                    .urlProducto(URL_PRODUCTO)
                    .nombreDeUsuario(NOMBRE_DE_USUARIO)
                    .build();

            // act
            mockMvc.perform(post(ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(nuevaSolicitud))
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(header().exists("Location"))
                    .andExpect(header().string("Location", "http://localhost/solicitud-monitoreo/" + ID_SOLICITUD))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.id").value(ID_SOLICITUD))
                    .andExpect(jsonPath("$.nombreProducto").value(NOMBRE_PRODUCTO))
                    .andExpect(jsonPath("$.urlProducto").value(URL_PRODUCTO))
                    .andExpect(jsonPath("$.nombreDeUsuario").value(NOMBRE_DE_USUARIO));

        }
    }



}