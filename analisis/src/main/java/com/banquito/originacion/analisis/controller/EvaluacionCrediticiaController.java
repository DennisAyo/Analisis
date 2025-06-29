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

import com.banquito.originacion.analisis.controller.dto.EvaluacionCrediticiaDTO;
import com.banquito.originacion.analisis.controller.mapper.EvaluacionCrediticiaMapper;
import com.banquito.originacion.analisis.exception.CreateException;
import com.banquito.originacion.analisis.exception.NotFoundException;
import com.banquito.originacion.analisis.exception.UpdateException;
import com.banquito.originacion.analisis.model.EvaluacionCrediticia;
import com.banquito.originacion.analisis.service.EvaluacionCrediticiaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/evaluaciones-crediticias")
@Tag(name = "Evaluaciones Crediticias", description = "Gestión de evaluaciones crediticias")
public class EvaluacionCrediticiaController {

    private final EvaluacionCrediticiaService service;

    public EvaluacionCrediticiaController(EvaluacionCrediticiaService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener evaluación por ID", 
               description = "Recupera una evaluación crediticia específica por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Evaluación encontrada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Evaluación no encontrada")
    })
    public ResponseEntity<EvaluacionCrediticiaDTO> getEvaluacionById(
            @Parameter(description = "ID de la evaluación", example = "1")
            @PathVariable("id") Long id) {
        try {
            EvaluacionCrediticia evaluacion = this.service.findById(id);
            return ResponseEntity.ok(EvaluacionCrediticiaMapper.toDTO(evaluacion));
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/solicitud/{idSolicitud}")
    @Operation(summary = "Obtener evaluación por solicitud", 
               description = "Recupera la evaluación crediticia asociada a una solicitud")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Evaluación encontrada exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontró evaluación para la solicitud")
    })
    public ResponseEntity<EvaluacionCrediticiaDTO> getEvaluacionBySolicitud(
            @Parameter(description = "ID de la solicitud", example = "12345")
            @PathVariable("idSolicitud") Integer idSolicitud) {
        try {
            EvaluacionCrediticia evaluacion = this.service.findLastByIdSolicitud(idSolicitud);
            return ResponseEntity.ok(EvaluacionCrediticiaMapper.toDTO(evaluacion));
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/solicitud/{idSolicitud}/ultima")
    @Operation(summary = "Obtener última evaluación de una solicitud", 
               description = "Recupera la evaluación crediticia más reciente de una solicitud")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Última evaluación encontrada exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron evaluaciones para la solicitud")
    })
    public ResponseEntity<EvaluacionCrediticiaDTO> getUltimaEvaluacionBySolicitud(
            @Parameter(description = "ID de la solicitud", example = "12345")
            @PathVariable("idSolicitud") Integer idSolicitud) {
        try {
            EvaluacionCrediticia evaluacion = this.service.findLastByIdSolicitud(idSolicitud);
            return ResponseEntity.ok(EvaluacionCrediticiaMapper.toDTO(evaluacion));
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/categoria-riesgo/{categoria}")
    @Operation(summary = "Obtener evaluaciones por categoría de riesgo", 
               description = "Recupera todas las evaluaciones con una categoría de riesgo específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Evaluaciones encontradas exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron evaluaciones con la categoría especificada")
    })
    public ResponseEntity<List<EvaluacionCrediticiaDTO>> getEvaluacionesByCategoriaRiesgo(
            @Parameter(description = "Categoría de riesgo", example = "MEDIO")
            @PathVariable("categoria") String categoriaRiesgo) {
        try {
            List<EvaluacionCrediticia> evaluaciones = this.service.findByCategoriaRiesgo(categoriaRiesgo);
            List<EvaluacionCrediticiaDTO> dtos = new ArrayList<>(evaluaciones.size());
            
            for (EvaluacionCrediticia evaluacion : evaluaciones) {
                dtos.add(EvaluacionCrediticiaMapper.toDTO(evaluacion));
            }
            return ResponseEntity.ok(dtos);
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/tipo/{esAutomatico}")
    @Operation(summary = "Obtener evaluaciones por tipo", 
               description = "Recupera evaluaciones automáticas o manuales")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Evaluaciones encontradas exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron evaluaciones del tipo especificado")
    })
    public ResponseEntity<List<EvaluacionCrediticiaDTO>> getEvaluacionesByTipo(
            @Parameter(description = "Tipo de evaluación (true=automática, false=manual)", example = "true")
            @PathVariable("esAutomatico") Boolean esAutomatico) {
        try {
            List<EvaluacionCrediticia> evaluaciones = this.service.findByTipoEvaluacion(esAutomatico);
            List<EvaluacionCrediticiaDTO> dtos = new ArrayList<>(evaluaciones.size());
            
            for (EvaluacionCrediticia evaluacion : evaluaciones) {
                dtos.add(EvaluacionCrediticiaMapper.toDTO(evaluacion));
            }
            return ResponseEntity.ok(dtos);
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Crear evaluación crediticia manual", 
               description = "Registra una nueva evaluación crediticia de forma manual")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Evaluación creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "422", description = "Error de reglas de negocio")
    })
    public ResponseEntity<EvaluacionCrediticiaDTO> createEvaluacionManual(
            @Parameter(description = "Datos de la evaluación a crear")
            @Valid @RequestBody EvaluacionCrediticiaDTO evaluacionDTO) {
        try {
            EvaluacionCrediticia evaluacion = EvaluacionCrediticiaMapper.toEntity(evaluacionDTO);
            EvaluacionCrediticia evaluacionCreada = this.service.create(evaluacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(EvaluacionCrediticiaMapper.toDTO(evaluacionCreada));
        } catch (CreateException ce) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    @PostMapping("/automatica")
    @Operation(summary = "Crear evaluación automática", 
               description = "Crea una evaluación crediticia automática basada en una consulta de buró")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Evaluación automática creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "422", description = "Error de reglas de negocio")
    })
    public ResponseEntity<EvaluacionCrediticiaDTO> createEvaluacionAutomatica(
            @Parameter(description = "ID de la solicitud", example = "12345")
            @RequestParam("idSolicitud") Integer idSolicitud,
            @Parameter(description = "ID de la consulta de buró", example = "1")
            @RequestParam("idConsultaBuro") Long idConsultaBuro) {
        try {
            EvaluacionCrediticia evaluacion = this.service.createAutomaticEvaluation(idSolicitud, idConsultaBuro);
            return ResponseEntity.status(HttpStatus.CREATED).body(EvaluacionCrediticiaMapper.toDTO(evaluacion));
        } catch (CreateException ce) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    @PatchMapping("/{id}/categoria-riesgo")
    @Operation(summary = "Actualizar categoría de riesgo", 
               description = "Actualiza la categoría de riesgo de una evaluación crediticia")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Evaluación no encontrada"),
        @ApiResponse(responseCode = "422", description = "Error al actualizar categoría")
    })
    public ResponseEntity<EvaluacionCrediticiaDTO> updateCategoriaRiesgo(
            @Parameter(description = "ID de la evaluación", example = "1")
            @PathVariable("id") Long id,
            @Parameter(description = "Nueva categoría de riesgo")
            @RequestParam("categoria") String nuevaCategoria) {
        try {
            EvaluacionCrediticia evaluacion = this.service.findById(id);
            evaluacion.setCategoriaRiesgo(nuevaCategoria);
            EvaluacionCrediticia evaluacionActualizada = this.service.update(evaluacion);
            return ResponseEntity.ok(EvaluacionCrediticiaMapper.toDTO(evaluacionActualizada));
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        } catch (UpdateException ue) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    @GetMapping("/score/{score}/categoria")
    @Operation(summary = "Calcular categoría de riesgo por score", 
               description = "Calcula la categoría de riesgo basada en un score específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría calculada exitosamente")
    })
    public ResponseEntity<Map<String, String>> calculateRiskCategory(
            @Parameter(description = "Score crediticio", example = "680")
            @PathVariable("score") String score) {
        try {
            java.math.BigDecimal scoreValue = new java.math.BigDecimal(score);
            String categoria = this.service.calculateRiskCategory(scoreValue);
            return ResponseEntity.ok(Map.of(
                "score", score,
                "categoriaRiesgo", categoria
            ));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "SCORE_INVALIDO",
                "mensaje", "El score debe ser un número válido"
            ));
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
} 