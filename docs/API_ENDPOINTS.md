# Endpoints REST — sankUni Backend

Nota de seguridad: ningún controller usa `@PreAuthorize`/`@Secured`/`@RolesAllowed`. La autorización por rol no está implementada a nivel de endpoint todavía — solo hay distinción público/autenticado vía JWT en `SecurityConfig`. Tenerlo en cuenta al exponer los endpoints nuevos (ver sección final).

## 1. Endpoints existentes

### AlertaAcademicaController (`/api/v1/alertas`)
| Método | Ruta | Descripción | Body/Params | Devuelve |
|---|---|---|---|---|
| GET | `/pendientes` | Lista alertas académicas pendientes | — | `List<AlertaAcademicaDTO>` |
| GET | `/docente/{idDocente}` | Alertas de un docente | `idDocente` (path) | `List<AlertaAcademicaDTO>` |
| PUT | `/{id}/resolver` | Marca una alerta como resuelta | `id` (path) | `ApiResponseDTO` |

### AlumnoController (`/api/v1/alumnos`)
| Método | Ruta | Descripción | Body/Params | Devuelve |
|---|---|---|---|---|
| GET | `/` | Lista todos los alumnos | — | `List<AlumnoDTO>` |
| GET | `/{id}` | Alumno por id | `id` (path) | `AlumnoDTO` |
| GET | `/perfil/{usuarioId}` | Perfil de alumno por id de usuario | `usuarioId` (path) | `AlumnoDTO` |
| GET | `/dni/{dni}` | Busca alumno por DNI (Coordinador) | `dni` (path) | `AlumnoDTO` |
| POST | `/registro-manual` | Registra alumno desde postulante | Body: `PostulanteDTO` | `AlumnoDTO` (201) |

### AsistenciaController (`/api/v1/asistencias`)
| Método | Ruta | Descripción | Body/Params | Devuelve |
|---|---|---|---|---|
| POST | `/registrar` | Registra asistencia de una sección | Body: `AsistenciaRequestDTO` | `ApiResponseDTO` (201) |
| GET | `/seccion/{idSeccion}/fecha/{fecha}` | Asistencia de sección en una fecha | `idSeccion`, `fecha` (path) | `List<AsistenciaDTO>` |
| GET | `/alumno/{alumnoId}` | Asistencias de un alumno | `alumnoId` (path) | `List<AsistenciaDTO>` |

### AuthController (`/api/v1/auth`)
| Método | Ruta | Descripción | Body/Params | Devuelve |
|---|---|---|---|---|
| POST | `/login` | Autentica y genera JWT | Body: `AuthRequestDTO` | `AuthResponseDTO` |

### CarreraController (`/api/v1/carreras`)
| Método | Ruta | Descripción | Body/Params | Devuelve |
|---|---|---|---|---|
| GET | `/` | Lista carreras | — | `List<CarreraDTO>` |
| POST | `/` | Crea carrera | Body: `CarreraDTO` | `CarreraDTO` (201) |
| PUT | `/{id}` | Edita carrera | `id` (path), Body: `CarreraDTO` | `CarreraDTO` |

### CuotaAlumnoController (`/api/v1/cuotas`)
| Método | Ruta | Descripción | Body/Params | Devuelve |
|---|---|---|---|---|
| GET | `/alumno/{idAlumno}` | Cuotas de un alumno | `idAlumno` (path) | `List<CuotaAlumnoDTO>` |
| GET | `/todas` | Todas las cuotas del sistema | — | `List<CuotaAlumnoDTO>` |

### CursoController (`/api/v1/cursos`)
| Método | Ruta | Descripción | Body/Params | Devuelve |
|---|---|---|---|---|
| POST | `/` | Crea curso | Body: `CursoDTO` | `CursoDTO` (201) |
| GET | `/` | Lista cursos | — | `List<CursoDTO>` |

### DocenteController (`/api/v1/docentes`)
| Método | Ruta | Descripción | Body/Params | Devuelve |
|---|---|---|---|---|
| POST | `/registro` | Registra docente | Body: `Map<String,String>` (dni, nombres, apellidos, correo, especialidad) | `DocenteDTO` (201) |
| GET | `/` | Lista docentes | — | `List<DocenteDTO>` |
| GET | `/{id}` | Docente por id | `id` (path) | `DocenteDTO` |
| GET | `/perfil/{usuarioId}` | Perfil de docente por id de usuario | `usuarioId` (path) | `DocenteDTO` |

### EvaluacionController (`/api/v1/evaluaciones`)
| Método | Ruta | Descripción | Body/Params | Devuelve |
|---|---|---|---|---|
| POST | `/` | Crea evaluación (opcionalmente agrupada en un módulo) | Body: `EvaluacionDTO` (seccionId, idModulo opcional, nombreExamen, pesoPorcentaje, fechaExamen) | `EvaluacionDTO` (201) |
| GET | `/seccion/{id}` | Evaluaciones de una sección | `id` (path) | `List<EvaluacionDTO>` |
| GET | `/modulo/{idModulo}` | Evaluaciones agrupadas por módulo | `idModulo` (path) | `List<EvaluacionDTO>` |
| DELETE | `/{id}` | Elimina una evaluación. Falla con 400 si ya tiene notas o entregas registradas (protege esos datos) | `id` (path) | 204 |

### MaterialClaseController (`/api/v1/materiales`)
| Método | Ruta | Descripción | Body/Params | Devuelve |
|---|---|---|---|---|
| POST | `/` | Docente sube un archivo real (multipart), opcionalmente agrupado en un módulo | `multipart/form-data`: idSeccion, titulo, idModulo (opcional), archivo | `MaterialClaseDTO` (201) |
| GET | `/seccion/{idSeccion}` | Materiales de una sección | `idSeccion` (path) | `List<MaterialClaseDTO>` |
| GET | `/modulo/{idModulo}` | Materiales agrupados por módulo | `idModulo` (path) | `List<MaterialClaseDTO>` |
| GET | `/{id}/archivo` | Descarga el binario del material vía `StorageService` | `id` (path) | binario |
| DELETE | `/{id}` | Elimina el material y borra su archivo físico del disco | `id` (path) | 204 |

### ModuloCurso (`/api/v1/modulos`)
| Método | Ruta | Descripción | Body/Params | Devuelve |
|---|---|---|---|---|
| POST | `/` | Coordinador crea un módulo (unidad/semana) para un curso | Body: `ModuloCursoDTO` (idCurso, titulo, descripcion, orden) | `ModuloCursoDTO` (201) |
| GET | `/curso/{idCurso}` | Lista módulos de un curso, ordenados | `idCurso` (path) | `List<ModuloCursoDTO>` |
| PUT | `/{id}` | Edita título/descripción/orden | `id` (path), Body: `ModuloCursoDTO` | `ModuloCursoDTO` |
| DELETE | `/{id}` | Elimina un módulo. Su material y evaluaciones NO se borran: quedan sin agrupar (`idModulo=null`), conservando archivos, notas y entregas. Por eso nunca falla por tener contenido cargado. | `id` (path) | 204 |

### EntregaEvaluacion (`/api/v1/entregas`)
| Método | Ruta | Descripción | Body/Params | Devuelve |
|---|---|---|---|---|
| POST | `/` | Alumno entrega un trabajo (una sola entrega por evaluación+alumno; reintento requiere que el docente borre la anterior) | `multipart/form-data`: idEvaluacion, idAlumno, archivo | `EntregaEvaluacionDTO` (201; 400 si ya existe entrega) |
| GET | `/evaluacion/{idEvaluacion}` | Lista entregas de una evaluación (para calificar) | `idEvaluacion` (path) | `List<EntregaEvaluacionDTO>` |
| GET | `/alumno/{alumnoId}` | Lista entregas de un alumno | `alumnoId` (path) | `List<EntregaEvaluacionDTO>` |
| GET | `/{id}/archivo` | Descarga el archivo entregado | `id` (path) | binario |

### AnuncioSeccion (`/api/v1/anuncios`)
| Método | Ruta | Descripción | Body/Params | Devuelve |
|---|---|---|---|---|
| POST | `/` | Docente publica un anuncio en su sección | Body: `AnuncioSeccionDTO` (idSeccion, titulo, contenido) | `AnuncioSeccionDTO` (201) |
| GET | `/seccion/{idSeccion}` | Anuncios de una sección, por fecha desc | `idSeccion` (path) | `List<AnuncioSeccionDTO>` |
| GET | `/alumno/{alumnoId}` | Anuncios agregados de todas las secciones en que el alumno tiene matrícula | `alumnoId` (path) | `List<AnuncioSeccionDTO>` |
| DELETE | `/{id}` | Elimina un anuncio | `id` (path) | 204 |

### MatriculaController (`/api/v1/matriculas`)
| Método | Ruta | Descripción | Body/Params | Devuelve |
|---|---|---|---|---|
| POST | `/automatricula` | Procesa automatrícula | Body: `MatriculaRequestDTO` | `ApiResponseDTO` (201) |
| GET | `/seccion/{idSeccion}` | Alumnos matriculados en una sección | `idSeccion` (path) | `List<MatriculaDTO>` |

### MensajeContactoController (`/api/v1/mensajes`)
| Método | Ruta | Descripción | Body/Params | Devuelve |
|---|---|---|---|---|
| POST | `/` | Guarda mensaje de contacto web | Body: `MensajeContactoDTO` | `MensajeContactoDTO` (201) |
| GET | `/pendientes` | Mensajes pendientes | — | `List<MensajeContactoDTO>` |

### NotaEvaluacionController (`/api/v1/notas`)
| Método | Ruta | Descripción | Body/Params | Devuelve |
|---|---|---|---|---|
| POST | `/registrar` | Registra nota y feedback de una evaluación | Body: `NotaEvaluacionDTO` (suma `comentario` opcional) | `NotaEvaluacionDTO` |
| GET | `/evaluacion/{idEvaluacion}` | Notas de una evaluación | `idEvaluacion` (path) | `List<NotaEvaluacionDTO>` |
| GET | `/alumno/{alumnoId}` | Notas de un alumno | `alumnoId` (path) | `List<NotaEvaluacionDTO>` |

### NotificacionController (`/api/v1/notificaciones`)
| Método | Ruta | Descripción | Body/Params | Devuelve |
|---|---|---|---|---|
| GET | `/admin` | Notificaciones para Administrador | — | `List<NotificacionDTO>` |
| GET | `/coordinador` | Notificaciones para Coordinador | — | `List<NotificacionDTO>` |

### PagoController (`/api/v1/pagos`)
| Método | Ruta | Descripción | Body/Params | Devuelve |
|---|---|---|---|---|
| GET | `/` | Lista todos los pagos | — | `List<PagoDTO>` |
| GET | `/cuota/{idCuota}` | Pagos de una cuota | `idCuota` (path) | `List<PagoDTO>` |
| POST | `/pagar` | Procesa pago de una cuota | Body: `Map<String,Object>` (idCuota, monto, metodoPago) | `PagoDTO` |

### PostulanteController (`/api/v1/postulantes`)
| Método | Ruta | Descripción | Body/Params | Devuelve |
|---|---|---|---|---|
| POST | `/` | Registra postulante | Body: `PostulanteDTO` | `PostulanteDTO` (201) |
| GET | `/pendientes` | Postulantes pendientes de aprobación | — | `List<PostulanteDTO>` |
| POST | `/{id}/aprobar` | Aprueba postulante (lo convierte en alumno) | `id` (path) | `String` |

### ReporteController (`/api/v1/reportes`)
| Método | Ruta | Descripción | Body/Params | Devuelve |
|---|---|---|---|---|
| GET | `/financiero` | Reporte financiero general | — | `List<Map<String,Object>>` |
| GET | `/historial/{alumnoId}` | Historial académico de un alumno | `alumnoId` (path) | `List<Map<String,Object>>` |
| GET | `/semaforo/{docenteId}` | Semáforo de notas de un docente | `docenteId` (path) | `List<Map<String,Object>>` |
| GET | `/horario/{alumnoId}` | Horario de un alumno | `alumnoId` (path) | `List<Map<String,Object>>` |
| GET | `/horario-docente/{docenteId}` | Horario de un docente | `docenteId` (path) | `List<Map<String,Object>>` |
| GET | `/dashboard-admin` | Dashboard resumen para admin | — | `DashboardAdminDTO` |
| GET | `/rendimiento` | Rendimiento académico global | — | `List<Map<String,Object>>` |
| GET | `/matriculas-chart` | Evolución de matrículas (gráfico) | — | `List<Map<String,Object>>` |
| GET | `/semaforo-global` | Semáforo de notas global | — | `List<Map<String,Object>>` |

### SeccionController (`/api/v1/secciones`)
| Método | Ruta | Descripción | Body/Params | Devuelve |
|---|---|---|---|---|
| GET | `/` | Lista todas las secciones | — | `List<SeccionDTO>` |
| GET | `/ciclo/{ciclo}` | Secciones de un ciclo académico | `ciclo` (path) | `List<SeccionDTO>` |
| GET | `/docente/{idDocente}` | Secciones de un docente | `idDocente` (path) | `List<SeccionDTO>` |
| GET | `/docente/{idDocente}/dia/{diaSemana}` | Secciones de un docente en un día | `idDocente`, `diaSemana` (path) | `List<SeccionDTO>` |
| POST | `/` | Crea sección | Body: `SeccionDTO` | `SeccionDTO` (201) |
| PUT | `/{id}` | Edita sección | `id` (path), Body: `SeccionDTO` | `SeccionDTO` |
| DELETE | `/{id}` | Elimina sección | `id` (path) | 204 |

### SolicitudController (`/api/v1/solicitudes`)
| Método | Ruta | Descripción | Body/Params | Devuelve |
|---|---|---|---|---|
| POST | `/` | Crea solicitud (trámite) | Body: `SolicitudRequestDTO` | `SolicitudDTO` |
| GET | `/mis-solicitudes/{idUsuario}` | Solicitudes de un usuario | `idUsuario` (path) | `List<SolicitudDTO>` |
| GET | `/pendientes` | Solicitudes pendientes | — | `List<SolicitudDTO>` |
| PUT | `/{id}/responder` | Aprueba/rechaza solicitud | `id` (path), Body: `Map<String,String>` (estado, observacion) | `SolicitudDTO` |

### UsuarioController (`/api/v1/usuarios`)
| Método | Ruta | Descripción | Body/Params | Devuelve |
|---|---|---|---|---|
| GET | `/` | Lista usuarios | — | `List<UsuarioDTO>` |
| POST | `/` | Crea usuario | Body: `UsuarioDTO` | `UsuarioDTO` |
| PUT | `/{id}` | Actualiza usuario | `id` (path), Body: `UsuarioDTO` | `UsuarioDTO` |
| DELETE | `/{id}` | Elimina usuario | `id` (path) | 204 |
| PUT | `/{id}/password` | Actualiza contraseña | `id` (path), Body: `Map<String,String>` (nuevaPassword) | 200 |
| POST | `/{id}/enviar-codigo` | Envía código de verificación | `id` (path) | 200 |
| POST | `/{id}/verificar-codigo` | Verifica código enviado | `id` (path), Body: `Map<String,String>` (codigo) | 200 |

---

## 2. Pendiente transversal

- **Autorización por rol**: ningún endpoint (nuevo ni preexistente) tiene `@PreAuthorize`. `SecurityConfig` solo distingue rutas públicas vs autenticadas — falta control granular por rol (p.ej. que solo un Coordinador pueda crear `ModuloCurso`, o solo el Docente dueño de la sección pueda publicar `AnuncioSeccion`). Deferred junto con el resto del backend.
- **Schema SQL manual**: `spring.jpa.hibernate.ddl-auto=validate` — toda tabla/columna nueva requiere su `ALTER TABLE`/`CREATE TABLE` en `sql/schema.sql` (ya aplicado para `modulo_curso`, `entrega_evaluacion`, `anuncio_seccion` y las columnas `modulo_id`/`comentario`).
- **Almacenamiento local**: los archivos se guardan en `./uploads/{materiales,entregas}/` (fuera del control de versiones, ver `sankuni.storage.location` en `application.properties`). `StorageService` es la única capa que sabe dónde viven los archivos — migrar a S3/Cloudinary más adelante implica solo una nueva implementación de esa interfaz.
