package org.kwn.suricata.repositories;

import org.kwn.suricata.models.SolicitudMonitoreo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface SolicitudMonitoreoRepository extends JpaRepository<SolicitudMonitoreo, UUID> {


    @Query(value ="SELECT solicitud " +
            "FROM SolicitudMonitoreo solicitud " +
            "WHERE (:nombre IS NULL OR solicitud.nombre LIKE %:nombre%)",
            countQuery = "SELECT solicitud FROM SolicitudMonitoreo solicitud " +
                    "WHERE :nombre IS NULL OR solicitud.nombre LIKE %:nombre%"
    )
    Page<SolicitudMonitoreo> obtenerSolicitudesPor(
            @Param("nombre") String nombre,
            Pageable pageable);

}
