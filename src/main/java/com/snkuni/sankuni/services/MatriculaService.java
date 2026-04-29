package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.MatriculaRequestDTO;
import com.snkuni.sankuni.exceptions.BusinessLogicException;
import com.snkuni.sankuni.repositories.MatriculaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;

    @Transactional
    public UUID procesarAutomatricula(MatriculaRequestDTO request) {
        try {
            // Delega la responsabilidad transaccional a tu Función en PostgreSQL
            return matriculaRepository.matricularAlumnoTransaccional(
                    request.getIdAlumno(),
                    request.getIdSeccion(),
                    request.getMontoPago()
            );
        } catch (Exception ex) {
            // Si PostgreSQL lanza un RAISE EXCEPTION por cruce de horarios, lo capturamos aquí
            throw new BusinessLogicException("Fallo en la matrícula: " + ex.getMessage());
        }
    }
}