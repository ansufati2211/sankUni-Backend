package com.snkuni.sankuni.services;

import com.snkuni.sankuni.models.AlertaAcademica;
import com.snkuni.sankuni.models.Evaluacion;
import com.snkuni.sankuni.models.Seccion;
import com.snkuni.sankuni.models.enums.TipoAlerta;
import com.snkuni.sankuni.repositories.AlertaAcademicaRepository;
import com.snkuni.sankuni.repositories.AsistenciaRepository;
import com.snkuni.sankuni.repositories.EvaluacionRepository;
import com.snkuni.sankuni.repositories.SeccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertaAutomaticaScheduler {

    private final SeccionRepository seccionRepository;
    private final AsistenciaRepository asistenciaRepository;
    private final EvaluacionRepository evaluacionRepository;
    private final AlertaAcademicaRepository alertaRepository;

    // 1. REVISAR ASISTENCIA: Se ejecuta CADA MINUTO exactamente
    @Scheduled(cron = "0 * * * * *") 
    @Transactional
    public void verificarAsistenciasDiarias() {
        int diaActual = LocalDate.now().getDayOfWeek().getValue();
        LocalTime ahora = LocalTime.now();

        List<Seccion> clasesDeHoy = seccionRepository.findAll().stream()
                .filter(s -> s.getDiaSemana() == diaActual).toList();

        for (Seccion seccion : clasesDeHoy) {
            // ALERTA ASISTENCIA: Si la clase terminó o faltan 15 mins para terminar
            if (ahora.isAfter(seccion.getHoraFin().minusMinutes(15))) {
                
                boolean tomoAsistencia = !asistenciaRepository
                        .findBySeccion_IdSeccionAndFecha(seccion.getIdSeccion(), LocalDate.now())
                        .isEmpty();

                if (!tomoAsistencia) {
                    crearAlerta(seccion, TipoAlerta.ASISTENCIA_NO_REGISTRADA, 
                            "La clase finaliza a las " + seccion.getHoraFin() + ". Es necesario que registre la asistencia de hoy.");
                }
            }
        }
    }

    // 2. REVISAR NOTAS: Se ejecuta todos los días a las 8:00 AM
    @Scheduled(cron = "0 0 8 * * *")
    @Transactional
    public void verificarNotasPendientes() {
        List<Evaluacion> evaluaciones = evaluacionRepository.findAll();
        LocalDate hoy = LocalDate.now();

        for (Evaluacion eval : evaluaciones) {
            // ALERTA NOTAS: Si pasaron 3 días desde el examen y no se han subido notas
            if (eval.getFechaExamen() != null && hoy.isAfter(eval.getFechaExamen().plusDays(3))) {
                crearAlerta(eval.getSeccion(), TipoAlerta.NOTAS_ATRASADAS, 
                        "Recordatorio: Debe subir las calificaciones de la evaluación '" + eval.getNombreExamen() + "' que se tomó el " + eval.getFechaExamen() + ".");
            }
        }
    }

    private void crearAlerta(Seccion seccion, TipoAlerta tipo, String mensaje) {
        boolean existe = alertaRepository.findByDocente_IdDocenteAndResueltaFalse(seccion.getDocente().getIdDocente())
                .stream().anyMatch(a -> a.getTipo() == tipo && a.getSeccion().getIdSeccion().equals(seccion.getIdSeccion()));

        if (!existe) {
            AlertaAcademica alerta = AlertaAcademica.builder()
                    .tipo(tipo)
                    .seccion(seccion)
                    .docente(seccion.getDocente())
                    .mensaje(mensaje)
                    .resuelta(false)
                    .build();
            alertaRepository.save(alerta);
        }
    }
}