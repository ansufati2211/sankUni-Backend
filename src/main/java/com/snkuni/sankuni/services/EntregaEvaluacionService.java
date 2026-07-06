package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.EntregaEvaluacionDTO;
import com.snkuni.sankuni.exceptions.BusinessLogicException;
import com.snkuni.sankuni.exceptions.ResourceNotFoundException;
import com.snkuni.sankuni.models.Alumno;
import com.snkuni.sankuni.models.EntregaEvaluacion;
import com.snkuni.sankuni.models.Evaluacion;
import com.snkuni.sankuni.repositories.AlumnoRepository;
import com.snkuni.sankuni.repositories.EntregaEvaluacionRepository;
import com.snkuni.sankuni.repositories.EvaluacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EntregaEvaluacionService {

    private final EntregaEvaluacionRepository entregaRepository;
    private final EvaluacionRepository evaluacionRepository;
    private final AlumnoRepository alumnoRepository;
    private final StorageService storageService;

    @Transactional
    public EntregaEvaluacionDTO entregar(Long idEvaluacion, Long idAlumno, MultipartFile archivo) {
        Evaluacion evaluacion = evaluacionRepository.findById(idEvaluacion)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluación no encontrada"));
        Alumno alumno = alumnoRepository.findById(idAlumno)
                .orElseThrow(() -> new ResourceNotFoundException("Alumno no encontrado"));

        if (entregaRepository.existsByEvaluacion_IdEvaluacionAndAlumno_IdAlumno(idEvaluacion, idAlumno)) {
            throw new BusinessLogicException("Ya existe una entrega registrada para esta evaluación.");
        }

        String rutaArchivo = storageService.store(archivo, "entregas");

        EntregaEvaluacion entrega = EntregaEvaluacion.builder()
                .evaluacion(evaluacion)
                .alumno(alumno)
                .archivoUrl(rutaArchivo)
                .build();
        entregaRepository.save(entrega);

        return mapearADto(entrega);
    }

    @Transactional(readOnly = true)
    public List<EntregaEvaluacionDTO> listarPorEvaluacion(Long idEvaluacion) {
        return entregaRepository.findByEvaluacion_IdEvaluacion(idEvaluacion).stream()
                .map(this::mapearADto).toList();
    }

    @Transactional(readOnly = true)
    public List<EntregaEvaluacionDTO> listarPorAlumno(Long idAlumno) {
        return entregaRepository.findByAlumno_IdAlumno(idAlumno).stream()
                .map(this::mapearADto).toList();
    }

    @Transactional(readOnly = true)
    public ArchivoDescarga descargarArchivo(Long idEntrega) {
        EntregaEvaluacion entrega = entregaRepository.findById(idEntrega)
                .orElseThrow(() -> new ResourceNotFoundException("Entrega no encontrada"));
        Resource recurso = storageService.load(entrega.getArchivoUrl());
        String nombreBase = "entrega-" + entrega.getAlumno().getIdAlumno();
        return new ArchivoDescarga(recurso, nombreBase, entrega.getArchivoUrl());
    }

    private EntregaEvaluacionDTO mapearADto(EntregaEvaluacion e) {
        return EntregaEvaluacionDTO.builder()
                .idEntrega(e.getIdEntrega())
                .idEvaluacion(e.getEvaluacion().getIdEvaluacion())
                .idAlumno(e.getAlumno().getIdAlumno())
                .archivoUrl(e.getArchivoUrl())
                .fechaEntrega(e.getFechaEntrega())
                .build();
    }
}
