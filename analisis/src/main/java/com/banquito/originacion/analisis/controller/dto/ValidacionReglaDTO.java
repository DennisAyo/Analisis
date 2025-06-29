package com.banquito.originacion.analisis.controller.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO para las validaciones de reglas crediticias")
public class ValidacionReglaDTO {

    @Schema(description = "ID único de la validación", example = "1")
    private Long idValidacionRegla;

    @NotNull(message = "El ID de evaluación crediticia es obligatorio")
    @Schema(description = "ID de la evaluación crediticia", example = "5")
    private Long idEvaluacionesCrediticias;

    @NotNull(message = "El ID de regla es obligatorio")
    @Schema(description = "ID de la regla aplicada", example = "3")
    private Long idRegla;

    @NotNull(message = "El resultado es obligatorio")
    @Schema(description = "Resultado de la validación", example = "true")
    private Boolean resultado;

    @NotNull(message = "La fecha es obligatoria")
    @Schema(description = "Fecha y hora de la validación", example = "2024-01-15T15:45:00")
    private LocalDateTime fecha;

    // DTOs anidados para mostrar información relacionada
    @Schema(description = "Información básica de la evaluación crediticia")
    private EvaluacionBasicDTO evaluacionCrediticia;

    @Schema(description = "Información básica de la regla aplicada")
    private ReglaBasicDTO regla;

    @Data
    @Schema(description = "Información básica de evaluación crediticia")
    public static class EvaluacionBasicDTO {
        @Schema(description = "ID de la evaluación", example = "5")
        private Long idEvaluacionesCrediticias;
        
        @Schema(description = "ID de solicitud", example = "12345")
        private Integer idSolicitud;
        
        @Schema(description = "Categoría de riesgo", example = "MEDIO")
        private String categoriaRiesgo;
    }

    @Data
    @Schema(description = "Información básica de regla")
    public static class ReglaBasicDTO {
        @Schema(description = "ID de la regla", example = "3")
        private Long idRegla;
        
        @Schema(description = "Nombre de la regla", example = "Verificación de ingresos")
        private String nombre;
        
        @Schema(description = "Prioridad", example = "1")
        private Integer prioridad;
    }
} 