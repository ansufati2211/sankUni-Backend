package com.snkuni.sankuni.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "entrega_evaluacion", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"evaluacion_id", "alumno_id"})
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class EntregaEvaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entrega", updatable = false, nullable = false)
    private Long idEntrega;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluacion_id", nullable = false)
    private Evaluacion evaluacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alumno_id", nullable = false)
    private Alumno alumno;

    @Column(name = "archivo_url", nullable = false, columnDefinition = "TEXT")
    private String archivoUrl;

    @CreationTimestamp
    @Column(name = "fecha_entrega", updatable = false)
    private LocalDateTime fechaEntrega;
}
