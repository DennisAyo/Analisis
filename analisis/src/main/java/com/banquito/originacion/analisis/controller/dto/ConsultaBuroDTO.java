package com.banquito.originacion.analisis.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "DTO para las consultas de buró crediticio")
public class ConsultaBuroDTO {

    @Schema(description = "ID único de la consulta de buró", example = "1")
    private Long idConsultaBuro;

    @NotNull(message = "El ID de cliente prospecto es obligatorio")
    @Schema(description = "ID del cliente prospecto", example = "12345")
    private Integer idClienteProspecto;

    @Schema(description = "Score obtenido en la consulta", example = "750")
    private BigDecimal scoreObtenido;

    @NotNull(message = "La fecha de consulta es obligatoria")
    @Schema(description = "Fecha y hora de la consulta", example = "2024-01-15T10:30:00")
    private LocalDateTime fechaConsulta;

    @Size(max = 500, message = "El detalle de deudas no puede tener más de 500 caracteres")
    @Schema(description = "Detalle de las deudas encontradas", 
            example = "Tarjeta de crédito: $5000, Préstamo personal: $15000")
    private String detalleDeudas;

    @NotBlank(message = "El estado de consulta es obligatorio")
    @Size(max = 50, message = "El estado de consulta no puede tener más de 50 caracteres")
    @Schema(description = "Estado de la consulta", example = "EXITOSA")
    private String estadoConsulta;

    @NotNull(message = "La versión es obligatoria")
    @Schema(description = "Versión del registro", example = "1")
    private BigDecimal version;
} 