package org.kwn.suricata.services;

import org.kwn.suricata.web.model.solicitudes.monitores.SolicitudMonitoreoConsultaDto;
import org.kwn.suricata.web.model.solicitudes.monitores.SolicitudMonitoreoDto;
import org.kwn.suricata.web.model.solicitudes.monitores.SolicitudMonitoreoPageItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface SolicitudMonitoreoService {

    SolicitudMonitoreoDto crearSolicitudMonitoreo(SolicitudMonitoreoDto solicitudMonitoreo);

    SolicitudMonitoreoDto obtenerSolicitudMonitoreo(UUID id);

    Page<SolicitudMonitoreoPageItemDto> consultarSolicitudesMonitoreo(SolicitudMonitoreoConsultaDto consultaDto,
                                                              Pageable pageable);
}
