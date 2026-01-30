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
            // 1. Crear Roles si no existen
            if (rolRepo.count() == 0) {
                rolRepo.save(new Rol(null, "ADMINISTRATIVO"));
                rolRepo.save(new Rol(null, "OPERADOR"));
                rolRepo.save(new Rol(null, "CLIENTE"));
            }

            // 2. Crear Usuario Admin inicial si no existen usuarios
            if (userRepo.count() == 0) {
                Rol adminRol = rolRepo.findByNombre("ADMINISTRATIVO").orElseThrow();
                
                // Actualizado: Ahora pasamos 8 parámetros al constructor (el último es la fotoPerfil como null)
                userRepo.save(new Usuario(
                    null, 
                    "Rigoberto", 
                    "Leiva", 
                    "admin@pool.com", 
                    "912345678", 
                    encoder.encode("ADMIN123"), 
                    adminRol,
                    null // fotoPerfil inicial
                ));
            }
        };
    }
}