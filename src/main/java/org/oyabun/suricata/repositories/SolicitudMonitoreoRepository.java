package org.oyabun.suricata.repositories;

import org.oyabun.suricata.models.SolicitudMonitoreo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SolicitudMonitoreoRepository extends JpaRepository<SolicitudMonitoreo, UUID> {
}
