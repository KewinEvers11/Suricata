package org.kwn.suricata.repositories;

import org.kwn.suricata.models.SolicitudMonitoreo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SolicitudMonitoreoRepository extends JpaRepository<SolicitudMonitoreo, UUID> {
}
