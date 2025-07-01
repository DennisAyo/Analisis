package com.banquito.originacion.analisis.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class HistorialEstadoDTO {
    private Long idHistorial;
    private Long idConsulta;
    private String estadoAnterior;
    private String estadoNuevo;
    private LocalDateTime fechaCambio;
    private String usuario;
    private Long version;
} 