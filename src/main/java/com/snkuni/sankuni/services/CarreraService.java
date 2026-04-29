package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.CarreraDTO;
import com.snkuni.sankuni.models.Carrera;
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
        return carreraRepository.findAll().stream().map(carrera -> 
            CarreraDTO.builder()
                .idCarrera(carrera.getIdCarrera())
                .nombre(carrera.getNombre())
                .descripcion(carrera.getDescripcion())
                .estado(carrera.getEstado())
                .build()
        ).toList();
    }

    @Transactional
    public CarreraDTO crearCarrera(CarreraDTO dto) {
        Carrera nuevaCarrera = Carrera.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .estado(true)
                .build();

        Carrera guardada = carreraRepository.save(nuevaCarrera);
        dto.setIdCarrera(guardada.getIdCarrera());
        return dto;
    }
}