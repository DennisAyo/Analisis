package com.banquito.originacion.analisis.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "DTO para las evaluaciones crediticias")
public class EvaluacionCrediticiaDTO {

    @Schema(description = "ID único de la evaluación crediticia", example = "1")
    private Long idEvaluacionesCrediticias;

    @NotNull(message = "El ID de solicitud es obligatorio")
    @Schema(description = "ID de la solicitud", example = "12345")
    private Integer idSolicitud;

    @NotNull(message = "El ID de consulta de buró es obligatorio")
    @Schema(description = "ID de la consulta de buró asociada", example = "10")
    private Long idConsultaBuro;

    @Schema(description = "Score interno calculado", example = "680")
    private BigDecimal scoreInterno;

    @Size(max = 50, message = "La categoría de riesgo no puede tener más de 50 caracteres")
    @Schema(description = "Categoría de riesgo asignada", example = "MEDIO")
    private String categoriaRiesgo;

    @NotNull(message = "El tipo de evaluación es obligatorio")
    @Schema(description = "Indica si la evaluación es automática", example = "true")
    private Boolean esAutomatico;

    @NotNull(message = "La fecha de evaluación es obligatoria")
    @Schema(description = "Fecha y hora de la evaluación", example = "2024-01-15T14:30:00")
    private LocalDateTime fechaEvaluacion;

    @NotNull(message = "La versión es obligatoria")
    @Schema(description = "Versión del registro", example = "1")
    private BigDecimal version;

    // DTO anidado para mostrar información básica de la consulta de buró
    @Schema(description = "Información básica de la consulta de buró")
    private ConsultaBuroBasicDTO consultaBuro;

    @Data
    @Schema(description = "Información básica de consulta de buró")
    public static class ConsultaBuroBasicDTO {
        @Schema(description = "ID de la consulta", example = "10")
        private Long idConsultaBuro;
        
        @Schema(description = "Score obtenido", example = "750")
        private BigDecimal scoreObtenido;
        
        @Schema(description = "Estado de la consulta", example = "EXITOSA")
        private String estadoConsulta;
    }
} 