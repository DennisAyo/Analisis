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
    @Operation(summary = "Obtener evaluación crediticia por ID", 
               description = "Recupera una evaluación crediticia específica por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Evaluación encontrada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Evaluación no encontrada")
    })
    public ResponseEntity<EvaluacionCrediticiaDTO> getEvaluacionById(
            @Parameter(description = "ID de la evaluación crediticia", example = "1")
            @PathVariable("id") Long id) {
        try {
            EvaluacionCrediticia evaluacion = this.service.findById(id);
            return ResponseEntity.ok(EvaluacionCrediticiaMapper.toDTO(evaluacion));
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/informe/{idInforme}")
    @Operation(summary = "Obtener evaluaciones por informe de buró", 
               description = "Recupera todas las evaluaciones basadas en un informe de buró específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Evaluaciones encontradas exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron evaluaciones para el informe")
    })
    public ResponseEntity<List<EvaluacionCrediticiaDTO>> getEvaluacionesByInforme(
            @Parameter(description = "ID del informe de buró", example = "1")
            @PathVariable("idInforme") Long idInforme) {
        try {
            List<EvaluacionCrediticia> evaluaciones = this.service.findByInformeBuro(idInforme);
            List<EvaluacionCrediticiaDTO> dtos = new ArrayList<>(evaluaciones.size());
            
            for (EvaluacionCrediticia evaluacion : evaluaciones) {
                dtos.add(EvaluacionCrediticiaMapper.toDTO(evaluacion));
            }
            return ResponseEntity.ok(dtos);
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/score/{scoreInterno}")
    @Operation(summary = "Obtener evaluaciones por score interno", 
               description = "Recupera todas las evaluaciones con un score interno específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Evaluaciones encontradas exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron evaluaciones con el score especificado")
    })
    public ResponseEntity<List<EvaluacionCrediticiaDTO>> getEvaluacionesByScoreInterno(
            @Parameter(description = "Score interno", example = "650.5")
            @PathVariable("scoreInterno") String scoreInterno) {
        try {
            java.math.BigDecimal score = new java.math.BigDecimal(scoreInterno);
            List<EvaluacionCrediticia> evaluaciones = this.service.findByScoreInterno(score);
            List<EvaluacionCrediticiaDTO> dtos = new ArrayList<>(evaluaciones.size());
            
            for (EvaluacionCrediticia evaluacion : evaluaciones) {
                dtos.add(EvaluacionCrediticiaMapper.toDTO(evaluacion));
            }
            return ResponseEntity.ok(dtos);
        } catch (NumberFormatException nfe) {
            return ResponseEntity.badRequest().build();
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Crear nueva evaluación crediticia", 
               description = "Registra una nueva evaluación crediticia")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Evaluación creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "422", description = "Error de reglas de negocio")
    })
    public ResponseEntity<EvaluacionCrediticiaDTO> createEvaluacion(
            @Parameter(description = "Datos de la evaluación crediticia a crear")
            @Valid @RequestBody EvaluacionCrediticiaDTO evaluacionDTO) {
        try {
            EvaluacionCrediticia evaluacion = EvaluacionCrediticiaMapper.toEntity(evaluacionDTO);
            EvaluacionCrediticia evaluacionCreada = this.service.create(evaluacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(EvaluacionCrediticiaMapper.toDTO(evaluacionCreada));
        } catch (CreateException ce) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    @PatchMapping("/{id}/score-interno")
    @Operation(summary = "Actualizar score interno calculado", 
               description = "Actualiza el score interno calculado de una evaluación crediticia específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Score actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Evaluación no encontrada"),
        @ApiResponse(responseCode = "422", description = "Error al actualizar el score")
    })
    public ResponseEntity<EvaluacionCrediticiaDTO> updateScoreInterno(
            @Parameter(description = "ID de la evaluación", example = "1")
            @PathVariable("id") Long id,
            @Parameter(description = "Nuevo score interno")
            @RequestParam("scoreInterno") String nuevoScore) {
        try {
            EvaluacionCrediticia evaluacion = this.service.findById(id);
            evaluacion.setScoreInternoCalculado(new java.math.BigDecimal(nuevoScore));
            EvaluacionCrediticia evaluacionActualizada = this.service.update(evaluacion);
            return ResponseEntity.ok(EvaluacionCrediticiaMapper.toDTO(evaluacionActualizada));
        } catch (NumberFormatException nfe) {
            return ResponseEntity.badRequest().build();
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        } catch (UpdateException ue) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    @PatchMapping("/{id}/decision-analista")
    @Operation(summary = "Actualizar decisión final del analista", 
               description = "Actualiza la decisión final del analista para una evaluación específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Decisión actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Evaluación no encontrada"),
        @ApiResponse(responseCode = "422", description = "Error al actualizar la decisión")
    })
    public ResponseEntity<EvaluacionCrediticiaDTO> updateDecisionAnalista(
            @Parameter(description = "ID de la evaluación", example = "1")
            @PathVariable("id") Long id,
            @Parameter(description = "Decisión final del analista")
            @RequestParam("decision") String decisionAnalista,
            @Parameter(description = "Justificación de la decisión", required = false)
            @RequestParam(value = "justificacion", required = false) String justificacion) {
        try {
            EvaluacionCrediticia evaluacion = this.service.findById(id);
            evaluacion.setDecisionFinalAnalista(decisionAnalista);
            if (justificacion != null) {
                evaluacion.setJustificacionAnalista(justificacion);
            }
            EvaluacionCrediticia evaluacionActualizada = this.service.update(evaluacion);
            return ResponseEntity.ok(EvaluacionCrediticiaMapper.toDTO(evaluacionActualizada));
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        } catch (UpdateException ue) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    @GetMapping("/solicitud/{idSolicitud}/ultima")
    @Operation(summary = "Obtener última evaluación de una solicitud", 
               description = "Recupera la evaluación más reciente de una solicitud específica")
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

    @GetMapping("/decision-analista/{decision}")
    @Operation(summary = "Obtener evaluaciones por decisión del analista", 
               description = "Recupera todas las evaluaciones con una decisión específica del analista")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Evaluaciones encontradas exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron evaluaciones con la decisión especificada")
    })
    public ResponseEntity<List<EvaluacionCrediticiaDTO>> getEvaluacionesByDecisionAnalista(
            @Parameter(description = "Decisión del analista", example = "APROBADO")
            @PathVariable("decision") String decision) {
        try {
            List<EvaluacionCrediticia> evaluaciones = this.service.findByDecisionAnalista(decision);
            List<EvaluacionCrediticiaDTO> dtos = new ArrayList<>(evaluaciones.size());
            
            for (EvaluacionCrediticia evaluacion : evaluaciones) {
                dtos.add(EvaluacionCrediticiaMapper.toDTO(evaluacion));
            }
            return ResponseEntity.ok(dtos);
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/solicitud/{idSolicitud}")
    @Operation(summary = "Obtener evaluaciones por solicitud", 
               description = "Recupera todas las evaluaciones de una solicitud específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Evaluaciones encontradas exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron evaluaciones para la solicitud")
    })
    public ResponseEntity<List<EvaluacionCrediticiaDTO>> getEvaluacionesBySolicitud(
            @Parameter(description = "ID de la solicitud", example = "12345")
            @PathVariable("idSolicitud") Integer idSolicitud) {
        try {
            List<EvaluacionCrediticia> evaluaciones = this.service.findByIdSolicitud(idSolicitud);
            List<EvaluacionCrediticiaDTO> dtos = new ArrayList<>(evaluaciones.size());
            
            for (EvaluacionCrediticia evaluacion : evaluaciones) {
                dtos.add(EvaluacionCrediticiaMapper.toDTO(evaluacion));
            }
            return ResponseEntity.ok(dtos);
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/resultado-automatico/{resultado}")
    @Operation(summary = "Obtener evaluaciones por resultado automático", 
               description = "Recupera todas las evaluaciones con un resultado automático específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Evaluaciones encontradas exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron evaluaciones con el resultado especificado")
    })
    public ResponseEntity<List<EvaluacionCrediticiaDTO>> getEvaluacionesByResultadoAutomatico(
            @Parameter(description = "Resultado automático", example = "APROBADO")
            @PathVariable("resultado") String resultado) {
        try {
            List<EvaluacionCrediticia> evaluaciones = this.service.findByResultadoAutomatico(resultado);
            List<EvaluacionCrediticiaDTO> dtos = new ArrayList<>(evaluaciones.size());
            
            for (EvaluacionCrediticia evaluacion : evaluaciones) {
                dtos.add(EvaluacionCrediticiaMapper.toDTO(evaluacion));
            }
            return ResponseEntity.ok(dtos);
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/automatica")
    @Operation(summary = "Crear evaluación automática", 
               description = "Crea una evaluación crediticia automática basada en un informe de buró")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Evaluación automática creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "422", description = "Error de reglas de negocio")
    })
    public ResponseEntity<EvaluacionCrediticiaDTO> createEvaluacionAutomatica(
            @Parameter(description = "ID de la solicitud", example = "12345")
            @RequestParam("idSolicitud") Integer idSolicitud,
            @Parameter(description = "ID del informe de buró", example = "1")
            @RequestParam("idInformeBuro") Long idInformeBuro) {
        try {
            EvaluacionCrediticia evaluacion = this.service.createAutomaticEvaluation(idSolicitud, idInformeBuro);
            return ResponseEntity.status(HttpStatus.CREATED).body(EvaluacionCrediticiaMapper.toDTO(evaluacion));
        } catch (CreateException ce) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
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