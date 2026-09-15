package org.kwn.suricata.web.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kwn.suricata.services.SolicitudMonitoreoService;
import org.kwn.suricata.web.model.RespuestaError;
import org.kwn.suricata.web.model.SolicitudMonitoreoDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/solicitud-monitoreo")
@Tag(name = "Solicitudes de monitoreo", description = "Gestión de solicitudes de monitoreo de precios de productos")
public class SolicitudMonitoreoController {

    private final SolicitudMonitoreoService solicitudMonitoreoService;

    @PostMapping
    @Operation(summary = "Crear solicitud de monitoreo",
            description = "Registra una nueva solicitud para monitorear el precio de un producto")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Solicitud creada exitosamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SolicitudMonitoreoDto.class))),
            @ApiResponse(responseCode = "400", description = "Los datos de la solicitud son inválidos",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = RespuestaError.class))))
    })
    public ResponseEntity<SolicitudMonitoreoDto> crearSolicitudProducto(@RequestBody @Valid SolicitudMonitoreoDto solicitudMonitoreoDto,
                                                                        UriComponentsBuilder ucb) {
        SolicitudMonitoreoDto solicitudMonitoreoDtoCreada = solicitudMonitoreoService.crearSolicitudMonitoreo(solicitudMonitoreoDto);
        URI location = ucb
                .path("/solicitud-monitoreo/{id}")
                .buildAndExpand(solicitudMonitoreoDtoCreada.id())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(solicitudMonitoreoDtoCreada);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener solicitud de monitoreo",
            description = "Obtiene los detalles de una solicitud de monitoreo por su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitud encontrada",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SolicitudMonitoreoDto.class))),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RespuestaError.class)))
    })
    public ResponseEntity<SolicitudMonitoreoDto> obtenerSolicitudMonitoreo(@PathVariable
                                                                           @Parameter(description = "Identificador único de la solicitud de monitoreo",
                                                                                   example = "018e5a0f-0b2f-7b1e-8000-000000000000")
                                                                           UUID id) {
        return ResponseEntity.ok(solicitudMonitoreoService.obtenerSolicitudMonitoreo(id));
    }

}