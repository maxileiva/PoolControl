package com.example.logs.service;

import com.example.logs.dto.LogRequestDTO;
import com.example.logs.model.Log;
import com.example.logs.repository.LogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LogServiceImplTest {

    @Mock
    private LogRepository logRepository;

    @InjectMocks
    private LogServiceImpl logService;

    @Test
    void crearLog_deberiaGuardarYRetornarLog() {
        // Preparar datos
        LogRequestDTO request = new LogRequestDTO();
        request.setNivel("INFO");
        request.setMensaje("Test log");
        request.setServicio("logs");

        Log logGuardado = Log.builder()
                .id(1L)
                .nivel("INFO")
                .mensaje("Test log")
                .servicio("logs")
                .fecha(LocalDateTime.now())
                .build();

        // Mockear el repository
        when(logRepository.save(any(Log.class))).thenReturn(logGuardado);

        // Llamar al service
        var response = logService.crearLog(request);

        // Verificar resultados
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getNivel()).isEqualTo("INFO");
        assertThat(response.getMensaje()).isEqualTo("Test log");
    }

    @Test
    void obtenerTodos_deberiaRetornarLista() {
        Log log1 = Log.builder().id(1L).nivel("INFO").mensaje("A").servicio("logs").fecha(LocalDateTime.now()).build();
        Log log2 = Log.builder().id(2L).nivel("ERROR").mensaje("B").servicio("logs").fecha(LocalDateTime.now()).build();

        when(logRepository.findAll()).thenReturn(java.util.List.of(log1, log2));

        var lista = logService.obtenerTodos();

        assertThat(lista).hasSize(2);
        assertThat(lista.get(0).getNivel()).isEqualTo("INFO");
        assertThat(lista.get(1).getNivel()).isEqualTo("ERROR");
    }
}
