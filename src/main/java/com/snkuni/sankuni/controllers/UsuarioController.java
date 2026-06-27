package com.snkuni.sankuni.controllers;

import com.snkuni.sankuni.dtos.UsuarioDTO;
import com.snkuni.sankuni.services.UsuarioService;
import com.snkuni.sankuni.exceptions.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarTodosLosUsuarios());
    }

    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody UsuarioDTO dto) {
        try {
            return ResponseEntity.ok(usuarioService.crearUsuario(dto));
        } catch (BusinessLogicException e) {
            // Envía el mensaje EXACTO al Frontend en lugar de dar Error 500
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioDTO dto) {
        try {
            return ResponseEntity.ok(usuarioService.actualizarUsuario(id, dto));
        } catch (BusinessLogicException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        try {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (BusinessLogicException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping("/{id}/password")
    public ResponseEntity<?> actualizarPassword(@PathVariable Long id, @RequestBody java.util.Map<String, String> body) {
        try {
            String nuevaPassword = body.get("nuevaPassword");
            usuarioService.actualizarPassword(id, nuevaPassword);
            return ResponseEntity.ok().build();
        } catch (BusinessLogicException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/enviar-codigo")
    public ResponseEntity<?> enviarCodigo(@PathVariable Long id) {
        try {
            usuarioService.generarYEnviarCodigo(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/verificar-codigo")
    public ResponseEntity<?> verificarCodigo(@PathVariable Long id, @RequestBody java.util.Map<String, String> body) {
        try {
            usuarioService.verificarCodigo(id, body.get("codigo"));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}