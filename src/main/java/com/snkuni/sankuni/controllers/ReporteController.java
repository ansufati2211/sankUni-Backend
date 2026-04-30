package com.snkuni.sankuni.controllers;

import com.snkuni.sankuni.dtos.DashboardAdminDTO;
import com.snkuni.sankuni.services.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping("/financiero")
    public ResponseEntity<List<Map<String, Object>>> getFinanciero() {
        return ResponseEntity.ok(reporteService.obtenerReporteFinanciero());
    }

    @GetMapping("/historial/{alumnoId}")
    public ResponseEntity<List<Map<String, Object>>> getHistorial(@PathVariable("alumnoId") Long alumnoId) {
        return ResponseEntity.ok(reporteService.obtenerHistorialAcademico(alumnoId));
    }

    @GetMapping("/semaforo/{docenteId}")
    public ResponseEntity<List<Map<String, Object>>> getSemaforo(@PathVariable("docenteId") Long docenteId) {
        return ResponseEntity.ok(reporteService.obtenerSemaforoNotasDeDocente(docenteId));
    }

    @GetMapping("/horario/{alumnoId}")
    public ResponseEntity<List<Map<String, Object>>> getHorario(@PathVariable("alumnoId") Long alumnoId) {
        return ResponseEntity.ok(reporteService.obtenerHorarioAlumno(alumnoId));
    }

    // NUEVO: Endpoint para el Dashboard del Administrador
    @GetMapping("/dashboard-admin")
    public ResponseEntity<DashboardAdminDTO> getDashboardAdmin() {
        return ResponseEntity.ok(reporteService.obtenerDashboardAdmin());
    }
}