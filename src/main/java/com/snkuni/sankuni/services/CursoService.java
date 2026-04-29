package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.CursoDTO;
import com.snkuni.sankuni.models.Carrera;
import com.snkuni.sankuni.models.Curso;
import com.snkuni.sankuni.repositories.CarreraRepository;
import com.snkuni.sankuni.repositories.CursoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CursoService {

    private final CursoRepository cursoRepository;
    private final CarreraRepository carreraRepository;

    @Transactional(readOnly = true)
    public List<CursoDTO> listarPorCarrera(UUID idCarrera) {
        return cursoRepository.findByCarrera_IdCarrera(idCarrera).stream().map(curso -> 
            CursoDTO.builder()
                .idCurso(curso.getIdCurso())
                .carreraId(curso.getCarrera().getIdCarrera())
                .nombreCarrera(curso.getCarrera().getNombre())
                .nombre(curso.getNombre())
                .creditos(curso.getCreditos())
                .temarioUrl(curso.getTemarioUrl())
                .descripcionInformativa(curso.getDescripcionInformativa())
                .build()
        ).toList();
    }

    @Transactional
    public CursoDTO crearCurso(CursoDTO dto) {
        Carrera carrera = carreraRepository.findById(dto.getCarreraId())
                .orElseThrow(() -> new IllegalArgumentException("Carrera no encontrada."));

        Curso nuevoCurso = Curso.builder()
                .carrera(carrera)
                .nombre(dto.getNombre())
                .creditos(dto.getCreditos())
                .temarioUrl(dto.getTemarioUrl())
                .descripcionInformativa(dto.getDescripcionInformativa())
                .build();

        Curso guardado = cursoRepository.save(nuevoCurso);
        dto.setIdCurso(guardado.getIdCurso());
        dto.setNombreCarrera(carrera.getNombre());
        return dto;
    }
}