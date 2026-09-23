package org.kwn.suricata.mappers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwn.suricata.web.model.solicitudes.monitores.SolicitudMonitoreoPageItemDto;
import org.mapstruct.factory.Mappers;
import org.kwn.suricata.models.EstadoSolicitud;
import org.kwn.suricata.models.SolicitudMonitoreo;
import org.kwn.suricata.web.model.solicitudes.monitores.SolicitudMonitoreoActualizacionDto;
import org.kwn.suricata.web.model.solicitudes.monitores.SolicitudMonitoreoDto;

import java.util.UUID;


class SolicitudMonitoreoMapperTest {

    SolicitudMonitoreoMapper solicitudMonitoreoMapper = Mappers.getMapper(SolicitudMonitoreoMapper.class);

    static final String NOMBRE_PRODUCTO = "Producto para monitorear";
    static final String NOMBRE_PRODUCTO_ACTUALIZADO = "Producto actualizado";
    static final String URL_PRODUCTO = "www.productos.com";
    static final String NOMBRE_USUARIO = "user1";
    static final String REVISOR = "revisor1";
    static final String REVISOR_ACTUALIZADO = "revisor2";
    static final UUID TEST_UUID = UUID.randomUUID();

    @Test
    @DisplayName("Should map all dto values to entity correctly.")
    void testToEntity() {
        // arrange
        SolicitudMonitoreoDto solicitudMonitoreoDto = new SolicitudMonitoreoDto(
                TEST_UUID.toString(),
                NOMBRE_PRODUCTO,
                URL_PRODUCTO,
                NOMBRE_USUARIO,
                EstadoSolicitud.EN_REVISION.toString(),
                REVISOR);

        // act
        SolicitudMonitoreo resultado = solicitudMonitoreoMapper.toEntity(solicitudMonitoreoDto);

        // assert
        Assertions
                .assertThat(resultado)
                .extracting(
                        SolicitudMonitoreo::getId,
                        SolicitudMonitoreo::getNombre,
                        SolicitudMonitoreo::getUrlProducto,
                        SolicitudMonitoreo::getNombreUsuario,
                        SolicitudMonitoreo::getEstadoSolicitud,
                        SolicitudMonitoreo::getRevisor
                )
                .containsExactly(
                        TEST_UUID,
                        NOMBRE_PRODUCTO,
                        URL_PRODUCTO,
                        NOMBRE_USUARIO,
                        EstadoSolicitud.EN_REVISION,
                        null
                );
    }

    @Test
    @DisplayName("Should map available values from entity to DTO correctly")
    void testToDto() {
        // arrange
        SolicitudMonitoreo entidad = SolicitudMonitoreo.builder()
                .id(TEST_UUID)
                .nombre(NOMBRE_PRODUCTO)
                .urlProducto(URL_PRODUCTO)
                .nombreUsuario(NOMBRE_USUARIO)
                .revisor(REVISOR)
                .estadoSolicitud(EstadoSolicitud.EN_REVISION)
                .build();

        // act
        SolicitudMonitoreoDto resultado = solicitudMonitoreoMapper.toDto(entidad);

        // assert
        Assertions
                .assertThat(resultado)
                .extracting(
                        SolicitudMonitoreoDto::id,
                        SolicitudMonitoreoDto::nombreProducto,
                        SolicitudMonitoreoDto::urlProducto,
                        SolicitudMonitoreoDto::nombreDeUsuario,
                        SolicitudMonitoreoDto::estado,
                        SolicitudMonitoreoDto::revisor
                )
                .containsExactly(
                        TEST_UUID.toString(),
                        NOMBRE_PRODUCTO,
                        URL_PRODUCTO,
                        NOMBRE_USUARIO,
                        EstadoSolicitud.EN_REVISION.toString(),
                        REVISOR
                );
    }

    @Test
    @DisplayName("Should update only the non-null fields and preserve the rest")
    void testActualizarEntidadSoloNombre() {
        // arrange
        SolicitudMonitoreo entidad = SolicitudMonitoreo.builder()
                .id(TEST_UUID)
                .nombre(NOMBRE_PRODUCTO)
                .urlProducto(URL_PRODUCTO)
                .nombreUsuario(NOMBRE_USUARIO)
                .revisor(REVISOR)
                .estadoSolicitud(EstadoSolicitud.EN_REVISION)
                .build();
        SolicitudMonitoreoActualizacionDto actualizacionDto = SolicitudMonitoreoActualizacionDto.builder()
                .nombreProducto(NOMBRE_PRODUCTO_ACTUALIZADO)
                .build();

        // act
        solicitudMonitoreoMapper.actualizarEntidad(actualizacionDto, entidad);

        // assert
        Assertions
                .assertThat(entidad)
                .extracting(
                        SolicitudMonitoreo::getNombre,
                        SolicitudMonitoreo::getRevisor,
                        SolicitudMonitoreo::getUrlProducto,
                        SolicitudMonitoreo::getNombreUsuario,
                        SolicitudMonitoreo::getEstadoSolicitud
                )
                .containsExactly(
                        NOMBRE_PRODUCTO_ACTUALIZADO,
                        REVISOR,
                        URL_PRODUCTO,
                        NOMBRE_USUARIO,
                        EstadoSolicitud.EN_REVISION
                );
    }

    @Test
    @DisplayName("Should update both nombre and revisor when both are present")
    void testActualizarEntidadNombreYRevisor() {
        // arrange
        SolicitudMonitoreo entidad = SolicitudMonitoreo.builder()
                .id(TEST_UUID)
                .nombre(NOMBRE_PRODUCTO)
                .urlProducto(URL_PRODUCTO)
                .nombreUsuario(NOMBRE_USUARIO)
                .revisor(REVISOR)
                .estadoSolicitud(EstadoSolicitud.EN_REVISION)
                .build();
        SolicitudMonitoreoActualizacionDto actualizacionDto = SolicitudMonitoreoActualizacionDto.builder()
                .nombreProducto(NOMBRE_PRODUCTO_ACTUALIZADO)
                .revisor(REVISOR_ACTUALIZADO)
                .build();

        // act
        solicitudMonitoreoMapper.actualizarEntidad(actualizacionDto, entidad);

        // assert
        Assertions
                .assertThat(entidad)
                .extracting(
                        SolicitudMonitoreo::getNombre,
                        SolicitudMonitoreo::getRevisor
                )
                .containsExactly(NOMBRE_PRODUCTO_ACTUALIZADO, REVISOR_ACTUALIZADO);
    }

    @Test
    @DisplayName("Should clear the revisor when the update dto sends a blank value")
    void testActualizarEntidadRevisorEnBlancoLoLimpia() {
        // arrange
        SolicitudMonitoreo entidad = SolicitudMonitoreo.builder()
                .id(TEST_UUID)
                .nombre(NOMBRE_PRODUCTO)
                .urlProducto(URL_PRODUCTO)
                .nombreUsuario(NOMBRE_USUARIO)
                .revisor(REVISOR)
                .estadoSolicitud(EstadoSolicitud.EN_REVISION)
                .build();
        SolicitudMonitoreoActualizacionDto actualizacionDto = SolicitudMonitoreoActualizacionDto.builder()
                .revisor("")
                .build();

        // act
        solicitudMonitoreoMapper.actualizarEntidad(actualizacionDto, entidad);

        // assert
        Assertions.assertThat(entidad.getRevisor()).isNull();
        Assertions.assertThat(entidad.getNombre()).isEqualTo(NOMBRE_PRODUCTO);
    }

    @Test
    @DisplayName("Should keep every field untouched when the update dto is empty")
    void testActualizarEntidadSinCambios() {
        // arrange
        SolicitudMonitoreo entidad = SolicitudMonitoreo.builder()
                .id(TEST_UUID)
                .nombre(NOMBRE_PRODUCTO)
                .urlProducto(URL_PRODUCTO)
                .nombreUsuario(NOMBRE_USUARIO)
                .revisor(REVISOR)
                .estadoSolicitud(EstadoSolicitud.EN_REVISION)
                .build();
        SolicitudMonitoreoActualizacionDto actualizacionDto = SolicitudMonitoreoActualizacionDto.builder().build();

        // act
        solicitudMonitoreoMapper.actualizarEntidad(actualizacionDto, entidad);

        // assert
        Assertions
                .assertThat(entidad)
                .extracting(
                        SolicitudMonitoreo::getNombre,
                        SolicitudMonitoreo::getRevisor,
                        SolicitudMonitoreo::getEstadoSolicitud
                )
                .containsExactly(NOMBRE_PRODUCTO, REVISOR, EstadoSolicitud.EN_REVISION);
    }

    @Test
    @DisplayName("Should map necessary fields for item")
    void testToPageItem() {
        SolicitudMonitoreo solicitudMonitoreo = SolicitudMonitoreo.builder()
                .id(TEST_UUID)
                .nombre(NOMBRE_PRODUCTO)
                .estadoSolicitud(EstadoSolicitud.RECHAZADA)
                .build();
        SolicitudMonitoreoPageItemDto pageItem = solicitudMonitoreoMapper.toPageItem(solicitudMonitoreo);
        Assertions
                .assertThat(pageItem)
                .extracting(SolicitudMonitoreoPageItemDto::getId,
                        SolicitudMonitoreoPageItemDto::getNombreProducto,
                        SolicitudMonitoreoPageItemDto::getEstado)
                .containsExactly(
                        TEST_UUID.toString(), NOMBRE_PRODUCTO, EstadoSolicitud.RECHAZADA.toString()
                );
    }

}
