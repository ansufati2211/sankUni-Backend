package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.UsuarioDTO;
import com.snkuni.sankuni.models.Alumno;
import com.snkuni.sankuni.models.Docente;
import com.snkuni.sankuni.models.Usuario;
import com.snkuni.sankuni.models.enums.StudentStatus;
import com.snkuni.sankuni.models.enums.TeacherStatus;
import com.snkuni.sankuni.models.enums.UserRole;
import com.snkuni.sankuni.repositories.AlumnoRepository;
import com.snkuni.sankuni.repositories.DocenteRepository;
import com.snkuni.sankuni.repositories.UsuarioRepository;
import com.snkuni.sankuni.exceptions.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final DocenteRepository docenteRepository;
    private final AlumnoRepository alumnoRepository;
    private final PasswordEncoder passwordEncoder; 

    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarTodosLosUsuarios() {
        return usuarioRepository.findAll().stream().map(this::mapearADto).toList(); 
    }

    @Transactional
    public UsuarioDTO crearUsuario(UsuarioDTO dto) {
        try {
            Usuario usuario = new Usuario();
            usuario.setDni(dto.getDni().trim());
            usuario.setNombres(dto.getNombres().trim());
            usuario.setApellidos(dto.getApellidos().trim());
            usuario.setEmail(dto.getEmail().trim());
            usuario.setPasswordHash(passwordEncoder.encode(dto.getDni().trim())); 
            
            UserRole rolAsignado = UserRole.valueOf(dto.getRol().trim().toUpperCase());
            usuario.setRol(rolAsignado);
            
            Usuario usuarioGuardado = usuarioRepository.save(usuario);

            if (rolAsignado == UserRole.DOCENTE) {
                Docente docente = Docente.builder()
                        .usuario(usuarioGuardado)
                        .estado(TeacherStatus.ACTIVO).build();
                docenteRepository.save(docente);
            } else if (rolAsignado == UserRole.ALUMNO) {
                Alumno alumno = Alumno.builder()
                        .usuario(usuarioGuardado)
                        .estado(StudentStatus.ACTIVO).build();
                alumnoRepository.save(alumno);
            }
            
            return mapearADto(usuarioGuardado);
            
        } catch (DataIntegrityViolationException e) {
            throw new BusinessLogicException("El DNI o Correo ingresado ya se encuentra registrado.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessLogicException("Fallo en la base de datos al crear: " + e.getMessage());
        }
    }

    @Transactional
    public UsuarioDTO actualizarUsuario(Long id, UsuarioDTO dto) {
        try {
            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new BusinessLogicException("Usuario no encontrado"));
                    
            usuario.setDni(dto.getDni().trim());
            usuario.setNombres(dto.getNombres().trim());
            usuario.setApellidos(dto.getApellidos().trim());
            usuario.setEmail(dto.getEmail().trim());
            // PROTECCIÓN: Ya NO actualizamos el rol aquí para evitar corrupción entre tablas.
            
            return mapearADto(usuarioRepository.save(usuario));
            
        } catch (DataIntegrityViolationException e) {
            throw new BusinessLogicException("El DNI o Correo modificado choca con el de otro usuario.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessLogicException("Fallo en la BD al actualizar: " + e.getMessage());
        }
    }

    @Transactional
    public void eliminarUsuario(Long id) {
        try {
            usuarioRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessLogicException("No se puede eliminar: El usuario tiene notas, asistencias o trámites registrados.");
        } catch (Exception e) {
            throw new BusinessLogicException("Fallo al eliminar: " + e.getMessage());
        }
    }
    
    private UsuarioDTO mapearADto(Usuario usuario) {
        return UsuarioDTO.builder()
                .idUsuario(usuario.getIdUsuario())
                .dni(usuario.getDni())
                .nombres(usuario.getNombres())
                .apellidos(usuario.getApellidos())
                .nombreCompleto(usuario.getNombreCompleto())
                .email(usuario.getEmail())
                .rol(usuario.getRol().name())
                .build();
    }
}