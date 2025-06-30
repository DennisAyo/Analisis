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
import org.springframework.web.bind.annotation.RestController;

import com.banquito.originacion.analisis.controller.dto.InformeBuroDTO;
import com.banquito.originacion.analisis.controller.mapper.InformeBuroMapper;
import com.banquito.originacion.analisis.exception.CreateException;
import com.banquito.originacion.analisis.exception.NotFoundException;
import com.banquito.originacion.analisis.exception.UpdateException;
import com.banquito.originacion.analisis.model.InformeBuro;
import com.banquito.originacion.analisis.service.InformeBuroService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/informes-buro")
@Tag(name = "Informes de Buró", description = "Gestión de informes de buró crediticio")
public class InformeBuroController {

    private final InformeBuroService service;

    public InformeBuroController(InformeBuroService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener informe por ID", 
               description = "Recupera un informe de buró específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Informe encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Informe no encontrado")
    })
    public ResponseEntity<InformeBuroDTO> getInformeById(
            @Parameter(description = "ID del informe de buró", example = "1")
            @PathVariable("id") Long id) {
        try {
            InformeBuro informe = this.service.findById(id);
            return ResponseEntity.ok(InformeBuroMapper.toDTO(informe));
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/consulta/{idConsulta}")
    @Operation(summary = "Obtener informe por consulta", 
               description = "Recupera el informe asociado a una consulta específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Informe encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontró informe para la consulta")
    })
    public ResponseEntity<InformeBuroDTO> getInformeByConsulta(
            @Parameter(description = "ID de la consulta de buró", example = "1")
            @PathVariable("idConsulta") Long idConsulta) {
        try {
            InformeBuro informe = this.service.findByConsultaBuro(idConsulta);
            return ResponseEntity.ok(InformeBuroMapper.toDTO(informe));
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/consulta/{idConsulta}/ultimo")
    @Operation(summary = "Obtener último informe de una consulta", 
               description = "Recupera el informe más reciente de una consulta específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Último informe encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron informes para la consulta")
    })
    public ResponseEntity<InformeBuroDTO> getUltimoInformeByConsulta(
            @Parameter(description = "ID de la consulta de buró", example = "1")
            @PathVariable("idConsulta") Long idConsulta) {
        try {
            InformeBuro informe = this.service.findLatestByConsultaBuro(idConsulta);
            return ResponseEntity.ok(InformeBuroMapper.toDTO(informe));
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Crear nuevo informe de buró", 
               description = "Registra un nuevo informe de buró")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Informe creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "422", description = "Error de reglas de negocio")
    })
    public ResponseEntity<InformeBuroDTO> createInforme(
            @Parameter(description = "Datos del informe a crear")
            @Valid @RequestBody InformeBuroDTO informeDTO) {
        try {
            InformeBuro informe = InformeBuroMapper.toEntity(informeDTO);
            InformeBuro informeCreado = this.service.create(informe);
            return ResponseEntity.status(HttpStatus.CREATED).body(InformeBuroMapper.toDTO(informeCreado));
        } catch (CreateException ce) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    @PatchMapping("/{id}/json-respuesta")
    @Operation(summary = "Actualizar JSON de respuesta", 
               description = "Actualiza el JSON de respuesta completa de un informe")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "JSON actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Informe no encontrado"),
        @ApiResponse(responseCode = "422", description = "Error al actualizar el JSON")
    })
    public ResponseEntity<InformeBuroDTO> updateJsonRespuesta(
            @Parameter(description = "ID del informe", example = "1")
            @PathVariable("id") Long id,
            @Parameter(description = "JSON de respuesta actualizado")
            @RequestBody String jsonRespuesta) {
        try {
            InformeBuro informeActualizado = this.service.updateJsonRespuesta(id, jsonRespuesta);
            return ResponseEntity.ok(InformeBuroMapper.toDTO(informeActualizado));
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        } catch (UpdateException ue) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    @GetMapping("/score/{score}/minimo")
    @Operation(summary = "Obtener informes por score mínimo", 
               description = "Recupera informes con score mayor o igual al especificado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Informes encontrados exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron informes con el score especificado")
    })
    public ResponseEntity<List<InformeBuroDTO>> getInformesByScoreMinimo(
            @Parameter(description = "Score mínimo", example = "650")
            @PathVariable("score") String scoreMinimo) {
        try {
            java.math.BigDecimal score = new java.math.BigDecimal(scoreMinimo);
            List<InformeBuro> informes = this.service.findByMinScore(score);
            List<InformeBuroDTO> dtos = new ArrayList<>(informes.size());
            
            for (InformeBuro informe : informes) {
                dtos.add(InformeBuroMapper.toDTO(informe));
            }
            return ResponseEntity.ok(dtos);
        } catch (NumberFormatException nfe) {
            return ResponseEntity.badRequest().build();
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/deudas/{numeroDeudas}")
    @Operation(summary = "Obtener informes por número de deudas", 
               description = "Recupera informes que tengan exactamente el número de deudas impagas especificado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Informes encontrados exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron informes con ese número de deudas")
    })
    public ResponseEntity<List<InformeBuroDTO>> getInformesByDeudaImpagas(
            @Parameter(description = "Número de deudas impagas", example = "2")
            @PathVariable("numeroDeudas") Integer numeroDeudas) {
        try {
            List<InformeBuro> informes = this.service.findByDeudaImpagas(numeroDeudas);
            List<InformeBuroDTO> dtos = new ArrayList<>(informes.size());
            
            for (InformeBuro informe : informes) {
                dtos.add(InformeBuroMapper.toDTO(informe));
            }
            return ResponseEntity.ok(dtos);
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