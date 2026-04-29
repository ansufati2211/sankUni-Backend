package com.snkuni.sankuni.controllers;

import com.snkuni.sankuni.dtos.PagoDTO;
import com.snkuni.sankuni.services.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    @GetMapping
    public ResponseEntity<List<PagoDTO>> listarPagos() {
        return ResponseEntity.ok(pagoService.listarTodosLosPagos());
    }
}