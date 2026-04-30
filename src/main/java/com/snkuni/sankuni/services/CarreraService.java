package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.CarreraDTO;
import com.snkuni.sankuni.models.Carrera;
import com.snkuni.sankuni.models.enums.TipoPrograma;
import com.snkuni.sankuni.repositories.CarreraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarreraService {

    private final CarreraRepository carreraRepository;

    @Transactional(readOnly = true)
    public List<CarreraDTO> listarTodas() {
        return carreraRepository.findAll().stream()
                .map(this::mapearADto)
                .toList();
    }

    @Transactional
    public CarreraDTO crearCarrera(CarreraDTO dto) {
        Carrera nuevaCarrera = Carrera.builder()
                // Convertimos el String del DTO al Enum, si es nulo por defecto es CARRERA
                .tipo(dto.getTipo() != null ? TipoPrograma.valueOf(dto.getTipo()) : TipoPrograma.CARRERA)
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .perfilAcademico(dto.getPerfilAcademico())
                .mercadoLaboral(dto.getMercadoLaboral())
                .beneficios(dto.getBeneficios())
                .requisitos(dto.getRequisitos())
                .estado(true)
                .build();

        Carrera guardada = carreraRepository.save(nuevaCarrera);
        dto.setIdCarrera(guardada.getIdCarrera());
        return dto;
    }

    // Mapeo completo para la página web pública
    private CarreraDTO mapearADto(Carrera carrera) {
        return CarreraDTO.builder()
                .idCarrera(carrera.getIdCarrera())
                .tipo(carrera.getTipo() != null ? carrera.getTipo().name() : "CARRERA")
                .nombre(carrera.getNombre())
                .descripcion(carrera.getDescripcion())
                .perfilAcademico(carrera.getPerfilAcademico())
                .mercadoLaboral(carrera.getMercadoLaboral())
                .beneficios(carrera.getBeneficios())
                .requisitos(carrera.getRequisitos())
                .estado(carrera.getEstado())
                .build();
    }
}