package com.example.reservas.client;

import com.example.reservas.dto.LogRequestDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class LogClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String LOG_URL = "http://localhost:8083/logs";

    public void enviarLog(String nivel, String mensaje, String servicio) {
        try {
            LogRequestDTO log = new LogRequestDTO(nivel, mensaje, servicio);
            restTemplate.postForObject(LOG_URL, log, Void.class);
        } catch (Exception e) {
            System.err.println("Error enviando log: " + e.getMessage());
        }
    }
}
