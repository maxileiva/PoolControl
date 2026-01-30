package com.example.usuarios.controller;

import com.example.usuarios.dto.LoginRequest;
import com.example.usuarios.dto.RegisterRequest;
import com.example.usuarios.model.Rol;
import com.example.usuarios.model.Usuario;
import com.example.usuarios.repository.RolRepository;
import com.example.usuarios.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return usuarioRepository.findByCorreo(request.getCorreo())
                .map(usuario -> {
                    boolean passwordOk = passwordEncoder.matches(request.getContrasena(), usuario.getContrasena());
                    if (!passwordOk) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
                    }
                    usuario.setContrasena(null);
                    return ResponseEntity.ok(usuario);
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        // 1. Validar duplicidad por Correo
        if (usuarioRepository.findByCorreo(req.getCorreo()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El correo ya está registrado");
        }

        // 2. Validar duplicidad por Teléfono
        if (usuarioRepository.findByTelefono(req.getTelefono()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El teléfono ya está registrado");
        }

        // Buscar rol
        Rol rol = rolRepository.findByNombre(req.getRolNombre())
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado: " + req.getRolNombre()));

        Usuario usuario = new Usuario();
        usuario.setNombre(req.getNombre());
        usuario.setApellido(req.getApellido());
        usuario.setCorreo(req.getCorreo());
        usuario.setTelefono(req.getTelefono());
        usuario.setContrasena(passwordEncoder.encode(req.getContrasena()));
        usuario.setRol(rol);

        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        nuevoUsuario.setContrasena(null); 
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }
}