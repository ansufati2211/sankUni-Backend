package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.ModuloCursoDTO;
import com.snkuni.sankuni.exceptions.ResourceNotFoundException;
import com.snkuni.sankuni.models.Curso;
import com.snkuni.sankuni.models.ModuloCurso;
import com.snkuni.sankuni.repositories.CursoRepository;
import com.snkuni.sankuni.repositories.EvaluacionRepository;
import com.snkuni.sankuni.repositories.MaterialClaseRepository;
import com.snkuni.sankuni.repositories.ModuloCursoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModuloCursoService {

    private final ModuloCursoRepository moduloRepository;
    private final CursoRepository cursoRepository;
    private final MaterialClaseRepository materialRepository;
    private final EvaluacionRepository evaluacionRepository;

    @Transactional
    public ModuloCursoDTO crear(ModuloCursoDTO dto) {
        Curso curso = cursoRepository.findById(dto.getIdCurso())
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado"));

        ModuloCurso modulo = ModuloCurso.builder()
                .curso(curso)
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .orden(dto.getOrden() != null ? dto.getOrden() : 0)
                .build();

        return mapearADto(moduloRepository.save(modulo));
    }

    @Transactional(readOnly = true)
    public List<ModuloCursoDTO> listarPorCurso(Long idCurso) {
        return moduloRepository.findByCurso_IdCursoOrderByOrdenAsc(idCurso).stream()
                .map(this::mapearADto).toList();
    }

    @Transactional
    public ModuloCursoDTO editar(Long id, ModuloCursoDTO dto) {
        ModuloCurso modulo = moduloRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Módulo no encontrado"));

        modulo.setTitulo(dto.getTitulo());
        modulo.setDescripcion(dto.getDescripcion());
        if (dto.getOrden() != null) {
            modulo.setOrden(dto.getOrden());
        }

        return mapearADto(moduloRepository.save(modulo));
    }

    @Transactional
    public void eliminar(Long id) {
        ModuloCurso modulo = moduloRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Módulo no encontrado"));

        // Vaciar el módulo: el material y las evaluaciones no se borran, solo quedan
        // sin agrupar (moduloId = null). Así eliminar un módulo nunca falla por tener
        // contenido cargado, ni destruye archivos o evaluaciones que ya tengan notas.
        var materiales = materialRepository.findByModulo_IdModulo(id);
        materiales.forEach(m -> m.setModulo(null));
        materialRepository.saveAll(materiales);

        var evaluaciones = evaluacionRepository.findByModulo_IdModulo(id);
        evaluaciones.forEach(e -> e.setModulo(null));
        evaluacionRepository.saveAll(evaluaciones);

        moduloRepository.delete(modulo);
    }

    private ModuloCursoDTO mapearADto(ModuloCurso m) {
        return ModuloCursoDTO.builder()
                .idModulo(m.getIdModulo())
                .idCurso(m.getCurso().getIdCurso())
                .titulo(m.getTitulo())
                .descripcion(m.getDescripcion())
                .orden(m.getOrden())
                .build();
    }
}
