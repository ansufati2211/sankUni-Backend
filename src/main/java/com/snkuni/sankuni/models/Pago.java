package com.snkuni.sankuni.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pagos")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_pago", updatable = false, nullable = false)
    private UUID idPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matricula_id", nullable = false)
    private Matricula matricula;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(length = 100)
    @Builder.Default
    private String concepto = "PAGO_MATRICULA";

    @CreationTimestamp
    @Column(name = "fecha_pago", updatable = false)
    private LocalDateTime fechaPago;
}