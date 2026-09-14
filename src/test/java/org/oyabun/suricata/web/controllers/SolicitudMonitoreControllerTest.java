package org.oyabun.suricata.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.oyabun.suricata.exceptions.SolicitudMonitoreoNoEncontradaException;
import org.oyabun.suricata.models.EstadoSolicitud;
import org.oyabun.suricata.services.SolicitudMonitoreoService;
import org.oyabun.suricata.web.model.SolicitudMonitoreoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    static final String CADENA_256_CARACTERES = "a".repeat(256);

    static final String TIPO_ERROR_BAD_REQUEST = "Bad Request";

    static final String MENSAJE_NOMBRE_PRODUCTO_NULO = "El nombre del producto no puede ser nulo";

    static final String MENSAJE_NOMBRE_PRODUCTO_TAMANO = "El nombre del producto tiene que tener mínimo (1) carácter y máximo (255) caracteres";

    static final String MENSAJE_URL_PRODUCTO_TAMANO = "El URL del producto solo puede tener máximo 255 caracteres";

    static final String MENSAJE_NOMBRE_USUARIO_TAMANO = "El nombre del usuario no puede ser mayor a 255 caracteres";

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

    @Nested
    @DisplayName("Tests validación de solicitudes de monitoreo")
    class testValidacionSolicitudesDeMonitoreo {

        @DisplayName("Debería retornar BAD_REQUEST cuando el nombre del producto es nulo.")
        @Test
        void testNombreProductoNuloRetornaBadRequestConMensaje() throws Exception {
            // arrange
            SolicitudMonitoreoDto nuevaSolicitud = SolicitudMonitoreoDto.builder()
                    .nombreProducto(null)
                    .urlProducto(URL_PRODUCTO)
                    .nombreDeUsuario(NOMBRE_DE_USUARIO)
                    .build();

            // act / assert
            mockMvc.perform(post(ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(nuevaSolicitud))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].mensaje").value(MENSAJE_NOMBRE_PRODUCTO_NULO))
                    .andExpect(jsonPath("$[0].tipoDeError").value(TIPO_ERROR_BAD_REQUEST));

            verifyNoInteractions(solicitudMonitoreoService);
        }

        @DisplayName("Debería retornar BAD_REQUEST cuando el nombre del producto está vacío.")
        @Test
        void testNombreProductoVacioRetornaBadRequestConMensaje() throws Exception {
            // arrange
            SolicitudMonitoreoDto nuevaSolicitud = SolicitudMonitoreoDto.builder()
                    .nombreProducto("")
                    .urlProducto(URL_PRODUCTO)
                    .nombreDeUsuario(NOMBRE_DE_USUARIO)
                    .build();

            // act / assert
            mockMvc.perform(post(ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(nuevaSolicitud))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].mensaje").value(MENSAJE_NOMBRE_PRODUCTO_TAMANO))
                    .andExpect(jsonPath("$[0].tipoDeError").value(TIPO_ERROR_BAD_REQUEST));

            verifyNoInteractions(solicitudMonitoreoService);
        }

        @DisplayName("Debería retornar BAD_REQUEST cuando el nombre del producto excede 255 caracteres.")
        @Test
        void testNombreProductoDemasiadoLargoRetornaBadRequestConMensaje() throws Exception {
            // arrange
            SolicitudMonitoreoDto nuevaSolicitud = SolicitudMonitoreoDto.builder()
                    .nombreProducto(CADENA_256_CARACTERES)
                    .urlProducto(URL_PRODUCTO)
                    .nombreDeUsuario(NOMBRE_DE_USUARIO)
                    .build();

            // act / assert
            mockMvc.perform(post(ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(nuevaSolicitud))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].mensaje").value(MENSAJE_NOMBRE_PRODUCTO_TAMANO))
                    .andExpect(jsonPath("$[0].tipoDeError").value(TIPO_ERROR_BAD_REQUEST));

            verifyNoInteractions(solicitudMonitoreoService);
        }

        @DisplayName("Debería retornar BAD_REQUEST cuando el URL del producto excede 255 caracteres.")
        @Test
        void testUrlProductoDemasiadoLargoRetornaBadRequestConMensaje() throws Exception {
            // arrange
            SolicitudMonitoreoDto nuevaSolicitud = SolicitudMonitoreoDto.builder()
                    .nombreProducto(NOMBRE_PRODUCTO)
                    .urlProducto(CADENA_256_CARACTERES)
                    .nombreDeUsuario(NOMBRE_DE_USUARIO)
                    .build();

            // act / assert
            mockMvc.perform(post(ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(nuevaSolicitud))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].mensaje").value(MENSAJE_URL_PRODUCTO_TAMANO))
                    .andExpect(jsonPath("$[0].tipoDeError").value(TIPO_ERROR_BAD_REQUEST));

            verifyNoInteractions(solicitudMonitoreoService);
        }

        @DisplayName("Debería retornar BAD_REQUEST cuando el nombre de usuario excede 255 caracteres.")
        @Test
        void testNombreDeUsuarioDemasiadoLargoRetornaBadRequestConMensaje() throws Exception {
            // arrange
            SolicitudMonitoreoDto nuevaSolicitud = SolicitudMonitoreoDto.builder()
                    .nombreProducto(NOMBRE_PRODUCTO)
                    .urlProducto(URL_PRODUCTO)
                    .nombreDeUsuario(CADENA_256_CARACTERES)
                    .build();

            // act / assert
            mockMvc.perform(post(ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(nuevaSolicitud))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].mensaje").value(MENSAJE_NOMBRE_USUARIO_TAMANO))
                    .andExpect(jsonPath("$[0].tipoDeError").value(TIPO_ERROR_BAD_REQUEST));

            verifyNoInteractions(solicitudMonitoreoService);
        }

        @DisplayName("Debería retornar una lista de errores cuando hay múltiples violaciones.")
        @Test
        void testMultiplesErroresRetornaListaDeErrores() throws Exception {
            // arrange
            SolicitudMonitoreoDto nuevaSolicitud = SolicitudMonitoreoDto.builder()
                    .nombreProducto(null)
                    .urlProducto(CADENA_256_CARACTERES)
                    .nombreDeUsuario(NOMBRE_DE_USUARIO)
                    .build();

            // act / assert
            mockMvc.perform(post(ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(nuevaSolicitud))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[*].mensaje", containsInAnyOrder(MENSAJE_NOMBRE_PRODUCTO_NULO, MENSAJE_URL_PRODUCTO_TAMANO)))
                    .andExpect(jsonPath("$[*].tipoDeError", containsInAnyOrder(TIPO_ERROR_BAD_REQUEST, TIPO_ERROR_BAD_REQUEST)));

            verifyNoInteractions(solicitudMonitoreoService);
        }
    }

    @Nested
    @DisplayName("Tests obtener solicitud de monitoreo")
    class testObtenerSolicitudDeMonitoreo {

        @DisplayName("Debería retornar la solicitud con el código correcto cuando existe.")
        @Test
        void testObtenerSolicitudDeMonitoreoExistente() throws Exception {
            // arrange
            UUID idSolicitud = UUID.fromString(ID_SOLICITUD);
            when(solicitudMonitoreoService.obtenerSolicitudMonitoreo(any(UUID.class)))
                    .thenReturn(SolicitudMonitoreoDto.builder()
                            .id(ID_SOLICITUD)
                            .nombreDeUsuario(NOMBRE_DE_USUARIO)
                            .nombreProducto(NOMBRE_PRODUCTO)
                            .urlProducto(URL_PRODUCTO)
                            .estado(EstadoSolicitud.EN_REVISION.toString())
                            .build());

            // act / assert
            mockMvc.perform(get(ENDPOINT + "/{id}", idSolicitud)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(ID_SOLICITUD))
                    .andExpect(jsonPath("$.nombreProducto").value(NOMBRE_PRODUCTO))
                    .andExpect(jsonPath("$.urlProducto").value(URL_PRODUCTO))
                    .andExpect(jsonPath("$.nombreDeUsuario").value(NOMBRE_DE_USUARIO))
                    .andExpect(jsonPath("$.estado").value(EstadoSolicitud.EN_REVISION.toString()));
        }

        @DisplayName("Debería retornar NOT_FOUND cuando la solicitud no existe.")
        @Test
        void testObtenerSolicitudDeMonitoreoNoExistenteRetornaNotFound() throws Exception {
            // arrange
            UUID idSolicitud = UUID.fromString(ID_SOLICITUD);
            when(solicitudMonitoreoService.obtenerSolicitudMonitoreo(idSolicitud))
                    .thenThrow(new SolicitudMonitoreoNoEncontradaException(idSolicitud));

            // act / assert
            mockMvc.perform(get(ENDPOINT + "/{id}", idSolicitud)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.mensaje").value("La solicitud de monitoreo con id '" + ID_SOLICITUD + "' no existe"))
                    .andExpect(jsonPath("$.tipoDeError").value("Not Found"));
        }
    }

}