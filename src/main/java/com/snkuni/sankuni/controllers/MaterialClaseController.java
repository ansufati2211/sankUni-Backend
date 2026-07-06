package com.snkuni.sankuni.controllers;

import com.snkuni.sankuni.dtos.MaterialClaseDTO;
import com.snkuni.sankuni.services.ArchivoDescarga;
import com.snkuni.sankuni.services.MaterialClaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/materiales")
@RequiredArgsConstructor
public class MaterialClaseController {

    private final MaterialClaseService materialService;

    // POST /api/v1/materiales -> Docente sube un archivo real (multipart)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MaterialClaseDTO> subirMaterial(
            @RequestParam Long idSeccion,
            @RequestParam String titulo,
            @RequestParam(required = false) Long idModulo,
            @RequestParam MultipartFile archivo) {
        MaterialClaseDTO dto = materialService.subirMaterial(idSeccion, titulo, idModulo, archivo);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    // GET /api/v1/materiales/seccion/1 -> Alumno o Docente ven los materiales
    @GetMapping("/seccion/{idSeccion}")
    public ResponseEntity<List<MaterialClaseDTO>> listarPorSeccion(@PathVariable Long idSeccion) {
        return ResponseEntity.ok(materialService.listarPorSeccion(idSeccion));
    }

    @GetMapping("/modulo/{idModulo}")
    public ResponseEntity<List<MaterialClaseDTO>> listarPorModulo(@PathVariable Long idModulo) {
        return ResponseEntity.ok(materialService.listarPorModulo(idModulo));
    }

    @GetMapping("/{id}/archivo")
    public ResponseEntity<Resource> descargarArchivo(@PathVariable Long id) {
        ArchivoDescarga archivo = materialService.descargarArchivo(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivo.nombreDescarga() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(archivo.recurso());
    }
}
