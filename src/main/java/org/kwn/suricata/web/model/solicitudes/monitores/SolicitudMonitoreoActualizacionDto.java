package org.kwn.suricata.web.model.solicitudes.monitores;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record SolicitudMonitoreoActualizacionDto(
        @Size(min = 1, max = 255, message = "El nombre del producto tiene que tener mínimo (1) carácter y máximo (255) caracteres")
        @Schema(description = "Nombre del producto a monitorear", example = "Nintendo Switch 2")
        String nombreProducto,
        @Size(max = 255, message = "El nombre del revisor no puede ser mayor a 255 caracteres")
        @Schema(description = "Nombre del revisor que evalúa la solicitud. Enviar una cadena vacía para limpiarlo",
                example = "jperez")
        String revisor
) {
}
