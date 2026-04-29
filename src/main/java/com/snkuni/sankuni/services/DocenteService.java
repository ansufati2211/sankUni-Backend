package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.DocenteDTO;
import com.snkuni.sankuni.dtos.UsuarioDTO;
import com.snkuni.sankuni.models.Docente;
import com.snkuni.sankuni.repositories.DocenteRepository;
import com.snkuni.sankuni.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocenteService {

    private final DocenteRepository docenteRepository;

    @Transactional(readOnly = true)
    public List<DocenteDTO> listarTodos() {
        return docenteRepository.findAll().stream()
                .map(this::mapearADto)
                .toList();
    }

    @Transactional(readOnly = true)
    public DocenteDTO obtenerPorId(Long id) {
        return docenteRepository.findById(id)
                .map(this::mapearADto)
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado con ID: " + id));
    }

    @Transactional(readOnly = true)
    public DocenteDTO obtenerPerfilPorUsuario(Long usuarioId) {
        return docenteRepository.findByUsuario_IdUsuario(usuarioId)
                .map(this::mapearADto)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de docente no encontrado"));
    }

    private DocenteDTO mapearADto(Docente docente) {
        return DocenteDTO.builder()
                .idDocente(docente.getIdDocente())
                .usuario(UsuarioDTO.builder()
                        .idUsuario(docente.getUsuario().getIdUsuario())
                        .nombreCompleto(docente.getUsuario().getNombreCompleto())
                        .email(docente.getUsuario().getEmail())
                        .rol(docente.getUsuario().getRol().name())
                        .build())
                .especialidad(docente.getEspecialidad())
                .estado(docente.getEstado().name())
                .build();
    }
}