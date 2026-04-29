package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.UsuarioDTO;
import com.snkuni.sankuni.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarTodosLosUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(u -> UsuarioDTO.builder()
                        .idUsuario(u.getIdUsuario())
                        .nombreCompleto(u.getNombreCompleto())
                        .email(u.getEmail())
                        .rol(u.getRol().name())
                        .build())
                .toList(); 
    }
}