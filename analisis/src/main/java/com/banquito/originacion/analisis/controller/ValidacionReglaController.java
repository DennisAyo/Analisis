package com.banquito.originacion.analisis.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banquito.originacion.analisis.controller.dto.ValidacionReglaDTO;
import com.banquito.originacion.analisis.controller.mapper.ValidacionReglaMapper;
import com.banquito.originacion.analisis.exception.CreateException;
import com.banquito.originacion.analisis.exception.NotFoundException;
import com.banquito.originacion.analisis.model.ValidacionRegla;
import com.banquito.originacion.analisis.service.ValidacionReglaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/validaciones-reglas")
@Tag(name = "Validaciones de Reglas", description = "Gestión de validaciones de reglas aplicadas")
public class ValidacionReglaController {

    private final ValidacionReglaService service;

    public ValidacionReglaController(ValidacionReglaService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener validación por ID", 
               description = "Recupera una validación de regla específica por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Validación encontrada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Validación no encontrada")
    })
    public ResponseEntity<ValidacionReglaDTO> getValidacionById(
            @Parameter(description = "ID de la validación", example = "1")
            @PathVariable("id") Long id) {
        try {
            ValidacionRegla validacion = this.service.findById(id);
            return ResponseEntity.ok(ValidacionReglaMapper.toDTO(validacion));
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/evaluacion/{idEvaluacion}")
    @Operation(summary = "Obtener validaciones por evaluación", 
               description = "Recupera todas las validaciones asociadas a una evaluación crediticia")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Validaciones encontradas exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron validaciones para la evaluación")
    })
    public ResponseEntity<List<ValidacionReglaDTO>> getValidacionesByEvaluacion(
            @Parameter(description = "ID de la evaluación crediticia", example = "1")
            @PathVariable("idEvaluacion") Long idEvaluacion) {
        try {
            List<ValidacionRegla> validaciones = this.service.findByEvaluacionCrediticia(idEvaluacion);
            List<ValidacionReglaDTO> dtos = new ArrayList<>(validaciones.size());
            
            for (ValidacionRegla validacion : validaciones) {
                dtos.add(ValidacionReglaMapper.toDTO(validacion));
            }
            return ResponseEntity.ok(dtos);
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/regla/{idRegla}")
    @Operation(summary = "Obtener validaciones por regla", 
               description = "Recupera todas las validaciones de una regla específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Validaciones encontradas exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron validaciones para la regla")
    })
    public ResponseEntity<List<ValidacionReglaDTO>> getValidacionesByRegla(
            @Parameter(description = "ID de la regla", example = "1")
            @PathVariable("idRegla") Long idRegla) {
        try {
            List<ValidacionRegla> validaciones = this.service.findByRegla(idRegla);
            List<ValidacionReglaDTO> dtos = new ArrayList<>(validaciones.size());
            
            for (ValidacionRegla validacion : validaciones) {
                dtos.add(ValidacionReglaMapper.toDTO(validacion));
            }
            return ResponseEntity.ok(dtos);
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Crear validación de regla", 
               description = "Registra el resultado de aplicar una regla a una evaluación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Validación creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "422", description = "Error de reglas de negocio")
    })
    public ResponseEntity<ValidacionReglaDTO> createValidacion(
            @Parameter(description = "Datos de la validación a crear")
            @Valid @RequestBody ValidacionReglaDTO validacionDTO) {
        try {
            ValidacionRegla validacion = ValidacionReglaMapper.toEntity(validacionDTO);
            ValidacionRegla validacionCreada = this.service.create(validacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(ValidacionReglaMapper.toDTO(validacionCreada));
        } catch (CreateException ce) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    @GetMapping("/evaluacion/{idEvaluacion}/resumen")
    @Operation(summary = "Obtener resumen de validaciones", 
               description = "Obtiene un resumen estadístico de las validaciones de una evaluación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resumen obtenido exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron validaciones para la evaluación")
    })
    public ResponseEntity<Map<String, Object>> getResumenValidaciones(
            @Parameter(description = "ID de la evaluación crediticia", example = "1")
            @PathVariable("idEvaluacion") Long idEvaluacion) {
        try {
            List<ValidacionRegla> todasValidaciones = this.service.findByEvaluacionCrediticia(idEvaluacion);
            
            long totalValidaciones = todasValidaciones.size();
            long validacionesExitosas = todasValidaciones.stream()
                    .mapToLong(v -> v.getResultado() ? 1 : 0)
                    .sum();
            long validacionesFallidas = totalValidaciones - validacionesExitosas;
            
            double porcentajeExito = totalValidaciones > 0 ? 
                    (double) validacionesExitosas / totalValidaciones * 100 : 0;
            
            return ResponseEntity.ok(Map.of(
                "idEvaluacion", idEvaluacion,
                "totalValidaciones", totalValidaciones,
                "validacionesExitosas", validacionesExitosas,
                "validacionesFallidas", validacionesFallidas,
                "porcentajeExito", Math.round(porcentajeExito * 100.0) / 100.0
            ));
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Map<String, String>> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                    "error", "RECURSO_NO_ENCONTRADO",
                    "mensaje", ex.getMessage()
                ));
    }

    @ExceptionHandler({CreateException.class})
    public ResponseEntity<Map<String, String>> handleCreateException(CreateException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(Map.of(
                    "error", "ERROR_CREACION",
                    "mensaje", ex.getMessage()
                ));
    }
} 