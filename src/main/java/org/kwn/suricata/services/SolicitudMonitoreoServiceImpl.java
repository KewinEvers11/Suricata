package org.kwn.suricata.services;

import lombok.RequiredArgsConstructor;
import org.kwn.suricata.exceptions.SolicitudMonitoreoNoEncontradaException;
import org.kwn.suricata.mappers.SolicitudMonitoreoMapper;
import org.kwn.suricata.models.EstadoSolicitud;
import org.kwn.suricata.models.SolicitudMonitoreo;
import org.kwn.suricata.repositories.SolicitudMonitoreoRepository;
import org.kwn.suricata.web.model.solicitudes.monitores.SolicitudMonitoreoConsultaDto;
import org.kwn.suricata.web.model.solicitudes.monitores.SolicitudMonitoreoDto;
import org.kwn.suricata.web.model.solicitudes.monitores.SolicitudMonitoreoPageItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SolicitudMonitoreoServiceImpl implements SolicitudMonitoreoService{

    private final SolicitudMonitoreoRepository solicitudMonitoreoRepository;

    private final SolicitudMonitoreoMapper solicitudMonitoreoMapper;

    @Override
    public SolicitudMonitoreoDto crearSolicitudMonitoreo(SolicitudMonitoreoDto solicitudMonitoreoDto) {
        SolicitudMonitoreo solicitudMonitoreoParaGuardar = solicitudMonitoreoMapper.toEntity(solicitudMonitoreoDto);
        solicitudMonitoreoParaGuardar.setEstadoSolicitud(EstadoSolicitud.EN_REVISION);
        SolicitudMonitoreo solicitudMonitoreoCreada =  solicitudMonitoreoRepository.save(solicitudMonitoreoParaGuardar);
        return solicitudMonitoreoMapper.toDto(solicitudMonitoreoCreada);
    }

    @Override
    public SolicitudMonitoreoDto obtenerSolicitudMonitoreo(UUID id) {
        SolicitudMonitoreo solicitudMonitoreo = solicitudMonitoreoRepository.findById(id)
                .orElseThrow(() -> new SolicitudMonitoreoNoEncontradaException(id));
        return solicitudMonitoreoMapper.toDto(solicitudMonitoreo);
    }

    @Override
    public Page<SolicitudMonitoreoPageItemDto> consultarSolicitudesMonitoreo(SolicitudMonitoreoConsultaDto consultaDto,
                                                                             Pageable pageable) {
        Page<SolicitudMonitoreo> solicitudMonitoreoPage = solicitudMonitoreoRepository.obtenerSolicitudesPor(consultaDto.nombre(), pageable);
        List<SolicitudMonitoreoPageItemDto> pageItems = solicitudMonitoreoPage.getContent()
                .stream()
                .map(solicitudMonitoreoMapper::toPageItem)
                .toList();
        return new PageImpl<>(pageItems, pageable, solicitudMonitoreoPage.getTotalPages());
    }
}
