package com.banquito.originacion.analisis.controller.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "DTO para las reglas de evaluación crediticia")
public class ReglaEvaluacionCrediticiaDTO {

    @Schema(description = "ID único de la regla", example = "1")
    private Long idRegla;

    @NotBlank(message = "El nombre de la regla es obligatorio")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    @Schema(description = "Nombre de la regla", example = "Verificación de ingresos mínimos")
    private String nombre;

    @Size(max = 500, message = "La descripción no puede tener más de 500 caracteres")
    @Schema(description = "Descripción detallada de la regla", 
            example = "Verifica que los ingresos del solicitante sean superiores a 3 veces la cuota mensual")
    private String descripcion;

    @NotBlank(message = "La condición es obligatoria")
    @Size(max = 100, message = "La condición no puede tener más de 100 caracteres")
    @Schema(description = "Condición lógica de la regla", example = "ingresos >= (cuota * 3)")
    private String condicion;

    @NotNull(message = "La prioridad es obligatoria")
    @Schema(description = "Prioridad de ejecución de la regla", example = "1")
    private Integer prioridad;

    @NotNull(message = "El estado activo es obligatorio")
    @Schema(description = "Indica si la regla está activa", example = "true")
    private Boolean activa;

    @NotNull(message = "La versión es obligatoria")
    @Schema(description = "Versión del registro", example = "1")
    private BigDecimal version;
} 