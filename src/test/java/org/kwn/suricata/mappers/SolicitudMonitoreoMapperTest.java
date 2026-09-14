package org.kwn.suricata.mappers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.kwn.suricata.models.EstadoSolicitud;
import org.kwn.suricata.models.SolicitudMonitoreo;
import org.kwn.suricata.web.model.SolicitudMonitoreoDto;

import java.util.UUID;


class SolicitudMonitoreoMapperTest {

    SolicitudMonitoreoMapper solicitudMonitoreoMapper = Mappers.getMapper(SolicitudMonitoreoMapper.class);

    static final String NOMBRE_PRODUCTO = "Producto para monitorear";
    static final String URL_PRODUCTO = "www.productos.com";
    static final String NOMBRE_USUARIO = "user1";
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
                EstadoSolicitud.EN_REVISION.toString());

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
                .contains(
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
                        SolicitudMonitoreoDto::estado
                )
                .contains(
                        TEST_UUID.toString(),
                        NOMBRE_PRODUCTO,
                        URL_PRODUCTO,
                        NOMBRE_USUARIO,
                        EstadoSolicitud.EN_REVISION.toString()
                );
    }

}