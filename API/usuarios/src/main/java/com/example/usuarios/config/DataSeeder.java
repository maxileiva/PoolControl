package com.example.usuarios.config;

import com.example.usuarios.model.Rol;
import com.example.usuarios.model.Usuario;
import com.example.usuarios.repository.RolRepository;
import com.example.usuarios.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initData(RolRepository rolRepo, UsuarioRepository userRepo, PasswordEncoder encoder) {
        return args -> {
            if (rolRepo.count() == 0) {
                rolRepo.save(new Rol(null, "ADMINISTRATIVO"));
                rolRepo.save(new Rol(null, "OPERADOR"));
                rolRepo.save(new Rol(null, "CLIENTE"));
            }

            if (userRepo.count() == 0) {
                Rol adminRol = rolRepo.findByNombre("ADMINISTRATIVO").orElseThrow();
                // Constructor de 7 parámetros (id, nombre, apellido, correo, telefono, password, rol)
                userRepo.save(new Usuario(null, "Rigoberto", "Leiva", "admin@pool.com", "912345678", encoder.encode("ADMIN123"), adminRol));
            }
        };
    }
} // <--- EL ARCHIVO DEBE TERMINAR AQUÍ. NO PONGAS OTRAS CLASES ABAJO.