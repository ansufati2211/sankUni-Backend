package com.snkuni.sankuni.controllers;

import com.snkuni.sankuni.dtos.EntregaEvaluacionDTO;
import com.snkuni.sankuni.services.ArchivoDescarga;
import com.snkuni.sankuni.services.EntregaEvaluacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/entregas")
@RequiredArgsConstructor
public class EntregaEvaluacionController {

    private final EntregaEvaluacionService entregaService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EntregaEvaluacionDTO> entregar(
            @RequestParam Long idEvaluacion,
            @RequestParam Long idAlumno,
            @RequestParam MultipartFile archivo) {
        EntregaEvaluacionDTO dto = entregaService.entregar(idEvaluacion, idAlumno, archivo);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/evaluacion/{idEvaluacion}")
    public ResponseEntity<List<EntregaEvaluacionDTO>> listarPorEvaluacion(@PathVariable Long idEvaluacion) {
        return ResponseEntity.ok(entregaService.listarPorEvaluacion(idEvaluacion));
    }

    @GetMapping("/alumno/{alumnoId}")
    public ResponseEntity<List<EntregaEvaluacionDTO>> listarPorAlumno(@PathVariable Long alumnoId) {
        return ResponseEntity.ok(entregaService.listarPorAlumno(alumnoId));
    }

    @GetMapping("/{id}/archivo")
    public ResponseEntity<Resource> descargarArchivo(@PathVariable Long id) {
        ArchivoDescarga archivo = entregaService.descargarArchivo(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivo.nombreDescarga() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(archivo.recurso());
    }
}
