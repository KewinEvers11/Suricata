package org.kwn.suricata.web.model.solicitudes.monitores;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record SolicitudMonitoreoDto(
        @Schema(description = "Identificador único de la solicitud de monitoreo", example = "018e5a0f-0b2f-7b1e-8000-000000000000", accessMode = Schema.AccessMode.READ_ONLY)
        String id,
        @NotNull(message = "El nombre del producto no puede ser nulo")
        @Size(min = 1, max = 255, message = "El nombre del producto tiene que tener mínimo (1) carácter y máximo (255) caracteres")
        @Schema(description = "Nombre del producto a monitorear", example = "Nintendo Switch 2")
        String nombreProducto,
        @Size(max=255, message = "El URL del producto solo puede tener máximo 255 caracteres")
        @Schema(description = "URL del producto en el sitio a monitorear", example = "https://www.mercadolibre.com.ar/nintendo-switch-2")
        String urlProducto,
        @Size(max=255, message = "El nombre del usuario no puede ser mayor a 255 caracteres")
        @Schema(description = "Nombre del usuario que solicita el monitoreo", example = "jperez")
        String nombreDeUsuario,
        @Schema(description = "Estado actual de la solicitud de monitoreo",
                allowableValues = {"EN_REVISION", "ACEPTADA", "RECHAZADA"},
                accessMode = Schema.AccessMode.READ_ONLY)
        String estado,
        @Schema(description = "Nombre del revisor que evalúa la solicitud", example = "jperez",
                accessMode = Schema.AccessMode.READ_ONLY)
        String revisor
) {
}