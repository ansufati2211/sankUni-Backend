package com.snkuni.sankuni.dtos;

import lombok.*;
import java.math.BigDecimal; // <-- AGREGADO
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PagoDTO {
    private Long idPago;
    private Long idCuota;
    private BigDecimal montoPagado; // <-- ASEGÚRATE QUE SEA BigDecimal
    private String metodoPago;
    private LocalDateTime fechaPago;
}