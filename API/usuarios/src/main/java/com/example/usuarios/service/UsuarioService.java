package com.example.usuarios.service;

import com.example.client.LogClient;
import com.example.usuarios.dto.LogRequestDTO;
import com.example.usuarios.model.Usuario;
import com.example.usuarios.model.Rol;
import com.example.usuarios.repository.UsuarioRepository;
import com.example.usuarios.repository.RolRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LogClient logClient; // 🔥 conexión con microservicio logs

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    /**
     * Registro de nuevo usuario (Registro App)
     */
    public Usuario guardar(Usuario usuario) {

        // 1. Encriptar contraseña
        if (usuario.getContrasena() != null) {
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        }

        // 2. Rol por defecto CLIENTE
        if (usuario.getRol() == null) {
            Rol rolCliente = rolRepository.findByNombre("CLIENTE")
                    .orElseThrow(() -> new RuntimeException("El rol CLIENTE debe existir"));
            usuario.setRol(rolCliente);
        }

        Usuario guardado = usuarioRepository.save(usuario);

        // 🔥 LOG CREACIÓN USUARIO
        logClient.enviarLog(new LogRequestDTO(
                "INFO",
                "Usuario creado con ID " + guardado.getId() + " y correo " + guardado.getCorreo(),
                "USUARIOS"
        ));

        return guardado;
    }

    /**
     * Actualización de perfil
     */
    @Transactional
    public Usuario actualizarInformacion(
            Long id,
            String nombre,
            String apellido,
            String telefono,
            String fotoPerfil) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Validar teléfono duplicado
        Optional<Usuario> existente = usuarioRepository.findByTelefono(telefono);
        if (existente.isPresent() && !existente.get().getId().equals(id)) {
            throw new IllegalArgumentException("telefono_duplicado");
        }

        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setTelefono(telefono);

        if (fotoPerfil != null && !fotoPerfil.isBlank()) {
            usuario.setFotoPerfil(fotoPerfil);
        }

        Usuario actualizado = usuarioRepository.save(usuario);

        // 🔥 LOG ACTUALIZACIÓN PERFIL
        logClient.enviarLog(new LogRequestDTO(
                "INFO",
                "Perfil actualizado para usuario ID " + id,
                "USUARIOS"
        ));

        return actualizado;
    }

    /**
     * Cambio de contraseña seguro
     */
    @Transactional
    public void cambiarContrasena(Long id, String actual, String nueva) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (!passwordEncoder.matches(actual, usuario.getContrasena())) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta");
        }

        usuario.setContrasena(passwordEncoder.encode(nueva));
        usuarioRepository.save(usuario);

        // 🔥 LOG CAMBIO CONTRASEÑA
        logClient.enviarLog(new LogRequestDTO(
                "WARN",
                "Cambio de contraseña para usuario ID " + id,
                "USUARIOS"
        ));
    }
}
