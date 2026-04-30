package com.snkuni.sankuni.models;

import com.snkuni.sankuni.models.enums.TipoAlerta;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "alertas_academicas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AlertaAcademica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAlerta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAlerta tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seccion_id", nullable = false)
    private Seccion seccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docente_id", nullable = false)
    private Docente docente;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    @Builder.Default
    private Boolean resuelta = false;

    @CreationTimestamp
    private LocalDateTime fechaCreacion;
}