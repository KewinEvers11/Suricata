package org.oyabun.suricata.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.boot.jaxb.mapping.GenerationTiming;
import org.springframework.aot.generate.Generated;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public abstract class EntidadBase {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name="created_at")
    private LocalDateTime created_at;

    @Column(name="updated_at")
    private LocalDateTime updated_at;

}
