package com.snkuni.sankuni.models;

import com.snkuni.sankuni.models.enums.StudentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "alumnos")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_alumno", updatable = false, nullable = false)
    private UUID idAlumno;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrera_id", nullable = false)
    private Carrera carrera;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StudentStatus estado = StudentStatus.ACTIVO;

    @Column(name = "fecha_ingreso")
    @Builder.Default
    private LocalDate fechaIngreso = LocalDate.now();
}