package org.kwn.suricata.web.model.solicitudes.monitores;

import lombok.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudMonitoreoPageItemDto{

    private String id;
    private String nombreProducto;
    private URI uri;
    private String estado;

    public void setUri(UriComponentsBuilder ucb) {
        this.uri = ucb.buildAndExpand(this.id).toUri();
    }

}
