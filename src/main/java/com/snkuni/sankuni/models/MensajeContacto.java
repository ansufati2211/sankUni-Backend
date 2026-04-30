package com.snkuni.sankuni.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "mensajes_contacto")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MensajeContacto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMensaje;

    @Column(nullable = false, length = 150)
    private String nombreCompleto;

    @Column(nullable = false, length = 100)
    private String correo;

    @Column(length = 20)
    private String celular;

    @Column(length = 100)
    private String programaInteres;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    @CreationTimestamp
    private LocalDateTime fechaEnvio;

    @Builder.Default
    private Boolean estadoAtencion = false;
}