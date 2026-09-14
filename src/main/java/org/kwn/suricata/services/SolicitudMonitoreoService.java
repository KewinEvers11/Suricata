package org.kwn.suricata.services;

import org.kwn.suricata.web.model.SolicitudMonitoreoDto;

import java.util.UUID;

public interface SolicitudMonitoreoService {

    SolicitudMonitoreoDto crearSolicitudMonitoreo(SolicitudMonitoreoDto solicitudMonitoreo);

    SolicitudMonitoreoDto obtenerSolicitudMonitoreo(UUID id);
}
