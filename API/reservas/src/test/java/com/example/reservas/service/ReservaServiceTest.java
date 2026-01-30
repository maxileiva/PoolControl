package com.example.reservas.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import com.example.reservas.model.Reserva;
import com.example.reservas.repository.ReservaRepository;
import com.example.reservas.webclient.UsuarioClient;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReservaServiceTest {

    @InjectMocks
    private ReservaService reservaService;

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private UsuarioClient usuarioClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Reserva crearReservaPrueba() {
        Reserva reserva = new Reserva();
        reserva.setIdCliente(301L);
        reserva.setFecha("2026-01-30");
        reserva.setNombrePiscina("Piscina Central");
        reserva.setDetalles("Familiar");
        reserva.setEstado("PENDIENTE");
        reserva.setPrecio(15000.0);
        return reserva;
    }

    @Test
    void guardarReserva_debeGuardar() {
        Reserva reserva = crearReservaPrueba();
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        Reserva resultado = reservaService.guardar(reserva);

        assertNotNull(resultado);
        assertEquals(301L, resultado.getIdCliente());
        assertEquals("Piscina Central", resultado.getNombrePiscina());
        verify(reservaRepository, times(1)).save(reserva);
    }

    @Test
    void cambiarEstado_reservaExistente_debeActualizar() {
        Reserva reservaExistente = crearReservaPrueba();
        reservaExistente.setId(1L);
        
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reservaExistente));
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaExistente);

        // Ejecutar
        Reserva resultado = reservaService.cambiarEstado(1L, "PAGADA");

        // Verificar
        assertNotNull(resultado);
        assertEquals("PAGADA", resultado.getEstado());
        verify(reservaRepository).save(reservaExistente);
    }
}