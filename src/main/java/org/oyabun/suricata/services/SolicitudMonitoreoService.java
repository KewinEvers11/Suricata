package org.oyabun.suricata.services;

import org.oyabun.suricata.web.model.SolicitudMonitoreoDto;

import java.util.UUID;

public interface SolicitudMonitoreoService {

    SolicitudMonitoreoDto crearSolicitudMonitoreo(SolicitudMonitoreoDto solicitudMonitoreo);

    SolicitudMonitoreoDto obtenerSolicitudMonitoreo(UUID id);
}
