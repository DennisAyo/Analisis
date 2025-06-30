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

import com.banquito.originacion.analisis.controller.dto.ConsultaBuroDTO;
import com.banquito.originacion.analisis.controller.mapper.ConsultaBuroMapper;
import com.banquito.originacion.analisis.exception.CreateException;
import com.banquito.originacion.analisis.exception.NotFoundException;
import com.banquito.originacion.analisis.exception.UpdateException;
import com.banquito.originacion.analisis.model.ConsultaBuro;
import com.banquito.originacion.analisis.service.ConsultaBuroService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/consultas-buro")
@Tag(name = "Consultas de Buró", description = "Gestión de consultas de buró crediticio")
public class ConsultaBuroController {

    private final ConsultaBuroService service;

    public ConsultaBuroController(ConsultaBuroService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener consulta de buró por ID", 
               description = "Recupera una consulta de buró específica por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Consulta encontrada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Consulta no encontrada")
    })
    public ResponseEntity<ConsultaBuroDTO> getConsultaBuroById(
            @Parameter(description = "ID de la consulta de buró", example = "1")
            @PathVariable("id") Long id) {
        try {
            ConsultaBuro consulta = this.service.findById(id);
            return ResponseEntity.ok(ConsultaBuroMapper.toDTO(consulta));
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/solicitud/{idSolicitud}")
    @Operation(summary = "Obtener consultas por solicitud", 
               description = "Recupera todas las consultas de buró de una solicitud específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Consultas encontradas exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron consultas para la solicitud")
    })
    public ResponseEntity<List<ConsultaBuroDTO>> getConsultasBySolicitud(
            @Parameter(description = "ID de la solicitud", example = "12345")
            @PathVariable("idSolicitud") Integer idSolicitud) {
        try {
            List<ConsultaBuro> consultas = this.service.findByIdSolicitud(idSolicitud);
            List<ConsultaBuroDTO> dtos = new ArrayList<>(consultas.size());
            
            for (ConsultaBuro consulta : consultas) {
                dtos.add(ConsultaBuroMapper.toDTO(consulta));
            }
            return ResponseEntity.ok(dtos);
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/solicitud/{idSolicitud}/ultima")
    @Operation(summary = "Obtener última consulta de una solicitud", 
               description = "Recupera la consulta de buró más reciente de una solicitud")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Última consulta encontrada exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron consultas para la solicitud")
    })
    public ResponseEntity<ConsultaBuroDTO> getUltimaConsultaBySolicitud(
            @Parameter(description = "ID de la solicitud", example = "12345")
            @PathVariable("idSolicitud") Integer idSolicitud) {
        try {
            ConsultaBuro consulta = this.service.findLastByIdSolicitud(idSolicitud);
            return ResponseEntity.ok(ConsultaBuroMapper.toDTO(consulta));
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Obtener consultas por estado", 
               description = "Recupera todas las consultas con un estado específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Consultas encontradas exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron consultas con el estado especificado")
    })
    public ResponseEntity<List<ConsultaBuroDTO>> getConsultasByEstado(
            @Parameter(description = "Estado de la consulta", example = "EXITOSA")
            @PathVariable("estado") String estado) {
        try {
            List<ConsultaBuro> consultas = this.service.findByEstadoConsulta(estado);
            List<ConsultaBuroDTO> dtos = new ArrayList<>(consultas.size());
            
            for (ConsultaBuro consulta : consultas) {
                dtos.add(ConsultaBuroMapper.toDTO(consulta));
            }
            return ResponseEntity.ok(dtos);
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Crear nueva consulta de buró", 
               description = "Registra una nueva consulta de buró crediticio")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Consulta creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "422", description = "Error de reglas de negocio")
    })
    public ResponseEntity<ConsultaBuroDTO> createConsultaBuro(
            @Parameter(description = "Datos de la consulta de buró a crear")
            @Valid @RequestBody ConsultaBuroDTO consultaBuroDTO) {
        try {
            ConsultaBuro consulta = ConsultaBuroMapper.toEntity(consultaBuroDTO);
            ConsultaBuro consultaCreada = this.service.create(consulta);
            return ResponseEntity.status(HttpStatus.CREATED).body(ConsultaBuroMapper.toDTO(consultaCreada));
        } catch (CreateException ce) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado de consulta", 
               description = "Actualiza el estado de una consulta de buró específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Consulta no encontrada"),
        @ApiResponse(responseCode = "422", description = "Error al actualizar el estado")
    })
    public ResponseEntity<ConsultaBuroDTO> updateEstadoConsulta(
            @Parameter(description = "ID de la consulta", example = "1")
            @PathVariable("id") Long id,
            @Parameter(description = "Nuevo estado de la consulta")
            @RequestParam("estado") String nuevoEstado) {
        try {
            ConsultaBuro consulta = this.service.findById(id);
            consulta.setEstadoConsulta(nuevoEstado);
            ConsultaBuro consultaActualizada = this.service.update(consulta);
            return ResponseEntity.ok(ConsultaBuroMapper.toDTO(consultaActualizada));
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        } catch (UpdateException ue) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    @GetMapping("/solicitud/{idSolicitud}/score-mejor")
    @Operation(summary = "Obtener mejor score de una solicitud", 
               description = "Recupera el mejor score crediticio obtenido para una solicitud")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Score obtenido exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron consultas para la solicitud")
    })
    public ResponseEntity<Map<String, Object>> getBestScore(
            @Parameter(description = "ID de la solicitud", example = "12345")
            @PathVariable("idSolicitud") Integer idSolicitud) {
        try {
            var bestScore = this.service.getBestScore(idSolicitud);
            return ResponseEntity.ok(Map.of(
                "idSolicitud", idSolicitud,
                "mejorScore", bestScore,
                "tieneConsultasExitosas", this.service.hasSuccessfulQueries(idSolicitud)
            ));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/solicitud/{idSolicitud}/mejor-consulta")
    @Operation(summary = "Obtener consulta con mejor score", 
               description = "Recupera la consulta con mejor score para una solicitud")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Consulta con mejor score encontrada"),
        @ApiResponse(responseCode = "404", description = "No se encontraron consultas con score para la solicitud")
    })
    public ResponseEntity<ConsultaBuroDTO> getBestScoreConsulta(
            @Parameter(description = "ID de la solicitud", example = "12345")
            @PathVariable("idSolicitud") Integer idSolicitud) {
        try {
            ConsultaBuro consulta = this.service.findByIdSolicitudWithBestScore(idSolicitud);
            return ResponseEntity.ok(ConsultaBuroMapper.toDTO(consulta));
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
} 