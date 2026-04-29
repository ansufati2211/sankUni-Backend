package com.snkuni.sankuni.models;

import jakarta.persistence.*;
import lombok.*;



@Entity
@Table(name = "carreras")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Carrera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrera", updatable = false, nullable = false)
    private Long idCarrera;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false)
    @Builder.Default
    private Boolean estado = true;
}