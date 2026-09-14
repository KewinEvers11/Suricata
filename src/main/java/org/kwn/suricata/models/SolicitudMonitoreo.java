package org.kwn.suricata.models;


import com.fasterxml.uuid.Generators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "solicitud_monitoreo")
public class SolicitudMonitoreo extends EntidadBase {

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "url_producto")
    private String urlProducto;

    @Column(name = "nombre_usuario")
    private String nombreUsuario;

    @Column(name = "revisor")
    private String revisor;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "estado")
    private EstadoSolicitud estadoSolicitud;

    @Builder
    public SolicitudMonitoreo(UUID id,
                              LocalDateTime created_at,
                              LocalDateTime updated_at,
                              String nombre,
                              String urlProducto,
                              String nombreUsuario,
                              String revisor,
                              EstadoSolicitud estadoSolicitud) {
        super(id, created_at, updated_at);
        this.nombre = nombre;
        this.urlProducto = urlProducto;
        this.nombreUsuario = nombreUsuario;
        this.revisor = revisor;
        this.estadoSolicitud = estadoSolicitud;
    }

    @PrePersist
    public void prepersist() {
        LocalDateTime creationTimestamp = LocalDateTime.now();
        this.setCreated_at(creationTimestamp);
        this.setUpdated_at(creationTimestamp);
        this.setId(Generators.timeBasedEpochRandomGenerator().generate());
    }

}
