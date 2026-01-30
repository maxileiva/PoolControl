package com.example.usuarios.controller;

import com.example.usuarios.model.Usuario;
import com.example.usuarios.service.UsuarioService;
import com.example.usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Collections;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<Usuario> crear(@Valid @RequestBody Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuario));
    }

    @GetMapping
    public List<Usuario> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtener(@PathVariable Long id) {
        return service.buscarPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // ACTUALIZAR DATOS GENERALES
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarInformacion(@PathVariable Long id, @RequestBody Map<String, String> datos) {
        try {
            String nombre = datos.get("nombre");
            String apellido = datos.get("apellido");
            String telefono = datos.get("telefono");
            String fotoPerfil = datos.get("fotoPerfil");
            Usuario actualizado = service.actualizarInformacion(id, nombre, apellido, telefono, fotoPerfil);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("telefono_duplicado")) 
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("error", "telefono_duplicado"));
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    // CAMBIAR CONTRASEÑA (ESTE ES EL QUE LLAMAREMOS DESDE EL PERFIL)
    @PutMapping("/{id}/contrasena")
    public ResponseEntity<?> cambiarContrasena(@PathVariable Long id, @RequestBody Map<String, String> datos) {
        try {
            String actual = datos.get("contrasenaActual");
            String nueva = datos.get("nuevaContrasena");
            service.cambiarContrasena(id, actual, nueva);
            return ResponseEntity.ok(Collections.singletonMap("mensaje", "Contraseña actualizada"));
        } catch (IllegalArgumentException e) {
            // Retornamos el mensaje para que el Toast lo muestre
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @PostMapping("/recuperar-password")
    public ResponseEntity<?> recuperarContrasena(@RequestBody Map<String, String> datos) {
        String correo = datos.get("correo");
        String telefono = datos.get("telefono");
        String nueva = datos.get("nuevaContrasena");

        return usuarioRepository.findByCorreo(correo)
            .map(u -> {
                if (u.getTelefono().equals(telefono)) {
                    u.setContrasena(passwordEncoder.encode(nueva));
                    usuarioRepository.save(u);
                    return ResponseEntity.ok(Collections.singletonMap("mensaje", "Contraseña actualizada"));
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Teléfono incorrecto"));
            }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "Correo no encontrado")));
    }
}