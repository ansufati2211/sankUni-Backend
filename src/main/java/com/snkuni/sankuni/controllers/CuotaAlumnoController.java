package com.snkuni.sankuni.controllers;

import com.snkuni.sankuni.dtos.CuotaAlumnoDTO;
import com.snkuni.sankuni.services.CuotaAlumnoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cuotas")
@RequiredArgsConstructor
public class CuotaAlumnoController {

    private final CuotaAlumnoService cuotaService;

    // Para la intranet del ALUMNO (ya lo tenías)
    @GetMapping("/alumno/{idAlumno}")
    public ResponseEntity<List<CuotaAlumnoDTO>> listarPorAlumno(@PathVariable Long idAlumno) {
        return ResponseEntity.ok(cuotaService.listarPorAlumno(idAlumno));
    }
    
    @GetMapping("/todas")
    public ResponseEntity<List<CuotaAlumnoDTO>> listarTodas() {
        return ResponseEntity.ok(cuotaService.listarTodasLasCuotas());
    }
}