package com.snkuni.sankuni.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void enviarCorreoSeguridad(String destinatario, String codigo) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(destinatario);
            mensaje.setSubject("SANKU - Código de Verificación de Seguridad");
            mensaje.setText("Hola,\n\n" +
                    "Se ha solicitado un cambio de contraseña para tu cuenta en el Instituto SANKU.\n" +
                    "Tu código de verificación de 6 dígitos es: " + codigo + "\n\n" +
                    "Si no solicitaste este cambio, ignora este correo.\n\n" +
                    "Atentamente,\nÁrea TI - SANKU.");
            mailSender.send(mensaje);
        } catch (Exception e) {
            System.out.println("Error al enviar el correo de seguridad: " + e.getMessage());
        }
    }

    @Async
    public void enviarCorreoBienvenida(String correoDestino, String usuarioLogin, String claveTemporal) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(correoDestino);
            mensaje.setSubject("¡Bienvenido al Instituto Tech! - Credenciales de Acceso");
            mensaje.setText("Hola,\n\n" +
                    "¡Felicidades! Tu matrícula ha sido aprobada exitosamente.\n\n" +
                    "Aquí tienes tus credenciales para acceder a la Intranet de estudiantes:\n" +
                    "Usuario/Correo: " + usuarioLogin + "\n" +
                    "Contraseña temporal: " + claveTemporal + "\n\n" +
                    "Por razones de seguridad, te recomendamos cambiar tu contraseña al ingresar por primera vez.\n\n" +
                    "Atentamente,\nCoordinación Académica - Instituto Tech");
            mailSender.send(mensaje);
        } catch (Exception e) {
            System.out.println("Error al enviar el correo de bienvenida: " + e.getMessage());
        }
    }
}