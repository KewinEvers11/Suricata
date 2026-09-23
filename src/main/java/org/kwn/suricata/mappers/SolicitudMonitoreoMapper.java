package org.kwn.suricata.mappers;

import org.kwn.suricata.web.model.solicitudes.monitores.SolicitudMonitoreoPageItemDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.kwn.suricata.models.SolicitudMonitoreo;

import org.kwn.suricata.web.model.solicitudes.monitores.SolicitudMonitoreoActualizacionDto;
import org.kwn.suricata.web.model.solicitudes.monitores.SolicitudMonitoreoDto;
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        imports = {MapperUtils.class}
)
public interface SolicitudMonitoreoMapper {

    @Mapping(source="nombreProducto", target="nombre")
    @Mapping(source="nombreDeUsuario", target="nombreUsuario")
    @Mapping(source="estado", target="estadoSolicitud")
    @Mapping(target="revisor", ignore = true)
    @Mapping(expression="java(MapperUtils.stringToUuid(solicitudMonitoreoDto.id()))", target="id")
    SolicitudMonitoreo toEntity(SolicitudMonitoreoDto solicitudMonitoreoDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source="nombreProducto", target="nombre")
    void actualizarEntidad(SolicitudMonitoreoActualizacionDto actualizacionDto,
                           @MappingTarget SolicitudMonitoreo solicitudMonitoreo);

    @Mapping(source="nombre", target="nombreProducto")
    @Mapping(source="nombreUsuario", target="nombreDeUsuario")
    @Mapping(source="estadoSolicitud", target="estado")
    @Mapping(expression="java(MapperUtils.uuidToString(solicitudMonitoreo.getId()))", target="id")
    SolicitudMonitoreoDto toDto(SolicitudMonitoreo solicitudMonitoreo);

    @Mapping(source="nombre", target="nombreProducto")
    @Mapping(source="estadoSolicitud", target="estado")
    SolicitudMonitoreoPageItemDto toPageItem(SolicitudMonitoreo solicitudMonitoreo);

    @AfterMapping
    default void limpiarRevisorEnBlanco(@MappingTarget SolicitudMonitoreo solicitudMonitoreo) {
        if (solicitudMonitoreo.getRevisor() != null && solicitudMonitoreo.getRevisor().isBlank()) {
            solicitudMonitoreo.setRevisor(null);
        }
    }

}
