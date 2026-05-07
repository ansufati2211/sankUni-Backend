package com.snkuni.sankuni.controllers;

import com.snkuni.sankuni.dtos.PagoDTO;
import com.snkuni.sankuni.services.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    @GetMapping
    public ResponseEntity<List<PagoDTO>> listarTodosLosPagos() {
        return ResponseEntity.ok(pagoService.listarTodosLosPagos());
    }

    @GetMapping("/cuota/{idCuota}")
    public ResponseEntity<List<PagoDTO>> listarPorCuota(@PathVariable("idCuota") Long idCuota) {
        return ResponseEntity.ok(pagoService.listarPorCuota(idCuota));
    }

    // NUEVO: LA PASARELA DE PAGOS MODIFICADA
    @PostMapping("/pagar")
    public ResponseEntity<PagoDTO> pagarCuota(@RequestBody Map<String, Object> payload) { 
        
        // Extraemos los datos del Map (Payload)
        Long idCuota = Long.valueOf(payload.get("idCuota").toString());
        BigDecimal monto = new BigDecimal(payload.get("monto").toString());
        String metodoPago = payload.get("metodoPago").toString();
        
        return ResponseEntity.ok(pagoService.procesarPago(idCuota, monto, metodoPago));
    }
}