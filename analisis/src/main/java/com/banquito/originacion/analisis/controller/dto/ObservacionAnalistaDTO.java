package com.banquito.originacion.analisis.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "DTO para las observaciones de analistas")
public class ObservacionAnalistaDTO {

    @Schema(description = "ID único de la observación", example = "1")
    private Long idObservacion;

    @NotNull(message = "El ID de evaluación es obligatorio")
    @Schema(description = "ID de la evaluación crediticia", example = "123")
    private BigDecimal idEvaluacion;

    @NotNull(message = "El ID de usuario es obligatorio")
    @Schema(description = "ID del usuario analista", example = "456")
    private BigDecimal idUsuario;

    @NotNull(message = "La razón de intervención es obligatoria")
    @Size(max = 100, message = "La razón de intervención no puede tener más de 100 caracteres")
    @Schema(description = "Razón de intervención automática", example = "SCORE_INSUFICIENTE")
    private String razonIntervencionAutoEnum;

    @Schema(description = "Justificación detallada de la observación")
    private String justificacion;

    @NotNull(message = "La fecha y hora son obligatorias")
    @Schema(description = "Fecha y hora de la observación", example = "2024-01-15T16:30:00")
    private LocalDateTime fechaHora;

    @NotNull(message = "La versión es obligatoria")
    @Schema(description = "Versión del registro", example = "1")
    private BigDecimal version;
} 