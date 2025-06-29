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

import com.banquito.originacion.analisis.controller.dto.HistorialEstadoDTO;
import com.banquito.originacion.analisis.controller.mapper.HistorialEstadoMapper;
import com.banquito.originacion.analisis.exception.BusinessRuleException;
import com.banquito.originacion.analisis.exception.CreateException;
import com.banquito.originacion.analisis.exception.NotFoundException;
import com.banquito.originacion.analisis.exception.UpdateException;
import com.banquito.originacion.analisis.model.HistorialEstado;
import com.banquito.originacion.analisis.service.AnalisisBusinessRuleService;
import com.banquito.originacion.analisis.service.HistorialEstadoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/historiales-estados")
@Tag(name = "Historial Estados", description = "API para gestionar el historial de estados de las solicitudes")
public class HistorialEstadoController {

    private final HistorialEstadoService service;
    private final HistorialEstadoMapper mapper;
    private final AnalisisBusinessRuleService businessRuleService;

    public HistorialEstadoController(HistorialEstadoService service, HistorialEstadoMapper mapper, 
                                   AnalisisBusinessRuleService businessRuleService) {
        this.service = service;
        this.mapper = mapper;
        this.businessRuleService = businessRuleService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los historiales de estados", 
               description = "Retorna una lista de todos los historiales de estados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de historiales obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron historiales")
    })
    public ResponseEntity<List<HistorialEstadoDTO>> getAllHistorialEstados(
            @Parameter(description = "Filtrar por estado") @RequestParam(required = false) String estado,
            @Parameter(description = "Filtrar por usuario") @RequestParam(required = false) String usuario) {
        
        List<HistorialEstado> historiales;
        
        if (estado != null) {
            historiales = this.service.findByEstado(estado);
        } else if (usuario != null) {
            historiales = this.service.findByUsuario(usuario);
        } else {
            historiales = this.service.findAll();
        }
        
        List<HistorialEstadoDTO> dtos = new ArrayList<>(historiales.size());
        for (HistorialEstado historial : historiales) {
            dtos.add(mapper.toDTO(historial));
        }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener historial de estado por ID", 
               description = "Retorna un historial de estado específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historial encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Historial no encontrado")
    })
    public ResponseEntity<HistorialEstadoDTO> getHistorialEstadoById(
            @Parameter(description = "ID del historial de estado") @PathVariable("id") Long id) {
        HistorialEstado historial = this.service.findById(id);
        return ResponseEntity.ok(this.mapper.toDTO(historial));
    }

    @GetMapping("/solicitudes/{idSolicitud}")
    @Operation(summary = "Obtener historial de estados por solicitud", 
               description = "Retorna todos los historiales de estados de una solicitud específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historiales encontrados exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron historiales para la solicitud")
    })
    public ResponseEntity<List<HistorialEstadoDTO>> getHistorialEstadosBySolicitud(
            @Parameter(description = "ID de la solicitud") @PathVariable("idSolicitud") Integer idSolicitud) {
        List<HistorialEstado> historiales = this.service.findByIdSolicitud(idSolicitud);
        List<HistorialEstadoDTO> dtos = new ArrayList<>(historiales.size());
        
        for (HistorialEstado historial : historiales) {
            dtos.add(mapper.toDTO(historial));
        }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/solicitudes/{idSolicitud}/ultimo")
    @Operation(summary = "Obtener último estado de una solicitud", 
               description = "Retorna el último estado registrado de una solicitud específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Último estado encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontró historial para la solicitud")
    })
    public ResponseEntity<HistorialEstadoDTO> getLastHistorialEstadoBySolicitud(
            @Parameter(description = "ID de la solicitud") @PathVariable("idSolicitud") Integer idSolicitud) {
        HistorialEstado historial = this.service.findLastByIdSolicitud(idSolicitud);
        return ResponseEntity.ok(this.mapper.toDTO(historial));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo historial de estado", 
               description = "Crea un nuevo registro de historial de estado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Historial creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<HistorialEstadoDTO> createHistorialEstado(
            @Valid @RequestBody HistorialEstadoDTO historialEstadoDTO) {
        HistorialEstado historial = this.mapper.toEntity(historialEstadoDTO);
        HistorialEstado createdHistorial = this.service.create(historial);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.mapper.toDTO(createdHistorial));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar historial de estado", 
               description = "Actualiza parcialmente un historial de estado existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historial actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Historial no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<HistorialEstadoDTO> updateHistorialEstado(
            @Parameter(description = "ID del historial de estado") @PathVariable("id") Long id,
            @Valid @RequestBody HistorialEstadoDTO historialEstadoDTO) {
        historialEstadoDTO.setIdHistorial(id);
        HistorialEstado historial = this.mapper.toEntity(historialEstadoDTO);
        HistorialEstado updatedHistorial = this.service.update(historial);
        return ResponseEntity.ok(this.mapper.toDTO(updatedHistorial));
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<String> handleNotFound(NotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({CreateException.class, UpdateException.class})
    public ResponseEntity<String> handleCreateUpdateException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @PostMapping("/cambiar-estado-validado")
    @Operation(summary = "Cambiar estado con validaciones de reglas de negocio", 
               description = "Cambia el estado de una solicitud aplicando todas las reglas de negocio")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Estado cambiado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Regla de negocio violada"),
        @ApiResponse(responseCode = "422", description = "Transición de estado no válida")
    })
    public ResponseEntity<HistorialEstadoDTO> cambiarEstadoConValidacion(
            @Valid @RequestBody HistorialEstadoDTO historialEstadoDTO) {
        
        HistorialEstado historial = this.mapper.toEntity(historialEstadoDTO);
        HistorialEstado createdHistorial = this.service.createWithValidation(
            historial, this.businessRuleService);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.mapper.toDTO(createdHistorial));
    }

    @GetMapping("/estados-validos/{idSolicitud}")
    @Operation(summary = "Obtener estados válidos para transición", 
               description = "Retorna los estados válidos a los que puede transicionar una solicitud")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estados válidos obtenidos exitosamente"),
        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    public ResponseEntity<Map<String, Object>> getEstadosValidos(
            @Parameter(description = "ID de la solicitud") @PathVariable("idSolicitud") Integer idSolicitud) {
        
        try {
            HistorialEstado ultimoEstado = this.service.findLastByIdSolicitud(idSolicitud);
            String estadoActual = ultimoEstado.getEstado();
            
            List<String> estadosValidos = getValidNextStates(estadoActual);
            
            return ResponseEntity.ok(Map.of(
                "estadoActual", estadoActual,
                "estadosValidos", estadosValidos,
                "fechaUltimaTransicion", ultimoEstado.getFechaHora()
            ));
        } catch (NotFoundException e) {
            return ResponseEntity.ok(Map.of(
                "estadoActual", "NUEVA",
                "estadosValidos", List.of("INGRESADA"),
                "mensaje", "Primera transición debe ser a INGRESADA"
            ));
        }
    }

    @GetMapping("/validar-sla/{idSolicitud}")
    @Operation(summary = "Validar SLA de análisis", 
               description = "Valida si una solicitud cumple con los SLA de análisis")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "SLA validado exitosamente"),
        @ApiResponse(responseCode = "422", description = "SLA excedido")
    })
    public ResponseEntity<Map<String, Object>> validarSLA(
            @Parameter(description = "ID de la solicitud") @PathVariable("idSolicitud") Integer idSolicitud) {
        
        try {
            this.businessRuleService.validateAnalysisSLA(idSolicitud);
            return ResponseEntity.ok(Map.of(
                "cumpleSLA", true,
                "mensaje", "La solicitud cumple con el SLA de análisis"
            ));
        } catch (BusinessRuleException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(Map.of(
                        "cumpleSLA", false,
                        "reglaViolada", e.getRule(),
                        "detalle", e.getDetail()
                    ));
        }
    }

    @GetMapping("/resumen-solicitud/{idSolicitud}")
    @Operation(summary = "Resumen completo de solicitud", 
               description = "Obtiene un resumen completo del estado y análisis de una solicitud")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resumen obtenido exitosamente"),
        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    public ResponseEntity<Map<String, Object>> getResumenSolicitud(
            @Parameter(description = "ID de la solicitud") @PathVariable("idSolicitud") Integer idSolicitud) {
        
        try {
            List<HistorialEstado> historial = this.service.findByIdSolicitud(idSolicitud);
            HistorialEstado estadoActual = historial.get(0); // El más reciente
            
            long diasEnProceso = java.time.temporal.ChronoUnit.DAYS.between(
                historial.get(historial.size() - 1).getFechaHora(), // Primer estado
                estadoActual.getFechaHora()
            );
            
            List<String> estadosRecorridos = historial.stream()
                    .map(HistorialEstado::getEstado)
                    .distinct()
                    .toList();
            
            return ResponseEntity.ok(Map.of(
                "idSolicitud", idSolicitud,
                "estadoActual", estadoActual.getEstado(),
                "fechaUltimaActualizacion", estadoActual.getFechaHora(),
                "diasEnProceso", diasEnProceso,
                "totalTransiciones", historial.size(),
                "estadosRecorridos", estadosRecorridos,
                "usuarioUltimaActualizacion", estadoActual.getUsuario()
            ));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/estadisticas-por-estado")
    @Operation(summary = "Estadísticas de historiales por estado", 
               description = "Obtiene estadísticas de historiales agrupadas por estado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente")
    })
    public ResponseEntity<Map<String, Object>> getEstadisticasPorEstado(
            @Parameter(description = "Filtrar por usuario") @RequestParam(required = false) String usuario) {
        
        List<HistorialEstado> historiales;
        if (usuario != null) {
            historiales = this.service.findByUsuario(usuario);
        } else {
            historiales = this.service.findAll();
        }
        
        Map<String, Long> estadisticas = historiales.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    HistorialEstado::getEstado,
                    java.util.stream.Collectors.counting()
                ));
        
        return ResponseEntity.ok(Map.of(
            "estadisticasPorEstado", estadisticas,
            "totalRegistros", historiales.size(),
            "usuario", usuario != null ? usuario : "TODOS"
        ));
    }

    private List<String> getValidNextStates(String estadoActual) {
        Map<String, List<String>> transicionesValidas = Map.of(
            "INGRESADA", List.of("EN_REVISION", "CANCELADA"),
            "EN_REVISION", List.of("DOC_PEND", "EN_ANALISIS", "RECHAZADA", "SUSPENDIDA"),
            "DOC_PEND", List.of("EN_REVISION", "EN_ANALISIS", "CANCELADA"),
            "EN_ANALISIS", List.of("APROBADA", "RECHAZADA", "DOC_PEND", "SUSPENDIDA"),
            "APROBADA", List.of("DESEMBOL", "SUSPENDIDA"),
            "RECHAZADA", List.of("EN_REVISION"),
            "SUSPENDIDA", List.of("EN_REVISION", "CANCELADA"),
            "CANCELADA", List.of(),
            "DESEMBOL", List.of()
        );
        
        return transicionesValidas.getOrDefault(estadoActual, List.of());
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