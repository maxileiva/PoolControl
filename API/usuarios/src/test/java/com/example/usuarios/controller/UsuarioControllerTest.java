package com.example.usuarios.controller;

import com.example.usuarios.config.SecurityTestConfig;
import com.example.usuarios.model.Rol;
import com.example.usuarios.model.Usuario;
import com.example.usuarios.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityTestConfig.class)
@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService service;

    @Autowired
    private ObjectMapper mapper;

    // Método auxiliar actualizado: Eliminamos el RUT del constructor
    private Usuario getEjemploUsuario() {
        // Asumiendo que tu constructor ahora es: id, nombre, apellido, correo, telefono, contrasena, rol
        return new Usuario(1L, "Luis", "Hurtubia", "luis@example.com", "+56912345678", "clave123", new Rol(1L, "CLIENTE"));
    }

    @Test
    void crearUsuario_retorna201() throws Exception {
        Usuario usuario = getEjemploUsuario();
        when(service.guardar(any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(usuario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Luis"))
                .andExpect(jsonPath("$.correo").value("luis@example.com")); // Validamos por correo en lugar de RUT
    }

    @Test
    void listarUsuarios_retornaLista() throws Exception {
        when(service.listar()).thenReturn(List.of(getEjemploUsuario()));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].correo").value("luis@example.com"));
    }

    @Test
    void obtenerUsuarioPorId_existente() throws Exception {
        Usuario usuario = getEjemploUsuario();
        when(service.buscarPorId(1L)).thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.apellido").value("Hurtubia"));
    }

    @Test
    void obtenerUsuarioPorId_noExistente() throws Exception {
        when(service.buscarPorId(100L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/usuarios/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarUsuarioPorCorreo_existente() throws Exception {
        Usuario usuario = getEjemploUsuario();
        when(service.buscarPorCorreo("luis@example.com")).thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/api/usuarios/correo/luis@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Luis"));
    }

    // Se eliminaron los métodos buscarUsuarioPorRut_existente y buscarUsuarioPorRut_noExistente
}