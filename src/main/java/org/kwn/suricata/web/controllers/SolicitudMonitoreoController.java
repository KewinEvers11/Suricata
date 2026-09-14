package org.kwn.suricata.web.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kwn.suricata.services.SolicitudMonitoreoService;
import org.kwn.suricata.web.model.SolicitudMonitoreoDto;
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
public class SolicitudMonitoreoController {

    private final SolicitudMonitoreoService solicitudMonitoreoService;

    @PostMapping
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
    public ResponseEntity<SolicitudMonitoreoDto> obtenerSolicitudMonitoreo(@PathVariable UUID id) {
        return ResponseEntity.ok(solicitudMonitoreoService.obtenerSolicitudMonitoreo(id));
    }

}
