package com.snkuni.sankuni.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class EmailService {

    // La clave API se inyecta desde Railway, NO SE CAMBIA.
    @Value("${brevo.api.key}")
    private String brevoApiKey;
    
    // TU NUEVO CORREO OFICIAL (Recuerda que debes haberlo verificado en Brevo)
    private static final String SENDER_EMAIL = "jhalebet1994@outlook.com"; 

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
        } catch (Exception e) {
            System.err.println("Error al preparar correo de seguridad: " + e.getMessage());
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
        } catch (Exception e) {
            System.err.println("Error al preparar correo de bienvenida: " + e.getMessage());
        }
    }

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
            
            System.out.println("Correo de " + tipoCorreo + " procesado vía API Brevo. Código HTTP: " + response.statusCode());
        } catch (Exception e) {
            System.err.println("Error de conexión con Brevo (" + tipoCorreo + "): " + e.getMessage());
        }
    }
}