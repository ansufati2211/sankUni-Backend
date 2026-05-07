package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.DashboardAdminDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> obtenerReporteFinanciero() {
        return jdbcTemplate.queryForList("SELECT * FROM vw_reporte_ingresos");
    }

    public List<Map<String, Object>> obtenerHistorialAcademico(Long idAlumno) {
        return jdbcTemplate.queryForList("SELECT * FROM vw_historial_academico WHERE id_alumno = ?", idAlumno);
    }

    public List<Map<String, Object>> obtenerSemaforoNotasDeDocente(Long idDocente) {
        return jdbcTemplate.queryForList("SELECT * FROM vw_semaforo_notas WHERE docente = (SELECT u.nombres || ' ' || u.apellidos FROM docentes d JOIN usuarios u ON d.usuario_id = u.id_usuario WHERE d.id_docente = ?)", idDocente);
    }

    public List<Map<String, Object>> obtenerHorarioAlumno(Long idAlumno) {
        return jdbcTemplate.queryForList("SELECT * FROM vw_horario_alumno WHERE id_alumno = ?", idAlumno);
    }

    public List<Map<String, Object>> obtenerHorarioDocente(Long idDocente) {
        return jdbcTemplate.queryForList("SELECT * FROM vw_horario_docente WHERE id_docente = ?", idDocente);
    }

    public List<Map<String, Object>> obtenerRendimientoGlobal() {
        return jdbcTemplate.queryForList("SELECT * FROM vw_rendimiento_alumno");
    }
    public List<Map<String, Object>> obtenerSemaforoGlobal() {
        return jdbcTemplate.queryForList("SELECT * FROM vw_semaforo_notas");
    }

    // 🚀 AHORA SÍ: Consulta las 4 métricas reales para el Administrador
    public DashboardAdminDTO obtenerDashboardAdmin() {
        Integer totalAlumnos = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM alumnos", Integer.class);
        Integer totalDocentes = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM docentes", Integer.class);
        // Suma de pagos del mes actual
        BigDecimal ingresosMes = jdbcTemplate.queryForObject(
            "SELECT COALESCE(SUM(monto_pagado), 0) FROM pagos WHERE EXTRACT(MONTH FROM fecha_pago) = EXTRACT(MONTH FROM CURRENT_DATE)", 
            BigDecimal.class);
        Integer pendientes = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM solicitudes WHERE estado = 'PENDIENTE'", Integer.class);

        return DashboardAdminDTO.builder()
                .totalAlumnos(totalAlumnos != null ? totalAlumnos : 0)
                .totalDocentes(totalDocentes != null ? totalDocentes : 0)
                .ingresosMes(ingresosMes != null ? ingresosMes : BigDecimal.ZERO)
                .solicitudesPendientes(pendientes != null ? pendientes : 0)
                .build();
    }

    public List<Map<String, Object>> obtenerEvolucionMatriculas() {
        // Trae los últimos 7 meses con matrículas reales
        String sql = "SELECT TO_CHAR(fecha_matricula, 'YYYY-MM') as mes, COUNT(*) as total " +
                     "FROM matriculas GROUP BY TO_CHAR(fecha_matricula, 'YYYY-MM') ORDER BY mes ASC LIMIT 7";
        return jdbcTemplate.queryForList(sql);
    }
}