package com.project.gestionconocimiento.project_backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "AsistenciaReunion", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"reunion_id", "cedula"})
})
public class AsistenciaReunion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reunion_id", nullable = false)
    private Integer reunionId;

    @Column(name = "cedula", nullable = false)
    private Integer cedula;

    @Column(name = "fecha_confirmacion", nullable = false)
    private LocalDateTime fechaConfirmacion;

    @ManyToOne
    @JoinColumn(name = "cedula", insertable = false, updatable = false)
    private Usuario usuario;
}

