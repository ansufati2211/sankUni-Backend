package com.snkuni.sankuni.models;

import com.snkuni.sankuni.models.enums.EstadoCuota;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cuotas_alumno")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CuotaAlumno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCuota;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alumno_id", nullable = false)
    private Alumno alumno;

    @Column(length = 20)
    private String cicloAcademico;

    @Column(nullable = false, length = 50)
    private String mesCorrespondiente;

    @Column(nullable = false)
    private BigDecimal montoTotal;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EstadoCuota estado = EstadoCuota.PENDIENTE;

    @Column(nullable = false)
    private LocalDate fechaVencimiento;
}