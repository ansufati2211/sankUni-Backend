package com.snkuni.sankuni.models;

import com.snkuni.sankuni.models.enums.TeacherStatus;
import jakarta.persistence.*;
import lombok.*;



@Entity
@Table(name = "docentes")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Docente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_docente", updatable = false, nullable = false)
    private Long idDocente;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TeacherStatus estado = TeacherStatus.ACTIVO;

    @Column(length = 100)
    private String especialidad;
}