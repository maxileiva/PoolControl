package com.example.reservas.repository;

import com.example.reservas.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    // Para que el cliente vea su historial en Android
    List<Reserva> findByIdCliente(Long idCliente);
}