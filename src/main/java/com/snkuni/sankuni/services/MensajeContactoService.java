package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.MensajeContactoDTO;
import com.snkuni.sankuni.models.MensajeContacto;
import com.snkuni.sankuni.repositories.MensajeContactoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MensajeContactoService {

    private final MensajeContactoRepository mensajeRepository;

    @Transactional
    public MensajeContactoDTO guardarMensaje(MensajeContactoDTO dto) {
        MensajeContacto mensaje = MensajeContacto.builder()
                .nombreCompleto(dto.getNombreCompleto())
                .correo(dto.getCorreo())
                .celular(dto.getCelular())
                .programaInteres(dto.getProgramaInteres())
                .mensaje(dto.getMensaje())
                .build();
        mensajeRepository.save(mensaje);
        return dto;
    }

    @Transactional(readOnly = true)
    public List<MensajeContactoDTO> listarPendientes() {
        return mensajeRepository.findByEstadoAtencionFalse().stream()
                .map(m -> MensajeContactoDTO.builder()
                        .nombreCompleto(m.getNombreCompleto())
                        .correo(m.getCorreo())
                        .celular(m.getCelular())
                        .programaInteres(m.getProgramaInteres())
                        .mensaje(m.getMensaje())
                        .build())
                .toList();
    }
}