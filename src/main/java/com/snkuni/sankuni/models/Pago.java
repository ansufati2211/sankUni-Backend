package com.snkuni.sankuni.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPago;

  @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuota_id", nullable = false)
    private CuotaAlumno cuota;

    @Column(nullable = false)
    private BigDecimal montoPagado;

    @Builder.Default
    private String metodoPago = "TARJETA";

    @CreationTimestamp
    private LocalDateTime fechaPago;
}