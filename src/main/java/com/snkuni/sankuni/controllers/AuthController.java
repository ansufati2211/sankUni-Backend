package com.snkuni.sankuni.controllers;

import com.snkuni.sankuni.dtos.AuthRequestDTO;
import com.snkuni.sankuni.dtos.AuthResponseDTO;
import com.snkuni.sankuni.models.Usuario;
import com.snkuni.sankuni.repositories.UsuarioRepository;
import com.snkuni.sankuni.security.JwtService;
import com.snkuni.sankuni.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> authenticate(@Valid @RequestBody AuthRequestDTO request) {
        // 1. Autenticar credenciales
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        // 2. Buscar al usuario en la BD
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        
        // 3. Generar Token
        String jwtToken = jwtService.generateToken(new UserDetailsImpl(usuario));
        
        // 4. Retornar DTO (AQUÍ FALTABA EL ID)
        return ResponseEntity.ok(AuthResponseDTO.builder()
                .token(jwtToken)
                .nombre(usuario.getNombreCompleto())
                .rol(usuario.getRol().name())
                .usuarioId(usuario.getIdUsuario()) 
                .build());
    }
}