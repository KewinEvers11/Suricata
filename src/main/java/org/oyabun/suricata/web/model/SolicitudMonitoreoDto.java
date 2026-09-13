package org.oyabun.suricata.web.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SolicitudMonitoreoDto(
        String id,
        @NotNull
        @Size(min = 1, max = 255)
        String nombreProducto,
        @Size(max=255)
        String urlProducto,
        @Size(max=255)
        String nombreDeUsuario,
        String entityUri
) {
}
