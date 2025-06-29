package com.banquito.originacion.analisis.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "DTO para las observaciones de los analistas")
public class ObservacionAnalistaDTO {

    @Schema(description = "ID único de la observación del analista", example = "1")
    private Long idObservacionAnalista;

    @NotNull(message = "El ID de solicitud es obligatorio")
    @Schema(description = "ID de la solicitud", example = "12345")
    private Integer idSolicitud;

    @NotBlank(message = "El usuario es obligatorio")
    @Size(max = 50, message = "El usuario no puede tener más de 50 caracteres")
    @Schema(description = "Usuario analista que realizó la observación", example = "analista1")
    private String usuario;

    @NotNull(message = "La fecha y hora son obligatorias")
    @Schema(description = "Fecha y hora de la observación", example = "2024-01-15T14:30:00")
    private LocalDateTime fechaHora;

    @NotBlank(message = "La razón de intervención es obligatoria")
    @Size(max = 600, message = "La razón de intervención no puede tener más de 600 caracteres")
    @Schema(description = "Razón de la intervención del analista", 
            example = "Se requiere documentación adicional para verificar los ingresos del solicitante")
    private String razonIntervencion;

    @NotNull(message = "La versión es obligatoria")
    @Schema(description = "Versión del registro", example = "1")
    private BigDecimal version;
} 