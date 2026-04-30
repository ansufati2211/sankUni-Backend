package com.snkuni.sankuni.models;

import com.snkuni.sankuni.models.enums.EstadoPostulante;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "postulantes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Postulante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPostulante;

    @Column(unique = true, nullable = false, length = 15)
    private String dni;

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(nullable = false, length = 100)
    private String correo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrera_id")
    private Carrera carrera;

    @Column(length = 50)
    private String sede;

    @Column(length = 20)
    private String turno;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EstadoPostulante estado = EstadoPostulante.EN_REVISION;

    @CreationTimestamp
    private LocalDateTime fechaPostulacion;
}