package de.koerperschmiede.backend.models.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
public abstract class BaseEntity implements Serializable {
    @Id
    @Column(updatable = false, nullable = false)
    protected UUID id = UUID.randomUUID();

    @CreationTimestamp
    protected Instant created;

    @UpdateTimestamp
    protected Instant modified;
}
