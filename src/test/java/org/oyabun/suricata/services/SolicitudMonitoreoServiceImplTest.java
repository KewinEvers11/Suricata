package org.oyabun.suricata.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.oyabun.suricata.exceptions.SolicitudMonitoreoNoEncontradaException;
import org.oyabun.suricata.mappers.SolicitudMonitoreoMapper;
import org.oyabun.suricata.models.EstadoSolicitud;
import org.oyabun.suricata.models.SolicitudMonitoreo;
import org.oyabun.suricata.repositories.SolicitudMonitoreoRepository;
import org.oyabun.suricata.web.model.SolicitudMonitoreoDto;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SolicitudMonitoreoServiceImplTest {

    public static final String NOMBRE_PRODUCTO = "RTX 5090";
    public static final String URL_PRODUCTO = "http://productos.com/203";
    public static final String NOMBRE_DE_USUARIO = "Kazuma";
    public static final UUID UUID_SOLICITUD = UUID.randomUUID();
    @Mock
    SolicitudMonitoreoRepository solicitudMonitoreoRepository;

    @Spy
    SolicitudMonitoreoMapper solicitudMonitoreoMapper = Mappers.getMapper(SolicitudMonitoreoMapper.class);

    @InjectMocks
    SolicitudMonitoreoServiceImpl solicitudMonitoreoService;

    @DisplayName("Test crearSolicitudMonitoreo deberia guardar la solicitud en estado REVISION correctamente")
    @Test
    void testCrearSolicitudMonitoreo() {
        // arrange
        SolicitudMonitoreoDto solicitudMonitoreoDto = SolicitudMonitoreoDto
                .builder()
                .nombreProducto(NOMBRE_PRODUCTO)
                .build();
        doAnswer((invocation) -> {
            SolicitudMonitoreo solicitud =(SolicitudMonitoreo) invocation.getArguments()[0];
            solicitud.setId(UUID_SOLICITUD);
            return solicitud;
        }).when(solicitudMonitoreoRepository).save(any(SolicitudMonitoreo.class));

        // act

        SolicitudMonitoreoDto response = solicitudMonitoreoService.crearSolicitudMonitoreo(solicitudMonitoreoDto);

        // assert
        Assertions.assertThat(response)
                .isNotNull()
                .extracting(SolicitudMonitoreoDto::id,
                        SolicitudMonitoreoDto::nombreProducto,
                        SolicitudMonitoreoDto::estado)
                .contains(
                        UUID_SOLICITUD.toString(),
                        NOMBRE_PRODUCTO,
                        EstadoSolicitud.EN_REVISION.toString()
                );
    }

    @DisplayName("Test obtenerSolicitudMonitoreo deberia retornar la solicitud cuando existe")
    @Test
    void testObtenerSolicitudMonitoreoExistente() {
        // arrange
        SolicitudMonitoreo solicitudMonitoreo = SolicitudMonitoreo.builder()
                .id(UUID_SOLICITUD)
                .nombre(NOMBRE_PRODUCTO)
                .urlProducto(URL_PRODUCTO)
                .nombreUsuario(NOMBRE_DE_USUARIO)
                .estadoSolicitud(EstadoSolicitud.EN_REVISION)
                .build();
        when(solicitudMonitoreoRepository.findById(UUID_SOLICITUD)).thenReturn(Optional.of(solicitudMonitoreo));

        // act
        SolicitudMonitoreoDto response = solicitudMonitoreoService.obtenerSolicitudMonitoreo(UUID_SOLICITUD);

        // assert
        Assertions.assertThat(response)
                .isNotNull()
                .extracting(SolicitudMonitoreoDto::id,
                        SolicitudMonitoreoDto::nombreProducto,
                        SolicitudMonitoreoDto::urlProducto,
                        SolicitudMonitoreoDto::nombreDeUsuario,
                        SolicitudMonitoreoDto::estado)
                .contains(
                        UUID_SOLICITUD.toString(),
                        NOMBRE_PRODUCTO,
                        URL_PRODUCTO,
                        NOMBRE_DE_USUARIO,
                        EstadoSolicitud.EN_REVISION.toString()
                );
    }

    @DisplayName("Test obtenerSolicitudMonitoreo deberia lanzar excepcion cuando no existe")
    @Test
    void testObtenerSolicitudMonitoreoNoExistente() {
        // arrange
        when(solicitudMonitoreoRepository.findById(UUID_SOLICITUD)).thenReturn(Optional.empty());

        // act / assert
        Assertions.assertThatThrownBy(() -> solicitudMonitoreoService.obtenerSolicitudMonitoreo(UUID_SOLICITUD))
                .isInstanceOf(SolicitudMonitoreoNoEncontradaException.class)
                .hasMessageContaining(UUID_SOLICITUD.toString());
    }

}