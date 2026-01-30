package com.example.client;

import com.example.usuarios.dto.LogRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "log-service", url = "http://localhost:8083")
public interface LogClient {

    @PostMapping("/logs")
    void enviarLog(@RequestBody LogRequestDTO logRequest);
}
