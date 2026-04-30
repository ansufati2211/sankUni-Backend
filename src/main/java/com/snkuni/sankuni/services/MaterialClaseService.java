package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.MaterialClaseDTO;
import com.snkuni.sankuni.models.MaterialClase;
import com.snkuni.sankuni.models.Seccion;
import com.snkuni.sankuni.repositories.MaterialClaseRepository;
import com.snkuni.sankuni.repositories.SeccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialClaseService {
    private final MaterialClaseRepository materialRepository;
    private final SeccionRepository seccionRepository;

    @Transactional
    public MaterialClaseDTO subirMaterial(MaterialClaseDTO dto) {
        Seccion seccion = seccionRepository.findById(dto.getIdSeccion()).orElseThrow();
        MaterialClase m = MaterialClase.builder()
                .seccion(seccion).titulo(dto.getTitulo()).archivoUrl(dto.getArchivoUrl()).build();
        materialRepository.save(m);
        dto.setIdMaterial(m.getIdMaterial());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<MaterialClaseDTO> listarPorSeccion(Long idSeccion) {
        return materialRepository.findBySeccion_IdSeccion(idSeccion).stream()
                .map(m -> MaterialClaseDTO.builder()
                        .idMaterial(m.getIdMaterial()).idSeccion(m.getSeccion().getIdSeccion())
                        .titulo(m.getTitulo()).archivoUrl(m.getArchivoUrl()).fechaSubida(m.getFechaSubida())
                        .build()).toList();
    }
}