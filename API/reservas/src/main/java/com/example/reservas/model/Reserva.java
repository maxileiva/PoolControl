package com.example.reservas.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "reservas")
@Data
public class Reserva {
    @Id
@GeneratedValue(strategy = GenerationType.IDENTITY) // <--- ESTO DEBE ESTAR ASÍ
private Long id;
    private String fecha;
    private String nombrePiscina;
    private String detalles;
    private Double precio;
    private Long idCliente;
    private String estado; // <--- AGREGAR ESTO (PENDIENTE, PAGADO, SOLICITA CANCELACION)
}