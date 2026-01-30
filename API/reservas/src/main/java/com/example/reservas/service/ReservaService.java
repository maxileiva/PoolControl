package com.example.reservas.service;

import com.example.reservas.client.LogClient;
import com.example.reservas.model.Reserva;
import com.example.reservas.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository repo;

    @Autowired
    private LogClient logClient;

    public List<Reserva> listarTodas() {
        return repo.findAll();
    }

    public List<Reserva> listarPorCliente(Long idCliente) {
        return repo.findByIdCliente(idCliente);
    }

    public Reserva guardar(Reserva reserva) {
        Reserva nueva = repo.save(reserva);

        // 🔹 LOG CUANDO SE CREA UNA RESERVA
        logClient.enviarLog(
            "INFO",
            "Reserva creada para piscina " + reserva.getNombrePiscina(),
            "RESERVAS"
        );

        return nueva;
    }

    public Reserva cambiarEstado(Long id, String nuevoEstado) {
        Reserva r = repo.findById(id).orElse(null);
        if (r != null) {
            r.setEstado(nuevoEstado);
            Reserva actualizada = repo.save(r);

            // 🔹 LOG CUANDO SE CAMBIA ESTADO
            logClient.enviarLog(
                "INFO",
                "Estado de reserva ID " + id + " cambiado a " + nuevoEstado,
                "RESERVAS"
            );

            return actualizada;
        }
        return null;
    }

    public void eliminar(Long id) {
        repo.deleteById(id);

        // 🔹 LOG CUANDO SE ELIMINA
        logClient.enviarLog(
            "WARN",
            "Reserva eliminada con ID " + id,
            "RESERVAS"
        );
    }

    public Reserva obtener(Long id) {
        return repo.findById(id).orElse(null);
    }
}
