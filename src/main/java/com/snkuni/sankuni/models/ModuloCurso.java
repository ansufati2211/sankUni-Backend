package com.snkuni.sankuni.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "modulo_curso")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ModuloCurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modulo", updatable = false, nullable = false)
    private Long idModulo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false)
    @Builder.Default
    private Integer orden = 0;
}
