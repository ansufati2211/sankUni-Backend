package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.AnuncioSeccionDTO;
import com.snkuni.sankuni.exceptions.ResourceNotFoundException;
import com.snkuni.sankuni.models.AnuncioSeccion;
import com.snkuni.sankuni.models.Matricula;
import com.snkuni.sankuni.models.Seccion;
import com.snkuni.sankuni.repositories.AnuncioSeccionRepository;
import com.snkuni.sankuni.repositories.MatriculaRepository;
import com.snkuni.sankuni.repositories.SeccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnuncioSeccionService {

    private final AnuncioSeccionRepository anuncioRepository;
    private final SeccionRepository seccionRepository;
    private final MatriculaRepository matriculaRepository;

    @Transactional
    public AnuncioSeccionDTO publicar(AnuncioSeccionDTO dto) {
        Seccion seccion = seccionRepository.findById(dto.getIdSeccion())
                .orElseThrow(() -> new ResourceNotFoundException("Sección no encontrada"));

        AnuncioSeccion anuncio = AnuncioSeccion.builder()
                .seccion(seccion)
                .titulo(dto.getTitulo())
                .contenido(dto.getContenido())
                .build();

        return mapearADto(anuncioRepository.save(anuncio));
    }

    @Transactional(readOnly = true)
    public List<AnuncioSeccionDTO> listarPorSeccion(Long idSeccion) {
        return anuncioRepository.findBySeccion_IdSeccionOrderByFechaPublicacionDesc(idSeccion).stream()
                .map(this::mapearADto).toList();
    }

    @Transactional(readOnly = true)
    public List<AnuncioSeccionDTO> listarPorAlumno(Long idAlumno) {
        List<Long> idsSeccion = matriculaRepository.findByAlumno_IdAlumno(idAlumno).stream()
                .map(Matricula::getSeccion)
                .map(Seccion::getIdSeccion)
                .toList();
        if (idsSeccion.isEmpty()) {
            return List.of();
        }
        return anuncioRepository.findBySeccion_IdSeccionInOrderByFechaPublicacionDesc(idsSeccion).stream()
                .map(this::mapearADto).toList();
    }

    @Transactional
    public void eliminar(Long id) {
        if (!anuncioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Anuncio no encontrado");
        }
        anuncioRepository.deleteById(id);
    }

    private AnuncioSeccionDTO mapearADto(AnuncioSeccion a) {
        return AnuncioSeccionDTO.builder()
                .idAnuncio(a.getIdAnuncio())
                .idSeccion(a.getSeccion().getIdSeccion())
                .titulo(a.getTitulo())
                .contenido(a.getContenido())
                .fechaPublicacion(a.getFechaPublicacion())
                .build();
    }
}
