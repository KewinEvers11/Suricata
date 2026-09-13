package org.oyabun.suricata.web.controller;

import lombok.RequiredArgsConstructor;
import org.oyabun.suricata.services.SolicitudMonitoreoService;
import org.oyabun.suricata.web.model.SolicitudMonitoreoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/solicitud-monitoreo")
public class SolicitudMonitoreController {

    private final SolicitudMonitoreoService solicitudMonitoreoService;

    @PostMapping
    public ResponseEntity<SolicitudMonitoreoDto> crearSolicitudProducto(@RequestBody SolicitudMonitoreoDto solicitudMonitoreoDto,
                                                                        UriComponentsBuilder ucb) {
        SolicitudMonitoreoDto solicitudMonitoreoDtoCreada = solicitudMonitoreoService.crearSolicitudMonitoreo(solicitudMonitoreoDto);
        URI location = ucb
                .path("products/{id}")
                .buildAndExpand(solicitudMonitoreoDtoCreada.id())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(solicitudMonitoreoDto);
    }

}
