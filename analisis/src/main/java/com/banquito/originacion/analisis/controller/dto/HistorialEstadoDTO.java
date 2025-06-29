package com.banquito.originacion.analisis.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "DTO para el historial de estados de una solicitud")
public class HistorialEstadoDTO {

    @Schema(description = "ID único del historial de estado", example = "1")
    private Long idHistorial;

    @NotNull(message = "El ID de solicitud es obligatorio")
    @Schema(description = "ID de la solicitud", example = "12345")
    private Integer idSolicitud;

    @NotBlank(message = "El estado es obligatorio")
    @Size(max = 12, message = "El estado no puede tener más de 12 caracteres")
    @Schema(description = "Estado de la solicitud", example = "APROBADO")
    private String estado;

    @NotNull(message = "La fecha y hora son obligatorias")
    @Schema(description = "Fecha y hora del cambio de estado", example = "2024-01-15T10:30:00")
    private LocalDateTime fechaHora;

    @NotBlank(message = "El usuario es obligatorio")
    @Size(max = 50, message = "El usuario no puede tener más de 50 caracteres")
    @Schema(description = "Usuario que realizó el cambio", example = "jperez")
    private String usuario;

    @Size(max = 120, message = "El motivo no puede tener más de 120 caracteres")
    @Schema(description = "Motivo del cambio de estado", example = "Documentación completa y verificada")
    private String motivo;

    @NotNull(message = "La versión es obligatoria")
    @Schema(description = "Versión del registro", example = "1")
    private BigDecimal version;
} 