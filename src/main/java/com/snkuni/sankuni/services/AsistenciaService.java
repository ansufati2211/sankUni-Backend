package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.AsistenciaRequestDTO;
import com.snkuni.sankuni.exceptions.BusinessLogicException;
import com.snkuni.sankuni.repositories.AsistenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;

    @Transactional
    public void registrarAsistencia(AsistenciaRequestDTO request) {
        try {
            // Llama a tu CALL sp_registrar_asistencia(...)
            asistenciaRepository.registrarAsistenciaMasiva(
                    request.getSeccionId(),
                    request.getAlumnoId(),
                    request.getPresente()
            );
        } catch (Exception ex) {
            throw new BusinessLogicException("No se pudo registrar la asistencia: " + ex.getMessage());
        }
    }
}