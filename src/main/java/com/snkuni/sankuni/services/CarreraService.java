package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.CarreraDTO;
import com.snkuni.sankuni.models.Carrera;
import com.snkuni.sankuni.models.enums.TipoPrograma;
import com.snkuni.sankuni.repositories.CarreraRepository;
import com.snkuni.sankuni.exceptions.ResourceNotFoundException;
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
                .tipo(dto.getTipo() != null ? TipoPrograma.valueOf(dto.getTipo()) : TipoPrograma.CARRERA)
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .perfilAcademico(dto.getPerfilAcademico())
                .mercadoLaboral(dto.getMercadoLaboral())
                .beneficios(dto.getBeneficios())
                .requisitos(dto.getRequisitos())
                // Toma el estado del Frontend, si no lo mandan, por defecto es true (Activo)
                .estado(dto.getEstado() != null ? dto.getEstado() : true)
                .build();

        Carrera guardada = carreraRepository.save(nuevaCarrera);
        dto.setIdCarrera(guardada.getIdCarrera());
        return dto;
    }

    // EL NUEVO MÉTODO DE EDICIÓN CON ESTADO
    @Transactional
    public CarreraDTO editarCarrera(Long id, CarreraDTO dto) {
        Carrera carrera = carreraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Programa no encontrado"));

        carrera.setTipo(TipoPrograma.valueOf(dto.getTipo()));
        carrera.setNombre(dto.getNombre());
        carrera.setDescripcion(dto.getDescripcion());
        carrera.setPerfilAcademico(dto.getPerfilAcademico());
        carrera.setMercadoLaboral(dto.getMercadoLaboral());
        carrera.setBeneficios(dto.getBeneficios());
        carrera.setRequisitos(dto.getRequisitos());
        
        if (dto.getEstado() != null) {
            carrera.setEstado(dto.getEstado());
        }

        return mapearADto(carreraRepository.save(carrera));
    }

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