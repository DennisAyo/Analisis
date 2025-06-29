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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.banquito.originacion.analisis.controller.dto.ReglaEvaluacionCrediticiaDTO;
import com.banquito.originacion.analisis.controller.mapper.ReglaEvaluacionCrediticiaMapper;
import com.banquito.originacion.analisis.exception.CreateException;
import com.banquito.originacion.analisis.exception.NotFoundException;
import com.banquito.originacion.analisis.exception.UpdateException;
import com.banquito.originacion.analisis.model.ReglaEvaluacionCrediticia;
import com.banquito.originacion.analisis.service.ReglaEvaluacionCrediticiaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/reglas-evaluacion")
@Tag(name = "Reglas de Evaluación", description = "Gestión de reglas para evaluación crediticia")
public class ReglaEvaluacionCrediticiaController {

    private final ReglaEvaluacionCrediticiaService service;

    public ReglaEvaluacionCrediticiaController(ReglaEvaluacionCrediticiaService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener regla por ID", 
               description = "Recupera una regla de evaluación específica por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Regla encontrada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Regla no encontrada")
    })
    public ResponseEntity<ReglaEvaluacionCrediticiaDTO> getReglaById(
            @Parameter(description = "ID de la regla", example = "1")
            @PathVariable("id") Long id) {
        try {
            ReglaEvaluacionCrediticia regla = this.service.findById(id);
            return ResponseEntity.ok(ReglaEvaluacionCrediticiaMapper.toDTO(regla));
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/activas")
    @Operation(summary = "Obtener reglas activas", 
               description = "Recupera todas las reglas activas ordenadas por prioridad")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reglas activas encontradas exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron reglas activas")
    })
    public ResponseEntity<List<ReglaEvaluacionCrediticiaDTO>> getReglasActivas() {
        try {
            List<ReglaEvaluacionCrediticia> reglas = this.service.findActive();
            List<ReglaEvaluacionCrediticiaDTO> dtos = new ArrayList<>(reglas.size());
            
            for (ReglaEvaluacionCrediticia regla : reglas) {
                dtos.add(ReglaEvaluacionCrediticiaMapper.toDTO(regla));
            }
            return ResponseEntity.ok(dtos);
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(summary = "Buscar reglas por nombre", 
               description = "Busca reglas que contengan el texto especificado en su nombre")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron reglas con el nombre especificado")
    })
    public ResponseEntity<List<ReglaEvaluacionCrediticiaDTO>> searchReglasByNombre(
            @Parameter(description = "Texto a buscar en el nombre de la regla", example = "ingresos")
            @RequestParam("nombre") String nombre) {
        try {
            List<ReglaEvaluacionCrediticia> reglas = this.service.findByNombre(nombre);
            List<ReglaEvaluacionCrediticiaDTO> dtos = new ArrayList<>(reglas.size());
            
            for (ReglaEvaluacionCrediticia regla : reglas) {
                dtos.add(ReglaEvaluacionCrediticiaMapper.toDTO(regla));
            }
            return ResponseEntity.ok(dtos);
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/prioridad")
    @Operation(summary = "Obtener reglas por rango de prioridad", 
               description = "Recupera reglas activas dentro del rango de prioridad especificado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reglas encontradas exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron reglas en el rango especificado")
    })
    public ResponseEntity<List<ReglaEvaluacionCrediticiaDTO>> getReglasByPrioridadRange(
            @Parameter(description = "Prioridad mínima", example = "1")
            @RequestParam("min") Integer prioridadMinima,
            @Parameter(description = "Prioridad máxima", example = "5")
            @RequestParam("max") Integer prioridadMaxima) {
        try {
            List<ReglaEvaluacionCrediticia> reglas = this.service.findByPrioridadRange(prioridadMinima, prioridadMaxima);
            List<ReglaEvaluacionCrediticiaDTO> dtos = new ArrayList<>(reglas.size());
            
            for (ReglaEvaluacionCrediticia regla : reglas) {
                dtos.add(ReglaEvaluacionCrediticiaMapper.toDTO(regla));
            }
            return ResponseEntity.ok(dtos);
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Crear nueva regla", 
               description = "Registra una nueva regla de evaluación crediticia")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Regla creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "422", description = "Error de reglas de negocio")
    })
    public ResponseEntity<ReglaEvaluacionCrediticiaDTO> createRegla(
            @Parameter(description = "Datos de la regla a crear")
            @Valid @RequestBody ReglaEvaluacionCrediticiaDTO reglaDTO) {
        try {
            ReglaEvaluacionCrediticia regla = ReglaEvaluacionCrediticiaMapper.toEntity(reglaDTO);
            ReglaEvaluacionCrediticia reglaCreada = this.service.create(regla);
            return ResponseEntity.status(HttpStatus.CREATED).body(ReglaEvaluacionCrediticiaMapper.toDTO(reglaCreada));
        } catch (CreateException ce) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar regla completa", 
               description = "Actualiza todos los campos de una regla de evaluación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Regla actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Regla no encontrada"),
        @ApiResponse(responseCode = "422", description = "Error al actualizar la regla")
    })
    public ResponseEntity<ReglaEvaluacionCrediticiaDTO> updateRegla(
            @Parameter(description = "ID de la regla", example = "1")
            @PathVariable("id") Long id,
            @Parameter(description = "Datos actualizados de la regla")
            @Valid @RequestBody ReglaEvaluacionCrediticiaDTO reglaDTO) {
        try {
            reglaDTO.setIdRegla(id);
            ReglaEvaluacionCrediticia regla = ReglaEvaluacionCrediticiaMapper.toEntity(reglaDTO);
            ReglaEvaluacionCrediticia reglaActualizada = this.service.update(regla);
            return ResponseEntity.ok(ReglaEvaluacionCrediticiaMapper.toDTO(reglaActualizada));
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        } catch (UpdateException ue) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    @PatchMapping("/{id}/toggle-activa")
    @Operation(summary = "Activar/Desactivar regla", 
               description = "Cambia el estado activo de una regla de evaluación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado de la regla actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Regla no encontrada"),
        @ApiResponse(responseCode = "422", description = "Error al cambiar estado de la regla")
    })
    public ResponseEntity<ReglaEvaluacionCrediticiaDTO> toggleActivaRegla(
            @Parameter(description = "ID de la regla", example = "1")
            @PathVariable("id") Long id) {
        try {
            ReglaEvaluacionCrediticia reglaActualizada = this.service.toggleActive(id);
            return ResponseEntity.ok(ReglaEvaluacionCrediticiaMapper.toDTO(reglaActualizada));
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        } catch (UpdateException ue) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    @PatchMapping("/{id}/prioridad")
    @Operation(summary = "Actualizar prioridad de regla", 
               description = "Actualiza únicamente la prioridad de una regla")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Prioridad actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Regla no encontrada"),
        @ApiResponse(responseCode = "422", description = "Error al actualizar la prioridad")
    })
    public ResponseEntity<ReglaEvaluacionCrediticiaDTO> updatePrioridadRegla(
            @Parameter(description = "ID de la regla", example = "1")
            @PathVariable("id") Long id,
            @Parameter(description = "Nueva prioridad de la regla", example = "5")
            @RequestParam("prioridad") Integer nuevaPrioridad) {
        try {
            ReglaEvaluacionCrediticia regla = this.service.findById(id);
            regla.setPrioridad(nuevaPrioridad);
            ReglaEvaluacionCrediticia reglaActualizada = this.service.update(regla);
            return ResponseEntity.ok(ReglaEvaluacionCrediticiaMapper.toDTO(reglaActualizada));
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        } catch (UpdateException ue) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    @GetMapping("/ejecucion")
    @Operation(summary = "Obtener reglas para ejecución", 
               description = "Recupera reglas activas ordenadas por prioridad para su ejecución")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reglas para ejecución obtenidas exitosamente")
    })
    public ResponseEntity<List<ReglaEvaluacionCrediticiaDTO>> getReglasForExecution() {
        List<ReglaEvaluacionCrediticia> reglas = this.service.getReglasForExecution();
        List<ReglaEvaluacionCrediticiaDTO> dtos = new ArrayList<>(reglas.size());
        
        for (ReglaEvaluacionCrediticia regla : reglas) {
            dtos.add(ReglaEvaluacionCrediticiaMapper.toDTO(regla));
        }
        return ResponseEntity.ok(dtos);
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