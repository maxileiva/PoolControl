package com.example.usuarios.service;

import com.example.usuarios.model.Rol;
import com.example.usuarios.model.Usuario;
import com.example.usuarios.repository.RolRepository;
import com.example.usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RolService {

    @Autowired 
    private RolRepository rolRepository;
    
    @Autowired 
    private UsuarioRepository usuarioRepository;

    // Método para validar si es Admin
    public void validarAdmin(Long idSolicitante) {
        Usuario admin = usuarioRepository.findById(idSolicitante)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (!"ADMINISTRATIVO".equalsIgnoreCase(admin.getRol().getNombre())) {
            throw new RuntimeException("No tiene permisos de administrador");
        }
    }

    // Método que faltaba para el Controller
    public Rol crearRol(Rol rol, Long idSolicitante) {
        validarAdmin(idSolicitante);
        return rolRepository.save(rol);
    }

    // Método que faltaba para el Controller
    public List<Rol> listarRoles(Long idSolicitante) {
        validarAdmin(idSolicitante);
        return rolRepository.findAll();
    }
}