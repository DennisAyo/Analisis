package com.banquito.originacion.analisis.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.banquito.originacion.analisis.controller.dto.ObservacionAnalistaDTO;
import com.banquito.originacion.analisis.controller.mapper.ObservacionAnalistaMapper;
import com.banquito.originacion.analisis.exception.BusinessRuleException;
import com.banquito.originacion.analisis.exception.CreateException;
import com.banquito.originacion.analisis.exception.NotFoundException;
import com.banquito.originacion.analisis.exception.UpdateException;
import com.banquito.originacion.analisis.model.ObservacionAnalista;
import com.banquito.originacion.analisis.service.ObservacionAnalistaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/observaciones-analista")
@Tag(name = "Observaciones del Analista", description = "Gestión de observaciones realizadas por analistas")
public class ObservacionAnalistaController {

    private final ObservacionAnalistaService service;

    public ObservacionAnalistaController(ObservacionAnalistaService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener observación por ID", 
               description = "Recupera una observación del analista específica por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Observación encontrada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Observación no encontrada")
    })
    public ResponseEntity<ObservacionAnalistaDTO> getObservacionById(
            @Parameter(description = "ID de la observación", example = "1")
            @PathVariable("id") Long id) {
        try {
            ObservacionAnalista observacion = this.service.findById(id);
            return ResponseEntity.ok(ObservacionAnalistaMapper.toDTO(observacion));
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/evaluacion/{idEvaluacion}")
    @Operation(summary = "Obtener observaciones por evaluación", 
               description = "Recupera todas las observaciones de una evaluación específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Observaciones encontradas exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron observaciones para la evaluación")
    })
    public ResponseEntity<List<ObservacionAnalistaDTO>> getObservacionesByEvaluacion(
            @Parameter(description = "ID de la evaluación", example = "1")
            @PathVariable("idEvaluacion") String idEvaluacion) {
        try {
            java.math.BigDecimal evaluacionId = new java.math.BigDecimal(idEvaluacion);
            List<ObservacionAnalista> observaciones = this.service.findByIdEvaluacion(evaluacionId);
            List<ObservacionAnalistaDTO> dtos = new ArrayList<>(observaciones.size());
            
            for (ObservacionAnalista observacion : observaciones) {
                dtos.add(ObservacionAnalistaMapper.toDTO(observacion));
            }
            return ResponseEntity.ok(dtos);
        } catch (NumberFormatException nfe) {
            return ResponseEntity.badRequest().build();
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario/{idUsuario}")
    @Operation(summary = "Obtener observaciones por usuario", 
               description = "Recupera todas las observaciones realizadas por un usuario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Observaciones encontradas exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron observaciones para el usuario")
    })
    public ResponseEntity<List<ObservacionAnalistaDTO>> getObservacionesByUsuario(
            @Parameter(description = "ID del usuario", example = "101")
            @PathVariable("idUsuario") String idUsuario) {
        try {
            java.math.BigDecimal usuarioId = new java.math.BigDecimal(idUsuario);
            List<ObservacionAnalista> observaciones = this.service.findByIdUsuario(usuarioId);
            List<ObservacionAnalistaDTO> dtos = new ArrayList<>(observaciones.size());
            
            for (ObservacionAnalista observacion : observaciones) {
                dtos.add(ObservacionAnalistaMapper.toDTO(observacion));
            }
            return ResponseEntity.ok(dtos);
        } catch (NumberFormatException nfe) {
            return ResponseEntity.badRequest().build();
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/razon/{razon}")
    @Operation(summary = "Obtener observaciones por razón de intervención", 
               description = "Recupera todas las observaciones con una razón específica de intervención")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Observaciones encontradas exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron observaciones con la razón especificada")
    })
    public ResponseEntity<List<ObservacionAnalistaDTO>> getObservacionesByRazon(
            @Parameter(description = "Razón de intervención automática", example = "SCORE_BAJO")
            @PathVariable("razon") String razon) {
        try {
            List<ObservacionAnalista> observaciones = this.service.findByRazonIntervencion(razon);
            List<ObservacionAnalistaDTO> dtos = new ArrayList<>(observaciones.size());
            
            for (ObservacionAnalista observacion : observaciones) {
                dtos.add(ObservacionAnalistaMapper.toDTO(observacion));
            }
            return ResponseEntity.ok(dtos);
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Crear nueva observación", 
               description = "Registra una nueva observación del analista")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Observación creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "422", description = "Error de reglas de negocio")
    })
    public ResponseEntity<ObservacionAnalistaDTO> createObservacion(
            @Parameter(description = "Datos de la observación a crear")
            @Valid @RequestBody ObservacionAnalistaDTO observacionDTO) {
        try {
            ObservacionAnalista observacion = ObservacionAnalistaMapper.toEntity(observacionDTO);
            ObservacionAnalista observacionCreada = this.service.create(observacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(ObservacionAnalistaMapper.toDTO(observacionCreada));
        } catch (CreateException ce) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    @PatchMapping("/{id}/razon-intervencion")
    @Operation(summary = "Actualizar razón de intervención", 
               description = "Actualiza la razón de intervención automática de una observación específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Razón actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Observación no encontrada"),
        @ApiResponse(responseCode = "422", description = "Error al actualizar la razón")
    })
    public ResponseEntity<ObservacionAnalistaDTO> updateRazonIntervencion(
            @Parameter(description = "ID de la observación", example = "1")
            @PathVariable("id") Long id,
            @Parameter(description = "Nueva razón de intervención")
            @RequestParam("razon") String nuevaRazon) {
        try {
            ObservacionAnalista observacion = this.service.findById(id);
            observacion.setRazonIntervencionAutoEnum(nuevaRazon);
            ObservacionAnalista observacionActualizada = this.service.update(observacion);
            return ResponseEntity.ok(ObservacionAnalistaMapper.toDTO(observacionActualizada));
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        } catch (UpdateException ue) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    @PatchMapping("/{id}/justificacion")
    @Operation(summary = "Actualizar justificación", 
               description = "Actualiza la justificación de una observación específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Justificación actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Observación no encontrada"),
        @ApiResponse(responseCode = "422", description = "Error al actualizar la justificación")
    })
    public ResponseEntity<ObservacionAnalistaDTO> updateJustificacion(
            @Parameter(description = "ID de la observación", example = "1")
            @PathVariable("id") Long id,
            @Parameter(description = "Nueva justificación")
            @RequestParam("justificacion") String nuevaJustificacion) {
        try {
            ObservacionAnalista observacion = this.service.findById(id);
            observacion.setJustificacion(nuevaJustificacion);
            ObservacionAnalista observacionActualizada = this.service.update(observacion);
            return ResponseEntity.ok(ObservacionAnalistaMapper.toDTO(observacionActualizada));
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        } catch (UpdateException ue) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    @GetMapping("/evaluacion/{idEvaluacion}/usuario/{idUsuario}")
    @Operation(summary = "Obtener observaciones por evaluación y usuario", 
               description = "Recupera observaciones específicas de una evaluación realizadas por un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Observaciones encontradas exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron observaciones")
    })
    public ResponseEntity<List<ObservacionAnalistaDTO>> getObservacionesByEvaluacionAndUsuario(
            @Parameter(description = "ID de la evaluación", example = "1")
            @PathVariable("idEvaluacion") String idEvaluacion,
            @Parameter(description = "ID del usuario", example = "101")
            @PathVariable("idUsuario") String idUsuario) {
        try {
            java.math.BigDecimal evaluacionId = new java.math.BigDecimal(idEvaluacion);
            java.math.BigDecimal usuarioId = new java.math.BigDecimal(idUsuario);
            List<ObservacionAnalista> observaciones = this.service.findByIdEvaluacionAndIdUsuario(evaluacionId, usuarioId);
            List<ObservacionAnalistaDTO> dtos = new ArrayList<>(observaciones.size());
            
            for (ObservacionAnalista observacion : observaciones) {
                dtos.add(ObservacionAnalistaMapper.toDTO(observacion));
            }
            return ResponseEntity.ok(dtos);
        } catch (NumberFormatException nfe) {
            return ResponseEntity.badRequest().build();
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

    @ExceptionHandler({UpdateException.class})
    public ResponseEntity<Map<String, String>> handleUpdateException(UpdateException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(Map.of(
                    "error", "ERROR_ACTUALIZACION",
                    "mensaje", ex.getMessage()
                ));
    }

    @ExceptionHandler({BusinessRuleException.class})
    public ResponseEntity<Map<String, String>> handleBusinessRuleException(BusinessRuleException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(Map.of(
                    "error", "REGLA_NEGOCIO_VIOLADA",
                    "regla", ex.getRule(),
                    "detalle", ex.getDetail(),
                    "mensaje", ex.getMessage()
                ));
    }
} 