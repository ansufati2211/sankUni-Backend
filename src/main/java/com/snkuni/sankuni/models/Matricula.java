package com.snkuni.sankuni.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "matriculas", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"alumno_id", "seccion_id"})
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_matricula", updatable = false, nullable = false)
    private UUID idMatricula;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alumno_id", nullable = false)
    private Alumno alumno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seccion_id", nullable = false)
    private Seccion seccion;

    @Column(name = "nota_final", precision = 4, scale = 2)
    private BigDecimal notaFinal;

    @CreationTimestamp
    @Column(name = "fecha_matricula", updatable = false)
    private LocalDateTime fechaMatricula;
}