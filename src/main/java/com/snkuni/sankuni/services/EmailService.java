package com.snkuni.sankuni.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// LIBRERÍAS DE SMTP COMENTADAS PARA PRESERVAR FUNCIONALIDAD LOCAL SI SE DESEA
// import org.springframework.mail.SimpleMailMessage;
// import org.springframework.mail.javamail.JavaMailSender;

@Service
public class EmailService {

    // Inyección de la clave API desde application.properties / Variables de Entorno (Railway)
    @Value("${brevo.api.key}")
    private String brevoApiKey;
    
    // Tu correo de la empresa verificado
    private static final String SENDER_EMAIL = "jhalebet1994@gmail.com"; 

    /* ==========================================
     * CONFIGURACIÓN ANTERIOR (SMTP)
     * Conservada por si se desea regresar al modelo local
     * ==========================================
     * private final JavaMailSender mailSender;
     * 
     * public EmailService(JavaMailSender mailSender) {
     *     this.mailSender = mailSender;
     * }
     */

    @Async
    public void enviarCorreoSeguridad(String destinatario, String codigo) {
        try {
            String jsonBody = String.format(
                "{" +
                "\"sender\": {\"name\": \"Área TI - SANKU\", \"email\": \"%s\"}," +
                "\"to\": [{\"email\": \"%s\"}]," +
                "\"subject\": \"SANKU - Código de Verificación de Seguridad\"," +
                "\"htmlContent\": \"<div style='font-family: Arial, sans-serif; color: #333; padding: 20px;'>" +
                "<p>Hola,</p>" +
                "<p>Se ha solicitado un cambio de contraseña para tu cuenta en el Instituto SANKU.</p>" +
                "<p>Tu código de verificación de 6 dígitos es: <b style='color: #0056b3; font-size: 18px;'>%s</b></p>" +
                "<p>Si no solicitaste este cambio, ignora este correo.</p>" +
                "<br><p>Atentamente,<br>Área TI - SANKU</p>" +
                "</div>\"" +
                "}", SENDER_EMAIL, destinatario, codigo);

            enviarPeticionBrevo(jsonBody, "Seguridad");

            /* --- CÓDIGO ANTERIOR PARA SMTP LOCAL ---
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(destinatario);
            mensaje.setSubject("SANKU - Código de Verificación de Seguridad");
            mensaje.setText("Hola,\n\n" +
                    "Se ha solicitado un cambio de contraseña para tu cuenta en el Instituto SANKU.\n" +
                    "Tu código de verificación de 6 dígitos es: " + codigo + "\n\n" +
                    "Si no solicitaste este cambio, ignora este correo.\n\n" +
                    "Atentamente,\nÁrea TI - SANKU.");
            mailSender.send(mensaje);
            */

        } catch (Exception e) {
            System.out.println("Error al enviar el correo de seguridad: " + e.getMessage());
        }
    }

    @Async
    public void enviarCorreoBienvenida(String correoDestino, String usuarioLogin, String claveTemporal) {
        try {
            String jsonBody = String.format(
                "{" +
                "\"sender\": {\"name\": \"Instituto SANKU\", \"email\": \"%s\"}," +
                "\"to\": [{\"email\": \"%s\"}]," +
                "\"subject\": \"¡Bienvenido al Instituto SANKU! - Credenciales de Acceso\"," +
                "\"htmlContent\": \"<div style='font-family: Arial, sans-serif; color: #333; padding: 20px;'>" +
                "<h2>¡Hola, Felicidades!</h2>" +
                "<p>Tu matrícula ha sido aprobada exitosamente. Aquí tienes tus credenciales para acceder a la Intranet de estudiantes:</p>" +
                "<ul><li><b>Usuario/Correo:</b> %s</li>" +
                "<li><b>Contraseña temporal:</b> %s</li></ul>" +
                "<p>Por razones de seguridad, te recomendamos cambiar tu contraseña al ingresar por primera vez.</p>" +
                "<br><p>Atentamente,<br>Coordinación Académica - Instituto SANKU</p>" +
                "</div>\"" +
                "}", SENDER_EMAIL, correoDestino, usuarioLogin, claveTemporal);

            enviarPeticionBrevo(jsonBody, "Bienvenida");

            /* --- CÓDIGO ANTERIOR PARA SMTP LOCAL ---
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
            */

        } catch (Exception e) {
            System.out.println("Error al enviar el correo de bienvenida: " + e.getMessage());
        }
    }

    // Método que gestiona la comunicación directa con la API de Brevo
    private void enviarPeticionBrevo(String jsonBody, String tipoCorreo) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.brevo.com/v3/smtp/email"))
                    .header("api-key", brevoApiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            System.out.println("Correo de " + tipoCorreo + " procesado vía API. Código HTTP: " + response.statusCode());
        } catch (Exception e) {
            System.out.println("Error de conexión con Brevo (" + tipoCorreo + "): " + e.getMessage());
        }
    }
}