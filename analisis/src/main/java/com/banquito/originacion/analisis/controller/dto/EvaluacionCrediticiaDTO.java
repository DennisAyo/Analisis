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
    private Long idEvaluacion;

    @NotNull(message = "El ID de solicitud es obligatorio")
    @Schema(description = "ID de la solicitud", example = "12345")
    private Integer idSolicitud;

    @NotNull(message = "El ID de informe de buró es obligatorio")
    @Schema(description = "ID del informe de buró asociado", example = "10")
    private Long idInformeBuro;

    @NotNull(message = "La fecha de evaluación es obligatoria")
    @Schema(description = "Fecha y hora de la evaluación", example = "2024-01-15T10:30:00")
    private LocalDateTime fechaEvaluacion;

    @Schema(description = "Score interno calculado", example = "680")
    private BigDecimal scoreInternoCalculado;

    @Size(max = 50, message = "El resultado automático no puede exceder 50 caracteres")
    @Schema(description = "Resultado de la evaluación automática", 
            example = "APROBADO", 
            allowableValues = {"APROBADO", "RECHAZADO", "REVISION_MANUAL"})
    private String resultadoAutomatico;

    @Schema(description = "Observaciones del motor de reglas")
    private String observacionesMotorReglas;

    @Size(max = 50, message = "La decisión final no puede exceder 50 caracteres")
    @Schema(description = "Decisión final del analista", 
            example = "APROBADO",
            allowableValues = {"APROBADO", "RECHAZADO", "PENDIENTE"})
    private String decisionFinalAnalista;

    @Schema(description = "Justificación del analista")
    private String justificacionAnalista;

    @NotNull(message = "La versión es obligatoria")
    @Schema(description = "Versión del registro", example = "1")
    private BigDecimal version;

    @Schema(description = "Información básica del informe de buró")
    private InformeBuroBasicDTO informeBuro;

    @Data
    @Schema(description = "Información básica de informe de buró")
    public static class InformeBuroBasicDTO {
        @Schema(description = "ID del informe", example = "10")
        private Long idInformeBuro;
        
        @Schema(description = "Score del informe", example = "720")
        private BigDecimal score;
        
        @Schema(description = "Número de deudas impagas", example = "2")
        private Integer numeroDeudaImpagas;
        
        @Schema(description = "Monto total adeudado", example = "25000.50")
        private BigDecimal montoTotalAdeudado;
    }
} 