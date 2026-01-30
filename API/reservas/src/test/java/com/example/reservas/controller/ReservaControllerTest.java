package com.example.reservas.controller;

import com.example.reservas.model.Reserva;
import com.example.reservas.service.ReservaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservaController.class)
public class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservaService service;

    @Autowired
    private ObjectMapper mapper;

    private Reserva getReservaEjemplo() {
        Reserva r = new Reserva();
        r.setId(1L);
        r.setIdCliente(301L);
        r.setFecha("2026-01-30");
        r.setNombrePiscina("Piscina Olimpica");
        r.setDetalles("5 personas");
        r.setEstado("PENDIENTE");
        r.setPrecio(25000.0);
        return r;
    }

    @Test
    void crearReserva_valida_retorna201() throws Exception {
        Reserva reserva = getReservaEjemplo();
        when(service.guardar(any(Reserva.class))).thenReturn(reserva);

        mockMvc.perform(post("/api/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(reserva)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombrePiscina").value("Piscina Olimpica"))
                .andExpect(jsonPath("$.idCliente").value(301L));
    }

    @Test
    void listarReservas_retornaLista() throws Exception {
        when(service.listarTodas()).thenReturn(List.of(getReservaEjemplo()));

        mockMvc.perform(get("/api/reservas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombrePiscina").value("Piscina Olimpica"))
                .andExpect(jsonPath("$[0].estado").value("PENDIENTE"));
    }

    @Test
    void obtenerReserva_existente() throws Exception {
        when(service.obtener(1L)).thenReturn(getReservaEjemplo());

        mockMvc.perform(get("/api/reservas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.precio").value(25000.0));
    }

    @Test
    void eliminarReserva() throws Exception {
        mockMvc.perform(delete("/api/reservas/1"))
                .andExpect(status().isNoContent());
    }
}