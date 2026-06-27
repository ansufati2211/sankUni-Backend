-- =============================================================================
-- SANKU INSTITUTO — Schema completo de base de datos PostgreSQL
-- Base de datos: dvSnkuni
-- Generado a partir de las entidades JPA del proyecto
--
-- USO:
--   1. Crea la base de datos:  CREATE DATABASE "dvSnkuni";
--   2. Conéctate a ella:       \c dvSnkuni
--   3. Ejecuta este script:    \i schema.sql
--
-- NOTA: Si la BD ya existe con datos, ejecuta primero DROP SCHEMA PUBLIC CASCADE;
-- =============================================================================

-- =============================================================================
-- 1. USUARIOS
-- Tabla base de autenticación. Todos los roles (alumno, docente, etc.) tienen
-- un usuario asociado.
-- =============================================================================
CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario      BIGSERIAL PRIMARY KEY,
    dni             VARCHAR(15)  NOT NULL UNIQUE,
    nombres         VARCHAR(100) NOT NULL,
    apellidos       VARCHAR(100) NOT NULL,
    email           VARCHAR(100) NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    rol             VARCHAR(20)  NOT NULL
                        CHECK (rol IN ('ADMINISTRADOR', 'COORDINADOR', 'DOCENTE', 'ALUMNO')),
    creado_en       TIMESTAMP DEFAULT NOW(),
    actualizado_en  TIMESTAMP DEFAULT NOW()
);

-- =============================================================================
-- 2. CARRERAS
-- =============================================================================
CREATE TABLE IF NOT EXISTS carreras (
    id_carrera          BIGSERIAL PRIMARY KEY,
    tipo                VARCHAR(20) CHECK (tipo IN ('CARRERA', 'CURSO_CORTO')),
    nombre              VARCHAR(100) NOT NULL UNIQUE,
    descripcion         TEXT,
    perfil_academico    TEXT,
    mercado_laboral     TEXT,
    beneficios          TEXT,
    requisitos          TEXT,
    estado              BOOLEAN DEFAULT TRUE
);

-- =============================================================================
-- 3. ALUMNOS
-- =============================================================================
CREATE TABLE IF NOT EXISTS alumnos (
    id_alumno           BIGSERIAL PRIMARY KEY,
    usuario_id          BIGINT NOT NULL UNIQUE REFERENCES usuarios(id_usuario),
    carrera_id          BIGINT NOT NULL REFERENCES carreras(id_carrera),
    estado              VARCHAR(20) NOT NULL DEFAULT 'ACTIVO'
                            CHECK (estado IN ('ACTIVO', 'SUSPENDIDO', 'EGRESADO')),
    fecha_ingreso       DATE DEFAULT CURRENT_DATE,
    promedio_historico  NUMERIC(4, 2) DEFAULT 0.00
);

-- =============================================================================
-- 4. DOCENTES
-- =============================================================================
CREATE TABLE IF NOT EXISTS docentes (
    id_docente      BIGSERIAL PRIMARY KEY,
    usuario_id      BIGINT NOT NULL UNIQUE REFERENCES usuarios(id_usuario),
    estado          VARCHAR(20) NOT NULL DEFAULT 'ACTIVO'
                        CHECK (estado IN ('ACTIVO', 'INACTIVO', 'DESPEDIDO')),
    especialidad    VARCHAR(100)
);

-- =============================================================================
-- 5. CURSOS
-- =============================================================================
CREATE TABLE IF NOT EXISTS cursos (
    id_curso                BIGSERIAL PRIMARY KEY,
    carrera_id              BIGINT NOT NULL REFERENCES carreras(id_carrera),
    nombre                  VARCHAR(100) NOT NULL,
    creditos                INTEGER NOT NULL DEFAULT 3,
    temario_url             TEXT,
    descripcion_informativa TEXT
);

-- =============================================================================
-- 6. SECCIONES
-- Una sección es la apertura de un curso para un ciclo con un docente y horario.
-- =============================================================================
CREATE TABLE IF NOT EXISTS secciones (
    id_seccion      BIGSERIAL PRIMARY KEY,
    curso_id        BIGINT NOT NULL REFERENCES cursos(id_curso),
    docente_id      BIGINT NOT NULL REFERENCES docentes(id_docente),
    ciclo_academico VARCHAR(20) NOT NULL,
    modalidad       VARCHAR(20) NOT NULL DEFAULT 'PRESENCIAL'
                        CHECK (modalidad IN ('PRESENCIAL', 'VIRTUAL', 'HIBRIDO')),
    dia_semana      INTEGER NOT NULL,
    hora_inicio     TIME NOT NULL,
    hora_fin        TIME NOT NULL
);

-- =============================================================================
-- 7. MATRÍCULAS
-- Alumno inscrito en una sección. Constraint único por alumno+sección.
-- =============================================================================
CREATE TABLE IF NOT EXISTS matriculas (
    id_matricula    BIGSERIAL PRIMARY KEY,
    alumno_id       BIGINT NOT NULL REFERENCES alumnos(id_alumno),
    seccion_id      BIGINT NOT NULL REFERENCES secciones(id_seccion),
    nota_final      NUMERIC(4, 2),
    fecha_matricula TIMESTAMP DEFAULT NOW(),
    CONSTRAINT uq_matricula UNIQUE (alumno_id, seccion_id)
);

-- =============================================================================
-- 8. EVALUACIONES
-- Exámenes/prácticas definidos por el docente para una sección.
-- =============================================================================
CREATE TABLE IF NOT EXISTS evaluaciones (
    id_evaluacion           BIGSERIAL PRIMARY KEY,
    seccion_id              BIGINT NOT NULL REFERENCES secciones(id_seccion),
    nombre_examen           VARCHAR(100) NOT NULL,
    peso_porcentaje         INTEGER,
    fecha_examen            DATE NOT NULL,
    fecha_publicacion_notas TIMESTAMP
);

-- =============================================================================
-- 9. NOTAS POR EVALUACIÓN
-- Nota individual de cada alumno en cada evaluación.
-- Constraint único: un alumno solo tiene una nota por evaluación.
-- =============================================================================
CREATE TABLE IF NOT EXISTS notas_evaluacion (
    id_nota         BIGSERIAL PRIMARY KEY,
    evaluacion_id   BIGINT NOT NULL REFERENCES evaluaciones(id_evaluacion),
    alumno_id       BIGINT NOT NULL REFERENCES alumnos(id_alumno),
    nota            NUMERIC(4, 2),
    fecha_registro  TIMESTAMP DEFAULT NOW(),
    CONSTRAINT uq_nota_evaluacion UNIQUE (evaluacion_id, alumno_id)
);

-- =============================================================================
-- 10. ASISTENCIA
-- Registro diario de asistencia por alumno y sección.
-- El stored procedure sp_registrar_asistencia hace UPSERT sobre esta tabla.
-- =============================================================================
CREATE TABLE IF NOT EXISTS asistencia (
    id_asistencia   BIGSERIAL PRIMARY KEY,
    seccion_id      BIGINT NOT NULL REFERENCES secciones(id_seccion),
    alumno_id       BIGINT NOT NULL REFERENCES alumnos(id_alumno),
    fecha           DATE NOT NULL DEFAULT CURRENT_DATE,
    presente        BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT uq_asistencia UNIQUE (seccion_id, alumno_id, fecha)
);

-- =============================================================================
-- 11. MATERIALES DE CLASE
-- Archivos/enlaces publicados por el docente en una sección.
-- =============================================================================
CREATE TABLE IF NOT EXISTS materiales_clase (
    id_material     BIGSERIAL PRIMARY KEY,
    seccion_id      BIGINT NOT NULL REFERENCES secciones(id_seccion),
    titulo          VARCHAR(150) NOT NULL,
    archivo_url     TEXT NOT NULL,
    fecha_subida    TIMESTAMP DEFAULT NOW()
);

-- =============================================================================
-- 12. CUOTAS DEL ALUMNO
-- Cuotas de pago generadas para cada alumno por ciclo/mes.
-- =============================================================================
CREATE TABLE IF NOT EXISTS cuotas_alumno (
    id_cuota            BIGSERIAL PRIMARY KEY,
    alumno_id           BIGINT NOT NULL REFERENCES alumnos(id_alumno),
    ciclo_academico     VARCHAR(20),
    mes_correspondiente VARCHAR(50) NOT NULL,
    monto_total         NUMERIC(10, 2) NOT NULL,
    estado              VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE'
                            CHECK (estado IN ('PENDIENTE', 'PAGADO', 'VENCIDO')),
    fecha_vencimiento   DATE NOT NULL
);

-- =============================================================================
-- 13. PAGOS
-- Registro de pagos realizados sobre una cuota.
-- =============================================================================
CREATE TABLE IF NOT EXISTS pagos (
    id_pago         BIGSERIAL PRIMARY KEY,
    cuota_id        BIGINT NOT NULL REFERENCES cuotas_alumno(id_cuota),
    monto_pagado    NUMERIC(10, 2) NOT NULL,
    metodo_pago     VARCHAR(50) DEFAULT 'TARJETA',
    fecha_pago      TIMESTAMP DEFAULT NOW()
);

-- =============================================================================
-- 14. SOLICITUDES (Buzón SAE)
-- Solicitudes enviadas por alumnos al coordinador/admin.
-- =============================================================================
CREATE TABLE IF NOT EXISTS solicitudes (
    id_solicitud            BIGSERIAL PRIMARY KEY,
    emisor_id               BIGINT NOT NULL REFERENCES usuarios(id_usuario),
    tipo                    VARCHAR(30) NOT NULL
                                CHECK (tipo IN ('REPROGRAMACION', 'VERIFICACION_NOTA', 'OTROS')),
    seccion_id              BIGINT REFERENCES secciones(id_seccion),
    descripcion             TEXT NOT NULL,
    estado                  VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE'
                                CHECK (estado IN ('PENDIENTE', 'APROBADO', 'RECHAZADO')),
    fecha_solicitud         TIMESTAMP DEFAULT NOW(),
    fecha_respuesta         TIMESTAMP,
    observacion_coordinador TEXT
);

-- =============================================================================
-- 15. ALERTAS ACADÉMICAS
-- Alertas automáticas generadas para docentes (notas atrasadas, asistencia).
-- =============================================================================
CREATE TABLE IF NOT EXISTS alertas_academicas (
    id_alerta       BIGSERIAL PRIMARY KEY,
    tipo            VARCHAR(30) NOT NULL
                        CHECK (tipo IN ('NOTAS_ATRASADAS', 'ASISTENCIA_NO_REGISTRADA')),
    seccion_id      BIGINT NOT NULL REFERENCES secciones(id_seccion),
    docente_id      BIGINT NOT NULL REFERENCES docentes(id_docente),
    mensaje         TEXT NOT NULL,
    resuelta        BOOLEAN DEFAULT FALSE,
    fecha_creacion  TIMESTAMP DEFAULT NOW()
);

-- =============================================================================
-- 16. POSTULANTES
-- Personas que se postulan a través del formulario de admisión.
-- =============================================================================
CREATE TABLE IF NOT EXISTS postulantes (
    id_postulante       BIGSERIAL PRIMARY KEY,
    dni                 VARCHAR(15)  NOT NULL UNIQUE,
    nombres             VARCHAR(100) NOT NULL,
    apellidos           VARCHAR(100) NOT NULL,
    correo              VARCHAR(100) NOT NULL,
    carrera_id          BIGINT REFERENCES carreras(id_carrera),
    sede                VARCHAR(50),
    turno               VARCHAR(20),
    estado              VARCHAR(20) DEFAULT 'EN_REVISION'
                            CHECK (estado IN ('EN_REVISION', 'APROBADO', 'RECHAZADO')),
    fecha_postulacion   TIMESTAMP DEFAULT NOW()
);

-- =============================================================================
-- 17. MENSAJES DE CONTACTO
-- Mensajes enviados desde el formulario público de contacto.
-- =============================================================================
CREATE TABLE IF NOT EXISTS mensajes_contacto (
    id_mensaje          BIGSERIAL PRIMARY KEY,
    nombre_completo     VARCHAR(150) NOT NULL,
    correo              VARCHAR(100) NOT NULL,
    celular             VARCHAR(20),
    programa_interes    VARCHAR(100),
    mensaje             TEXT NOT NULL,
    fecha_envio         TIMESTAMP DEFAULT NOW(),
    estado_atencion     BOOLEAN DEFAULT FALSE
);

-- =============================================================================
-- STORED PROCEDURE: sp_registrar_asistencia
-- Hace UPSERT de asistencia para un alumno en una sección en la fecha actual.
-- Si ya existe un registro para ese día, lo actualiza; si no, lo inserta.
-- =============================================================================
CREATE OR REPLACE PROCEDURE sp_registrar_asistencia(
    p_seccion_id    BIGINT,
    p_alumno_id     BIGINT,
    p_presente      BOOLEAN
)
LANGUAGE plpgsql AS $$
BEGIN
    INSERT INTO asistencia (seccion_id, alumno_id, fecha, presente)
    VALUES (p_seccion_id, p_alumno_id, CURRENT_DATE, p_presente)
    ON CONFLICT ON CONSTRAINT uq_asistencia
    DO UPDATE SET presente = EXCLUDED.presente;
END;
$$;

-- =============================================================================
-- ÍNDICES recomendados para las consultas más frecuentes
-- =============================================================================
CREATE INDEX IF NOT EXISTS idx_secciones_ciclo      ON secciones(ciclo_academico);
CREATE INDEX IF NOT EXISTS idx_secciones_docente     ON secciones(docente_id);
CREATE INDEX IF NOT EXISTS idx_matriculas_seccion    ON matriculas(seccion_id);
CREATE INDEX IF NOT EXISTS idx_matriculas_alumno     ON matriculas(alumno_id);
CREATE INDEX IF NOT EXISTS idx_notas_evaluacion      ON notas_evaluacion(evaluacion_id);
CREATE INDEX IF NOT EXISTS idx_notas_alumno          ON notas_evaluacion(alumno_id);
CREATE INDEX IF NOT EXISTS idx_asistencia_alumno     ON asistencia(alumno_id);
CREATE INDEX IF NOT EXISTS idx_asistencia_seccion_f  ON asistencia(seccion_id, fecha);
CREATE INDEX IF NOT EXISTS idx_cuotas_alumno         ON cuotas_alumno(alumno_id);
CREATE INDEX IF NOT EXISTS idx_alertas_docente       ON alertas_academicas(docente_id);
CREATE INDEX IF NOT EXISTS idx_solicitudes_emisor    ON solicitudes(emisor_id);
