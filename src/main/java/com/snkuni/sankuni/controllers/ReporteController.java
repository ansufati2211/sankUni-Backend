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

    @GetMapping("/horario-docente/{docenteId}")
    public ResponseEntity<List<Map<String, Object>>> getHorarioDocente(@PathVariable("docenteId") Long docenteId) {
        return ResponseEntity.ok(reporteService.obtenerHorarioDocente(docenteId));
    }

    @GetMapping("/dashboard-admin")
    public ResponseEntity<DashboardAdminDTO> getDashboardAdmin() {
        return ResponseEntity.ok(reporteService.obtenerDashboardAdmin());
    }
    
    @GetMapping("/rendimiento")
    public ResponseEntity<List<Map<String, Object>>> getRendimientoGlobal() {
        return ResponseEntity.ok(reporteService.obtenerRendimientoGlobal());
    }

    // 🚀 NUEVO: Puerta de acceso para descargar la data del Gráfico
    @GetMapping("/matriculas-chart")
    public ResponseEntity<List<Map<String, Object>>> getMatriculasChart() {
        return ResponseEntity.ok(reporteService.obtenerEvolucionMatriculas());
    }
}