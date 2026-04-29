package com.snkuni.sankuni.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "asistencia", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"seccion_id", "alumno_id", "fecha"})
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_asistencia", updatable = false, nullable = false)
    private UUID idAsistencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seccion_id", nullable = false)
    private Seccion seccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alumno_id", nullable = false)
    private Alumno alumno;

    @Column(nullable = false)
    @Builder.Default
    private LocalDate fecha = LocalDate.now();

    @Column(nullable = false)
    @Builder.Default
    private Boolean presente = false;
}