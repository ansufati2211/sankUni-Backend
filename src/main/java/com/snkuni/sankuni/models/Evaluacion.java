package com.snkuni.sankuni.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "evaluaciones")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Evaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_evaluacion", updatable = false, nullable = false)
    private UUID idEvaluacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seccion_id", nullable = false)
    private Seccion seccion;

    @Column(name = "nombre_examen", nullable = false, length = 100)
    private String nombreExamen;

    @Column(name = "peso_porcentaje")
    private Integer pesoPorcentaje;

    @Column(name = "fecha_examen", nullable = false)
    private LocalDate fechaExamen;

    @Column(name = "fecha_publicacion_notas")
    private LocalDateTime fechaPublicacionNotas;
}