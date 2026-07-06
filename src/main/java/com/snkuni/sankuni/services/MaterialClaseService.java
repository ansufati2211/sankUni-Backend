package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.MaterialClaseDTO;
import com.snkuni.sankuni.exceptions.BusinessLogicException;
import com.snkuni.sankuni.exceptions.ResourceNotFoundException;
import com.snkuni.sankuni.models.MaterialClase;
import com.snkuni.sankuni.models.ModuloCurso;
import com.snkuni.sankuni.models.Seccion;
import com.snkuni.sankuni.repositories.MaterialClaseRepository;
import com.snkuni.sankuni.repositories.ModuloCursoRepository;
import com.snkuni.sankuni.repositories.SeccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialClaseService {
    private final MaterialClaseRepository materialRepository;
    private final SeccionRepository seccionRepository;
    private final ModuloCursoRepository moduloRepository;
    private final StorageService storageService;

    @Transactional
    public MaterialClaseDTO subirMaterial(Long idSeccion, String titulo, Long idModulo, MultipartFile archivo) {
        Seccion seccion = seccionRepository.findById(idSeccion)
                .orElseThrow(() -> new ResourceNotFoundException("Sección no encontrada"));

        ModuloCurso modulo = resolverModulo(idModulo, seccion);

        String rutaArchivo = storageService.store(archivo, "materiales");

        MaterialClase m = MaterialClase.builder()
                .seccion(seccion)
                .modulo(modulo)
                .titulo(titulo)
                .archivoUrl(rutaArchivo)
                .build();
        materialRepository.save(m);

        return mapearADto(m);
    }

    @Transactional(readOnly = true)
    public List<MaterialClaseDTO> listarPorSeccion(Long idSeccion) {
        return materialRepository.findBySeccion_IdSeccion(idSeccion).stream()
                .map(this::mapearADto).toList();
    }

    @Transactional(readOnly = true)
    public List<MaterialClaseDTO> listarPorModulo(Long idModulo) {
        return materialRepository.findByModulo_IdModulo(idModulo).stream()
                .map(this::mapearADto).toList();
    }

    @Transactional(readOnly = true)
    public ArchivoDescarga descargarArchivo(Long idMaterial) {
        MaterialClase material = materialRepository.findById(idMaterial)
                .orElseThrow(() -> new ResourceNotFoundException("Material no encontrado"));
        Resource recurso = storageService.load(material.getArchivoUrl());
        return new ArchivoDescarga(recurso, material.getTitulo(), material.getArchivoUrl());
    }

    @Transactional
    public void eliminar(Long idMaterial) {
        MaterialClase material = materialRepository.findById(idMaterial)
                .orElseThrow(() -> new ResourceNotFoundException("Material no encontrado"));
        materialRepository.delete(material);
        storageService.delete(material.getArchivoUrl());
    }

    private ModuloCurso resolverModulo(Long idModulo, Seccion seccion) {
        if (idModulo == null) {
            return null;
        }
        ModuloCurso modulo = moduloRepository.findById(idModulo)
                .orElseThrow(() -> new ResourceNotFoundException("Módulo no encontrado"));
        if (!modulo.getCurso().getIdCurso().equals(seccion.getCurso().getIdCurso())) {
            throw new BusinessLogicException("El módulo indicado no pertenece al curso de la sección seleccionada.");
        }
        return modulo;
    }

    private MaterialClaseDTO mapearADto(MaterialClase m) {
        return MaterialClaseDTO.builder()
                .idMaterial(m.getIdMaterial())
                .idSeccion(m.getSeccion().getIdSeccion())
                .idModulo(m.getModulo() != null ? m.getModulo().getIdModulo() : null)
                .titulo(m.getTitulo())
                .archivoUrl(m.getArchivoUrl())
                .fechaSubida(m.getFechaSubida())
                .build();
    }
}
