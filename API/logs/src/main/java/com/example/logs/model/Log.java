package com.example.logs.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nivel; // INFO, WARN, ERROR

    @Column(nullable = false, length = 500)
    private String mensaje;

    @Column(nullable = false)
    private String servicio;

    @Column(nullable = false)
    private LocalDateTime fecha;
}
