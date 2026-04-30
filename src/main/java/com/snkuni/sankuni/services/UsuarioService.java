package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.UsuarioDTO;
import com.snkuni.sankuni.models.Usuario;
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
                .map(this::mapearADto)
                .toList(); 
    }
    
    // Centralizamos el mapeo para incluir el DNI y los nombres separados
    private UsuarioDTO mapearADto(Usuario usuario) {
        return UsuarioDTO.builder()
                .idUsuario(usuario.getIdUsuario())
                .dni(usuario.getDni())
                .nombres(usuario.getNombres())
                .apellidos(usuario.getApellidos())
                .nombreCompleto(usuario.getNombreCompleto()) // Funciona gracias al @Transient
                .email(usuario.getEmail())
                .rol(usuario.getRol().name())
                .build();
    }
}