package org.oyabun.suricata.exceptions;

import java.util.UUID;

public class SolicitudMonitoreoNoEncontradaException extends RuntimeException {

    private static final String MENSAJE = "La solicitud de monitoreo con id '%s' no existe";

    public SolicitudMonitoreoNoEncontradaException(UUID id) {
        super(String.format(MENSAJE, id));
    }
}
