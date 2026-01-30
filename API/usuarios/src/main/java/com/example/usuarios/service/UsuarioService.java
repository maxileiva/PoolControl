package com.example.usuarios.service;

import com.example.usuarios.model.Usuario;
import com.example.usuarios.model.Rol;
import com.example.usuarios.repository.UsuarioRepository;
import com.example.usuarios.repository.RolRepository; // IMPORTANTE: Añade esta importación
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private RolRepository rolRepository; // Inyectamos el repositorio de roles
    @Autowired private PasswordEncoder passwordEncoder;

    public Usuario guardar(Usuario usuario) {
        // 1. Encriptar contraseña si viene en el objeto
        if (usuario.getContrasena() != null) {
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        }

        // 2. Auto-asignar rol CLIENTE si el usuario no trae un rol (Registro desde la App)
        if (usuario.getRol() == null) {
            Rol rolCliente = rolRepository.findByNombre("CLIENTE")
                .orElseThrow(() -> new RuntimeException("Error: El rol CLIENTE debe existir en la base de datos."));
            usuario.setRol(rolCliente);
        }

        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    public Usuario actualizarInformacion(Long id, String nombre, String apellido, String telefono) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setTelefono(telefono);
        return usuarioRepository.save(usuario);
    }

    public void cambiarContrasena(Long id, String actual, String nueva) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();
        if (passwordEncoder.matches(actual, usuario.getContrasena())) {
            usuario.setContrasena(passwordEncoder.encode(nueva));
            usuarioRepository.save(usuario);
        }
    }
}