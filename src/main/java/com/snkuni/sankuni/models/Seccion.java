package com.snkuni.sankuni.models;

import com.snkuni.sankuni.models.enums.ModalidadSeccion;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;

@Entity
@Table(name = "secciones")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Seccion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSeccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docente_id", nullable = false)
    private Docente docente;

    @Column(nullable = false, length = 20)
    private String cicloAcademico;

    // --- AQUÍ ESTÁ LA SOLUCIÓN AL ERROR ---
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ModalidadSeccion modalidad = ModalidadSeccion.PRESENCIAL;
    // --------------------------------------

    @Column(nullable = false)
    private Integer diaSemana;

    @Column(nullable = false)
    private LocalTime horaInicio;

    @Column(nullable = false)
    private LocalTime horaFin;
}