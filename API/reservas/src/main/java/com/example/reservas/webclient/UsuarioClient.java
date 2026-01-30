package com.example.reservas.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

@Component
public class UsuarioClient {

    private final WebClient webClient;

    public UsuarioClient(@Value("${usuarios-service.url}") String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public String obtenerRolPorId(Long idUsuario) {
        try {
            Map<?, ?> response = webClient.get()
                    .uri("/{id}", idUsuario)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            if (response != null && response.get("rol") != null) {
                Map<?, ?> rol = (Map<?, ?>) response.get("rol");
                return rol.get("nombre").toString();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}