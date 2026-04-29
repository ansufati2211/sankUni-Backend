package com.snkuni.sankuni.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "cursos")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_curso", updatable = false, nullable = false)
    private UUID idCurso;

    // Relación Muchos Cursos pertenecen a Una Carrera
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrera_id", nullable = false)
    private Carrera carrera;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false)
    @Builder.Default
    private Integer creditos = 3;

    @Column(name = "temario_url", columnDefinition = "TEXT")
    private String temarioUrl;

    @Column(name = "descripcion_informativa", columnDefinition = "TEXT")
    private String descripcionInformativa;
}