package org.kwn.suricata.services;

import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kwn.suricata.exceptions.SolicitudMonitoreoNoEncontradaException;
import org.kwn.suricata.mappers.SolicitudMonitoreoMapper;
import org.kwn.suricata.models.EstadoSolicitud;
import org.kwn.suricata.models.SolicitudMonitoreo;
import org.kwn.suricata.repositories.SolicitudMonitoreoRepository;
import org.kwn.suricata.web.model.solicitudes.monitores.SolicitudMonitoreoActualizacionDto;
import org.kwn.suricata.web.model.solicitudes.monitores.SolicitudMonitoreoConsultaDto;
import org.kwn.suricata.web.model.solicitudes.monitores.SolicitudMonitoreoDto;
import org.kwn.suricata.web.model.solicitudes.monitores.SolicitudMonitoreoPageItemDto;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitudMonitoreoServiceImplTest {

    public static final String NOMBRE_PRODUCTO = "RTX 5090";
    public static final String NOMBRE_PRODUCTO_ACTUALIZADO = "RTX 5080";
    public static final String URL_PRODUCTO = "http://productos.com/203";
    public static final String NOMBRE_DE_USUARIO = "Kazuma";
    public static final String REVISOR = "Aqua";
    public static final String REVISOR_ACTUALIZADO = "Megumin";
    public static final UUID UUID_SOLICITUD = UUID.randomUUID();
    @Mock
    SolicitudMonitoreoRepository solicitudMonitoreoRepository;

    @Spy
    SolicitudMonitoreoMapper solicitudMonitoreoMapper = Mappers.getMapper(SolicitudMonitoreoMapper.class);

    @InjectMocks
    SolicitudMonitoreoServiceImpl solicitudMonitoreoService;

    @Captor
    ArgumentCaptor<String> nombreParamCaptor;

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
                .containsExactly(
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
                .containsExactly(
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

    @DisplayName("Test consultarSolicitudesMonitoreo")
    @Nested
    class TestsConsultarSolicitudesMonitoreo {

        @DisplayName("Test parametro nombre debe ser usado para realizar la consulta")
        @Test
        void testConsultarSolicitudMonitoreoDeberiaConsultarPorNombre() {
            // arrange
            SolicitudMonitoreoConsultaDto consultaDto = SolicitudMonitoreoConsultaDto.builder()
                    .nombre("Consulta")
                    .build();
            Pageable pageable = PageRequest.of(0, 5);
            when(solicitudMonitoreoRepository.obtenerSolicitudesPor(anyString(), any(Pageable.class)))
                    .thenReturn(
                            new PageImpl<>(List.of(SolicitudMonitoreo.builder()
                                            .id(UUID_SOLICITUD)
                                            .nombre(NOMBRE_PRODUCTO)
                                            .estadoSolicitud(EstadoSolicitud.EN_REVISION)
                                    .build()), pageable, 1)
                    );


            // act
            Page<SolicitudMonitoreoPageItemDto> pageResponse = solicitudMonitoreoService.consultarSolicitudesMonitoreo(consultaDto, pageable);

            // assert
            verify(solicitudMonitoreoRepository, times(1))
                    .obtenerSolicitudesPor(nombreParamCaptor.capture(), any(Pageable.class));
            Assertions.assertThat(nombreParamCaptor.getValue())
                    .isNotNull()
                    .isEqualTo("Consulta");
            Assertions
                    .assertThat(pageResponse)
                    .isNotNull()
                    .extracting(SolicitudMonitoreoPageItemDto::getId,
                            SolicitudMonitoreoPageItemDto::getNombreProducto,
                            SolicitudMonitoreoPageItemDto::getEstado)
                    .containsExactly(Tuple.tuple(UUID_SOLICITUD.toString(), NOMBRE_PRODUCTO, EstadoSolicitud.EN_REVISION.toString()));
        }
    }

    @DisplayName("Test actualizarSolicitudMonitoreo")
    @Nested
    class TestsActualizarSolicitudMonitoreo {

        private SolicitudMonitoreo solicitudExistente() {
            return SolicitudMonitoreo.builder()
                    .id(UUID_SOLICITUD)
                    .nombre(NOMBRE_PRODUCTO)
                    .urlProducto(URL_PRODUCTO)
                    .nombreUsuario(NOMBRE_DE_USUARIO)
                    .revisor(REVISOR)
                    .estadoSolicitud(EstadoSolicitud.EN_REVISION)
                    .build();
        }

        @DisplayName("Debería actualizar solo el nombre cuando el revisor no se envía")
        @Test
        void testActualizarSolicitudMonitoreoSoloNombre() {
            // arrange
            SolicitudMonitoreo solicitud = solicitudExistente();
            when(solicitudMonitoreoRepository.findById(UUID_SOLICITUD)).thenReturn(Optional.of(solicitud));
            when(solicitudMonitoreoRepository.save(any(SolicitudMonitoreo.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));
            SolicitudMonitoreoActualizacionDto actualizacionDto = SolicitudMonitoreoActualizacionDto.builder()
                    .nombreProducto(NOMBRE_PRODUCTO_ACTUALIZADO)
                    .build();

            // act
            SolicitudMonitoreoDto response = solicitudMonitoreoService.actualizarSolicitudMonitoreo(UUID_SOLICITUD, actualizacionDto);

            // assert
            Assertions.assertThat(response)
                    .isNotNull()
                    .extracting(SolicitudMonitoreoDto::id,
                            SolicitudMonitoreoDto::nombreProducto,
                            SolicitudMonitoreoDto::revisor,
                            SolicitudMonitoreoDto::urlProducto,
                            SolicitudMonitoreoDto::nombreDeUsuario,
                            SolicitudMonitoreoDto::estado)
                    .containsExactly(
                            UUID_SOLICITUD.toString(),
                            NOMBRE_PRODUCTO_ACTUALIZADO,
                            REVISOR,
                            URL_PRODUCTO,
                            NOMBRE_DE_USUARIO,
                            EstadoSolicitud.EN_REVISION.toString()
                    );
        }

        @DisplayName("Debería actualizar solo el revisor cuando el nombre no se envía")
        @Test
        void testActualizarSolicitudMonitoreoSoloRevisor() {
            // arrange
            SolicitudMonitoreo solicitud = solicitudExistente();
            when(solicitudMonitoreoRepository.findById(UUID_SOLICITUD)).thenReturn(Optional.of(solicitud));
            when(solicitudMonitoreoRepository.save(any(SolicitudMonitoreo.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));
            SolicitudMonitoreoActualizacionDto actualizacionDto = SolicitudMonitoreoActualizacionDto.builder()
                    .revisor(REVISOR_ACTUALIZADO)
                    .build();

            // act
            SolicitudMonitoreoDto response = solicitudMonitoreoService.actualizarSolicitudMonitoreo(UUID_SOLICITUD, actualizacionDto);

            // assert
            Assertions.assertThat(response)
                    .isNotNull()
                    .extracting(SolicitudMonitoreoDto::nombreProducto,
                            SolicitudMonitoreoDto::revisor,
                            SolicitudMonitoreoDto::estado)
                    .containsExactly(
                            NOMBRE_PRODUCTO,
                            REVISOR_ACTUALIZADO,
                            EstadoSolicitud.EN_REVISION.toString()
                    );
        }

        @DisplayName("Debería actualizar nombre y revisor cuando ambos se envían")
        @Test
        void testActualizarSolicitudMonitoreoNombreYRevisor() {
            // arrange
            SolicitudMonitoreo solicitud = solicitudExistente();
            when(solicitudMonitoreoRepository.findById(UUID_SOLICITUD)).thenReturn(Optional.of(solicitud));
            when(solicitudMonitoreoRepository.save(any(SolicitudMonitoreo.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));
            SolicitudMonitoreoActualizacionDto actualizacionDto = SolicitudMonitoreoActualizacionDto.builder()
                    .nombreProducto(NOMBRE_PRODUCTO_ACTUALIZADO)
                    .revisor(REVISOR_ACTUALIZADO)
                    .build();

            // act
            SolicitudMonitoreoDto response = solicitudMonitoreoService.actualizarSolicitudMonitoreo(UUID_SOLICITUD, actualizacionDto);

            // assert
            Assertions.assertThat(response)
                    .isNotNull()
                    .extracting(SolicitudMonitoreoDto::nombreProducto,
                            SolicitudMonitoreoDto::revisor)
                    .containsExactly(NOMBRE_PRODUCTO_ACTUALIZADO, REVISOR_ACTUALIZADO);
        }

        @DisplayName("Debería limpiar el revisor cuando se envía una cadena vacía")
        @Test
        void testActualizarSolicitudMonitoreoLimpiarRevisor() {
            // arrange
            SolicitudMonitoreo solicitud = solicitudExistente();
            when(solicitudMonitoreoRepository.findById(UUID_SOLICITUD)).thenReturn(Optional.of(solicitud));
            when(solicitudMonitoreoRepository.save(any(SolicitudMonitoreo.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));
            SolicitudMonitoreoActualizacionDto actualizacionDto = SolicitudMonitoreoActualizacionDto.builder()
                    .revisor("")
                    .build();

            // act
            SolicitudMonitoreoDto response = solicitudMonitoreoService.actualizarSolicitudMonitoreo(UUID_SOLICITUD, actualizacionDto);

            // assert
            Assertions.assertThat(response).isNotNull();
            Assertions.assertThat(response.revisor()).isNull();
            Assertions.assertThat(response.nombreProducto()).isEqualTo(NOMBRE_PRODUCTO);
        }

        @DisplayName("Debería lanzar excepción cuando la solicitud no existe")
        @Test
        void testActualizarSolicitudMonitoreoNoExistente() {
            // arrange
            when(solicitudMonitoreoRepository.findById(UUID_SOLICITUD)).thenReturn(Optional.empty());
            SolicitudMonitoreoActualizacionDto actualizacionDto = SolicitudMonitoreoActualizacionDto.builder()
                    .nombreProducto(NOMBRE_PRODUCTO_ACTUALIZADO)
                    .build();

            // act / assert
            Assertions.assertThatThrownBy(() -> solicitudMonitoreoService.actualizarSolicitudMonitoreo(UUID_SOLICITUD, actualizacionDto))
                    .isInstanceOf(SolicitudMonitoreoNoEncontradaException.class)
                    .hasMessageContaining(UUID_SOLICITUD.toString());
            verify(solicitudMonitoreoRepository, never()).save(any(SolicitudMonitoreo.class));
        }
    }

}