package com.snkuni.sankuni.controllers;

import com.snkuni.sankuni.dtos.NotificacionDTO;
import com.snkuni.sankuni.services.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;

    @GetMapping("/admin")
    public ResponseEntity<List<NotificacionDTO>> obtenerNotificacionesAdmin() {
        return ResponseEntity.ok(notificacionService.obtenerNotificacionesAdmin());
    }
}