package com.snkuni.sankuni.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notas_evaluacion", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"evaluacion_id", "alumno_id"})
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class NotaEvaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_nota", updatable = false, nullable = false)
    private UUID idNota;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluacion_id", nullable = false)
    private Evaluacion evaluacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alumno_id", nullable = false)
    private Alumno alumno;

    @Column(precision = 4, scale = 2)
    private BigDecimal nota;

    @CreationTimestamp
    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro;
}