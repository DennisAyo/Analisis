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
@RequestMapping("/v1/observaciones-analistas")
@Tag(name = "Observaciones Analistas", description = "API para gestionar las observaciones de los analistas")
public class ObservacionAnalistaController {

    private final ObservacionAnalistaService service;
    private final ObservacionAnalistaMapper mapper;

    public ObservacionAnalistaController(ObservacionAnalistaService service, ObservacionAnalistaMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las observaciones de analistas", 
               description = "Retorna una lista de todas las observaciones de analistas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de observaciones obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron observaciones")
    })
    public ResponseEntity<List<ObservacionAnalistaDTO>> getAllObservacionesAnalistas(
            @Parameter(description = "Filtrar por usuario analista") @RequestParam(required = false) String usuario) {
        
        List<ObservacionAnalista> observaciones;
        
        if (usuario != null) {
            observaciones = this.service.findByUsuario(usuario);
        } else {
            observaciones = this.service.findAll();
        }
        
        List<ObservacionAnalistaDTO> dtos = new ArrayList<>(observaciones.size());
        for (ObservacionAnalista observacion : observaciones) {
            dtos.add(mapper.toDTO(observacion));
        }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener observación de analista por ID", 
               description = "Retorna una observación de analista específica por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Observación encontrada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Observación no encontrada")
    })
    public ResponseEntity<ObservacionAnalistaDTO> getObservacionAnalistaById(
            @Parameter(description = "ID de la observación del analista") @PathVariable("id") Long id) {
        ObservacionAnalista observacion = this.service.findById(id);
        return ResponseEntity.ok(this.mapper.toDTO(observacion));
    }

    @GetMapping("/solicitudes/{idSolicitud}")
    @Operation(summary = "Obtener observaciones por solicitud", 
               description = "Retorna todas las observaciones de analistas de una solicitud específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Observaciones encontradas exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron observaciones para la solicitud")
    })
    public ResponseEntity<List<ObservacionAnalistaDTO>> getObservacionesBySolicitud(
            @Parameter(description = "ID de la solicitud") @PathVariable("idSolicitud") Integer idSolicitud,
            @Parameter(description = "Filtrar por usuario analista") @RequestParam(required = false) String usuario) {
        
        List<ObservacionAnalista> observaciones;
        
        if (usuario != null) {
            observaciones = this.service.findByIdSolicitudAndUsuario(idSolicitud, usuario);
        } else {
            observaciones = this.service.findByIdSolicitud(idSolicitud);
        }
        
        List<ObservacionAnalistaDTO> dtos = new ArrayList<>(observaciones.size());
        for (ObservacionAnalista observacion : observaciones) {
            dtos.add(mapper.toDTO(observacion));
        }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/usuarios/{usuario}")
    @Operation(summary = "Obtener observaciones por usuario analista", 
               description = "Retorna todas las observaciones realizadas por un analista específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Observaciones encontradas exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron observaciones para el usuario")
    })
    public ResponseEntity<List<ObservacionAnalistaDTO>> getObservacionesByUsuario(
            @Parameter(description = "Usuario analista") @PathVariable("usuario") String usuario) {
        List<ObservacionAnalista> observaciones = this.service.findByUsuario(usuario);
        List<ObservacionAnalistaDTO> dtos = new ArrayList<>(observaciones.size());
        
        for (ObservacionAnalista observacion : observaciones) {
            dtos.add(mapper.toDTO(observacion));
        }
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    @Operation(summary = "Crear nueva observación de analista", 
               description = "Crea un nuevo registro de observación de analista")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Observación creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ObservacionAnalistaDTO> createObservacionAnalista(
            @Valid @RequestBody ObservacionAnalistaDTO observacionAnalistaDTO) {
        ObservacionAnalista observacion = this.mapper.toEntity(observacionAnalistaDTO);
        ObservacionAnalista createdObservacion = this.service.create(observacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.mapper.toDTO(createdObservacion));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar observación de analista", 
               description = "Actualiza parcialmente una observación de analista existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Observación actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Observación no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<ObservacionAnalistaDTO> updateObservacionAnalista(
            @Parameter(description = "ID de la observación del analista") @PathVariable("id") Long id,
            @Valid @RequestBody ObservacionAnalistaDTO observacionAnalistaDTO) {
        observacionAnalistaDTO.setIdObservacionAnalista(id);
        ObservacionAnalista observacion = this.mapper.toEntity(observacionAnalistaDTO);
        ObservacionAnalista updatedObservacion = this.service.update(observacion);
        return ResponseEntity.ok(this.mapper.toDTO(updatedObservacion));
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<String> handleNotFound(NotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/estadisticas-por-usuario/{usuario}")
    @Operation(summary = "Estadísticas de observaciones por usuario", 
               description = "Obtiene estadísticas detalladas de las observaciones realizadas por un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<Map<String, Object>> getEstadisticasUsuario(
            @Parameter(description = "Usuario analista") @PathVariable("usuario") String usuario) {
        
        try {
            List<ObservacionAnalista> observaciones = this.service.findByUsuario(usuario);
            
            // Estadísticas básicas
            Map<String, Long> observacionesPorMes = observaciones.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    obs -> obs.getFechaHora().getYear() + "-" + obs.getFechaHora().getMonthValue(),
                    java.util.stream.Collectors.counting()
                ));

            // Palabras clave más comunes en observaciones
            Map<String, Long> palabrasComunes = observaciones.stream()
                .flatMap(obs -> java.util.Arrays.stream(obs.getRazonIntervencion().toLowerCase().split("\\s+")))
                .filter(palabra -> palabra.length() > 3) // Solo palabras de más de 3 caracteres
                .collect(java.util.stream.Collectors.groupingBy(
                    palabra -> palabra,
                    java.util.stream.Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10) // Top 10 palabras
                .collect(java.util.stream.Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (e1, e2) -> e1,
                    java.util.LinkedHashMap::new
                ));

            return ResponseEntity.ok(Map.of(
                "usuario", usuario,
                "totalObservaciones", observaciones.size(),
                "observacionesPorMes", observacionesPorMes,
                "palabrasComunes", palabrasComunes,
                "primerObservacion", observaciones.stream()
                    .map(ObservacionAnalista::getFechaHora)
                    .min(java.time.LocalDateTime::compareTo)
                    .orElse(null),
                "ultimaObservacion", observaciones.stream()
                    .map(ObservacionAnalista::getFechaHora)
                    .max(java.time.LocalDateTime::compareTo)
                    .orElse(null)
            ));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/observaciones-recientes")
    @Operation(summary = "Observaciones recientes", 
               description = "Obtiene las observaciones más recientes del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Observaciones obtenidas exitosamente")
    })
    public ResponseEntity<List<ObservacionAnalistaDTO>> getObservacionesRecientes(
            @Parameter(description = "Número de observaciones a obtener (máximo 50)") 
            @RequestParam(defaultValue = "10") Integer limite) {
        
        if (limite > 50) {
            limite = 50; // Máximo 50 observaciones
        }
        
        List<ObservacionAnalista> todasObservaciones = this.service.findAll();
        
        List<ObservacionAnalista> observacionesRecientes = todasObservaciones.stream()
            .sorted((o1, o2) -> o2.getFechaHora().compareTo(o1.getFechaHora())) // Más recientes primero
            .limit(limite)
            .toList();
        
        List<ObservacionAnalistaDTO> dtos = new ArrayList<>(observacionesRecientes.size());
        for (ObservacionAnalista observacion : observacionesRecientes) {
            dtos.add(mapper.toDTO(observacion));
        }
        
        return ResponseEntity.ok(dtos);
    }

    @ExceptionHandler({CreateException.class, UpdateException.class})
    public ResponseEntity<String> handleCreateUpdateException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
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