package com.banquito.originacion.analisis.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "DTO para las consultas de buró")
public class ConsultaBuroDTO {

    @Schema(description = "ID único de la consulta", example = "1")
    private Long idConsulta;

    @NotNull(message = "El ID de solicitud es obligatorio")
    @Schema(description = "ID de la solicitud", example = "12345")
    private Integer idSolicitud;

    @NotNull(message = "La fecha de consulta es obligatoria")
    @Schema(description = "Fecha y hora de la consulta", example = "2024-01-15T10:30:00")
    private LocalDateTime fechaConsulta;

    @NotNull(message = "El estado de consulta es obligatorio")
    @Size(max = 50, message = "El estado no puede tener más de 50 caracteres")
    @Schema(description = "Estado de la consulta", example = "EXITOSA")
    private String estadoConsulta;

    @Schema(description = "Score externo obtenido del buró", example = "750")
    private BigDecimal scoreExterno;

    @Schema(description = "Número de cuentas activas", example = "5")
    private BigDecimal cuentasActivas;

    @Schema(description = "Número de cuentas morosas", example = "2")
    private BigDecimal cuentasMorosas;

    @Schema(description = "Monto total moroso", example = "15000.75")
    private BigDecimal montoMorosoTotal;

    @Schema(description = "Días de mora promedio", example = "45.50")
    private BigDecimal diasMoraPromedio;

    @Schema(description = "Fecha de la primera mora", example = "2023-08-15")
    private LocalDate fechaPrimeraMora;

    @Schema(description = "Datos del buró encriptados (solo metadatos en respuesta)")
    private String datosBuroEncriptadoInfo;

    @NotNull(message = "La versión es obligatoria")
    @Schema(description = "Versión del registro", example = "1")
    private BigDecimal version;
} 