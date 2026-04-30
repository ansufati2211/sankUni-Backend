package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.MatriculaDTO;
import com.snkuni.sankuni.dtos.MatriculaRequestDTO;
import com.snkuni.sankuni.exceptions.BusinessLogicException;
import com.snkuni.sankuni.repositories.MatriculaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;

    @Transactional
    public Long procesarAutomatricula(MatriculaRequestDTO request) {
        try {
            return matriculaRepository.matricularAlumnoTransaccional(
                    request.getIdAlumno(),
                    request.getIdSeccion(),
                    request.getMontoPago()
            );
        } catch (Exception ex) {
            throw new BusinessLogicException("Fallo en la matrícula: " + ex.getMessage());
        }
    }

    // NUEVO: Para que el docente vea a los alumnos de su clase
    @Transactional(readOnly = true)
    public List<MatriculaDTO> listarAlumnosPorSeccion(Long idSeccion) {
        return matriculaRepository.findBySeccion_IdSeccion(idSeccion).stream()
                .map(m -> MatriculaDTO.builder()
                        .idMatricula(m.getIdMatricula())
                        .nombreAlumno(m.getAlumno().getUsuario().getNombreCompleto())
                        .cursoYSeccion(m.getSeccion().getCurso().getNombre())
                        .notaFinal(m.getNotaFinal())
                        .build()).toList();
    }
}