package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.DocenteDTO;
import com.snkuni.sankuni.dtos.UsuarioDTO;
import com.snkuni.sankuni.repositories.DocenteRepository;
import com.snkuni.sankuni.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class DocenteService {

    private final DocenteRepository docenteRepository;

    @Transactional(readOnly = true)
    public DocenteDTO obtenerPerfilPorUsuario(Long idUsuario) {
        return docenteRepository.findByUsuario_IdUsuario(idUsuario)
                .map(docente -> DocenteDTO.builder()
                        .idDocente(docente.getIdDocente())
                        .usuario(UsuarioDTO.builder()
                                .idUsuario(docente.getUsuario().getIdUsuario())
                                .nombreCompleto(docente.getUsuario().getNombreCompleto())
                                .email(docente.getUsuario().getEmail())
                                .rol(docente.getUsuario().getRol().name())
                                .build())
                        .especialidad(docente.getEspecialidad())
                        .estado(docente.getEstado().name())
                        .build())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de docente no encontrado"));
    }
}