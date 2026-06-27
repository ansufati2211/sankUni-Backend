package com.snkuni.sankuni.services;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void enviarCorreoSeguridad(String destinatario, String codigo) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destinatario);
        mensaje.setSubject("SANKU - Código de Verificación de Seguridad");
        mensaje.setText("Hola,\n\nSe ha solicitado un cambio de contraseña para tu cuenta en el Instituto SANKU.\n\n"
                + "Tu código de verificación de 6 dígitos es: " + codigo + "\n\n"
                + "Si no solicitaste este cambio, ignora este correo.\n\nAtentamente,\nÁrea TI - SANKU.");
        
        mailSender.send(mensaje);
    }
}