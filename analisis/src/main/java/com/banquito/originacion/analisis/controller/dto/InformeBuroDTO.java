package com.banquito.originacion.analisis.controller.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO para el informe detallado de buró crediticio")
public class InformeBuroDTO {

    @Schema(description = "ID único del informe de buró", example = "1")
    private Long idInformeBuro;

    @NotNull(message = "El ID de consulta de buró es obligatorio")
    @Schema(description = "ID de la consulta de buró asociada", example = "1", required = true)
    private Long idConsultaBuro;

    @DecimalMin(value = "0", message = "El score debe ser mayor o igual a 0")
    @Schema(description = "Score crediticio del informe", example = "720")
    private BigDecimal score;

    @Min(value = 0, message = "El número de deudas impagas debe ser mayor o igual a 0")
    @Schema(description = "Número de deudas impagas reportadas", example = "2")
    private Integer numeroDeudaImpagas;

    @DecimalMin(value = "0.00", message = "El monto total adeudado debe ser mayor o igual a 0")
    @Schema(description = "Monto total adeudado según el informe", example = "15000.50")
    private BigDecimal montoTotalAdeudado;

    @DecimalMin(value = "0.00", message = "La capacidad de pago debe ser mayor o igual a 0")
    @Schema(description = "Capacidad de pago reportada", example = "5000.00")
    private BigDecimal capacidadPagoReportada;

    @Schema(description = "Respuesta completa del buró en formato JSON", 
            example = "{\"detalle_creditos\":[{\"entidad\":\"Banco XYZ\",\"saldo\":1000}]}")
    private String jsonRespuestaCompleta;

    @DecimalMin(value = "1", message = "La versión debe ser mayor a 0")
    @Schema(description = "Versión del registro para control de concurrencia", example = "1")
    private BigDecimal version;

    @Schema(description = "Información básica de la consulta de buró asociada")
    private ConsultaBuroBasicDTO consultaBuro;

    @Getter
    @Setter
    @Schema(description = "Información básica de la consulta de buró")
    public static class ConsultaBuroBasicDTO {
        
        @Schema(description = "ID de la consulta de buró", example = "1")
        private Long idConsulta;
        
        @Schema(description = "ID de la solicitud", example = "12345")
        private Integer idSolicitud;
        
        @Schema(description = "Score externo obtenido", example = "750")
        private BigDecimal scoreExterno;
        
        @Schema(description = "Estado de la consulta", example = "EXITOSA")
        private String estadoConsulta;
    }
} 