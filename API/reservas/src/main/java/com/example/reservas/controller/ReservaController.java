package com.example.reservas.controller;

import com.example.reservas.model.Reserva;
import com.example.reservas.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
@Tag(name = "Reservas", description = "Gestión de reservas de piscinas")
public class ReservaController {

    @Autowired
    private ReservaService service;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Servicio de Reservas funcionando correctamente");
    }

    @GetMapping
    public List<Reserva> listar() {
        return service.listarTodas();
    }

    @GetMapping("/cliente/{idCliente}")
    public List<Reserva> listarPorCliente(@PathVariable Long idCliente) {
        return service.listarPorCliente(idCliente);
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Reserva reserva) {
        try {
            Reserva nueva = service.guardar(reserva);
            return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestParam String nuevoEstado) {
        Reserva actualizada = service.cambiarEstado(id, nuevoEstado);
        return actualizada != null ? ResponseEntity.ok(actualizada) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}