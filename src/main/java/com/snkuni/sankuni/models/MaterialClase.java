package com.snkuni.sankuni.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "materiales_clase")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MaterialClase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMaterial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seccion_id", nullable = false)
    private Seccion seccion;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String archivoUrl;

    @CreationTimestamp
    private LocalDateTime fechaSubida;
}