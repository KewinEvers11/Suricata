package org.oyabun.suricata.web.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record SolicitudMonitoreoDto(
        String id,
        @NotNull(message = "El nombre del producto no puede ser nulo")
        @Size(min = 1, max = 255, message = "El nombre del producto tiene que tener mínimo (1) carácter y máximo (255) caracteres")
        String nombreProducto,
        @Size(max=255, message = "El URL del producto solo puede tener máximo 255 caracteres")
        String urlProducto,
        @Size(max=255, message = "El nombre del usuario no puede ser mayor a 255 caracteres")
        String nombreDeUsuario,
        String estado
) {
}
