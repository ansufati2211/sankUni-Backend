package com.snkuni.sankuni.services;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class ReporteService {

    private final JdbcTemplate jdbcTemplate;

    // Conecta con la vista: vw_reporte_ingresos
    public List<Map<String, Object>> obtenerReporteFinanciero() {
        String sql = "SELECT * FROM vw_reporte_ingresos";
        return jdbcTemplate.queryForList(sql);
    }

    // Conecta con la vista: vw_historial_academico
    public List<Map<String, Object>> obtenerHistorialAcademico(Long idAlumno) {
        String sql = "SELECT * FROM vw_historial_academico WHERE id_alumno = ?";
        return jdbcTemplate.queryForList(sql, idAlumno);
    }

    // Conecta con la vista: vw_semaforo_notas
    public List<Map<String, Object>> obtenerSemaforoNotasDeDocente(Long idDocente) {
        // Asumiendo que quieres filtrar el semáforo por docente activo
        String sql = "SELECT * FROM vw_semaforo_notas WHERE docente = (SELECT u.nombre_completo FROM docentes d JOIN usuarios u ON d.usuario_id = u.id_usuario WHERE d.id_docente = ?)";
        return jdbcTemplate.queryForList(sql, idDocente);
    }

    // Conecta con la vista: vw_horario_alumno
    public List<Map<String, Object>> obtenerHorarioAlumno(Long idAlumno) {
        String sql = "SELECT * FROM vw_horario_alumno WHERE id_alumno = ?";
        return jdbcTemplate.queryForList(sql, idAlumno);
    }
}