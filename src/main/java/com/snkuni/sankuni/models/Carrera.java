package com.snkuni.sankuni.models;

import com.snkuni.sankuni.models.enums.TipoPrograma;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "carreras")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Carrera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCarrera;

    @Enumerated(EnumType.STRING)
    private TipoPrograma tipo;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    private String descripcion;
    private String perfilAcademico;
    private String mercadoLaboral;
    private String beneficios;
    private String requisitos;

    @Builder.Default
    private Boolean estado = true;
}