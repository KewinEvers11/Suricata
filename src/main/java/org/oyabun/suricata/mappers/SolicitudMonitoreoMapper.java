package org.oyabun.suricata.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.oyabun.suricata.models.SolicitudMonitoreo;
import org.oyabun.suricata.web.model.SolicitudMonitoreoDto;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        imports = {MapperUtils.class}
)
public interface SolicitudMonitoreoMapper {

    @Mapping(source="nombreProducto", target="nombre")
    @Mapping(source="nombreDeUsuario", target="nombreUsuario")
    @Mapping(expression="java(MapperUtils.stringToUuid(solicitudMonitoreoDto.id()))", target="id")
    SolicitudMonitoreo toEntity(SolicitudMonitoreoDto solicitudMonitoreoDto);

    @Mapping(source="nombre", target="nombreProducto")
    @Mapping(source="nombreUsuario", target="nombreDeUsuario")
    @Mapping(expression="java(MapperUtils.uuidToString(solicitudMonitoreo.getId()))", target="id")
    SolicitudMonitoreoDto toDto(SolicitudMonitoreo solicitudMonitoreo);

}
