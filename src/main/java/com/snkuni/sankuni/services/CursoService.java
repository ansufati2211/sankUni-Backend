package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.CursoDTO;
import com.snkuni.sankuni.models.Carrera;
import com.snkuni.sankuni.models.Curso;
import com.snkuni.sankuni.repositories.CarreraRepository;
import com.snkuni.sankuni.repositories.CursoRepository;
import com.snkuni.sankuni.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CursoService {
    private final CursoRepository cursoRepository;
    private final CarreraRepository carreraRepository;

    @Transactional
    public CursoDTO crearCurso(CursoDTO dto) {
        // Validación preventiva del ID de carrera[cite: 6]
        Carrera carrera = carreraRepository.findById(dto.getCarreraId())
                .orElseThrow(() -> new ResourceNotFoundException("La carrera seleccionada no existe en el sistema."));

        // Asignación de créditos: usa el valor del front o el default de la BD (3)[cite: 5, 6]
        Integer numCreditos = (dto.getCreditos() == null || dto.getCreditos() < 1) ? 3 : dto.getCreditos();

        Curso nuevoCurso = Curso.builder()
                .carrera(carrera)
                .nombre(dto.getNombre())
                .creditos(numCreditos)
                .descripcionInformativa(dto.getDescripcionInformativa())
                .build();

        Curso guardado = cursoRepository.save(nuevoCurso);
        
        // Actualizamos el DTO de respuesta con los datos reales guardados
        dto.setIdCurso(guardado.getIdCurso());
        dto.setNombreCarrera(carrera.getNombre());
        dto.setCreditos(numCreditos);
        return dto;
    }

    @Transactional(readOnly = true)
    public List<CursoDTO> listarTodos() {
        return cursoRepository.findAll().stream().map(this::mapearADto).toList();
    }

    private CursoDTO mapearADto(Curso curso) {
        return CursoDTO.builder()
                .idCurso(curso.getIdCurso())
                .carreraId(curso.getCarrera().getIdCarrera())
                .nombreCarrera(curso.getCarrera().getNombre())
                .nombre(curso.getNombre())
                .creditos(curso.getCreditos())
                .descripcionInformativa(curso.getDescripcionInformativa())
                .build();
    }
}