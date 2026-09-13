package org.oyabun.suricata.services;

import lombok.RequiredArgsConstructor;
import org.oyabun.suricata.mappers.SolicitudMonitoreoMapper;
import org.oyabun.suricata.models.EstadoSolicitud;
import org.oyabun.suricata.models.SolicitudMonitoreo;
import org.oyabun.suricata.repositories.SolicitudMonitoreoRepository;
import org.oyabun.suricata.web.model.SolicitudMonitoreoDto;
import org.springframework.stereotype.Service;

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
}
