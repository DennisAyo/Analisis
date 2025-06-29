package com.banquito.originacion.analisis.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.banquito.originacion.analisis.exception.BusinessRuleException;
import com.banquito.originacion.analisis.model.HistorialEstado;
import com.banquito.originacion.analisis.model.ObservacionAnalista;

@Service
public class AnalisisBusinessRuleService {

    private final HistorialEstadoService historialEstadoService;
    private final ObservacionAnalistaService observacionAnalistaService;

    public AnalisisBusinessRuleService(HistorialEstadoService historialEstadoService, 
                                     ObservacionAnalistaService observacionAnalistaService) {
        this.historialEstadoService = historialEstadoService;
        this.observacionAnalistaService = observacionAnalistaService;
    }

    /**
     * Valida la transición de estados
     */
    public void validateStateTransition(Integer idSolicitud, String nuevoEstado, String usuario) {
        try {
            HistorialEstado ultimoEstado = historialEstadoService.findLastByIdSolicitud(idSolicitud);
            String estadoActual = ultimoEstado.getEstado();
            
            if (!isValidTransition(estadoActual, nuevoEstado)) {
                throw new BusinessRuleException("TRANSICION_INVALIDA", 
                    "No se puede cambiar de " + estadoActual + " a " + nuevoEstado);
            }
            
            // Reglas específicas por estado
            validateSpecificStateRules(idSolicitud, nuevoEstado, usuario);
            
        } catch (Exception e) {
            // Si no hay historial previo, debe empezar con INGRESADA
            if (!nuevoEstado.equals("INGRESADA")) {
                throw new BusinessRuleException("ESTADO_INICIAL_INVALIDO", 
                    "La primera transición debe ser a estado INGRESADA");
            }
        }
    }

    /**
     * Valida si una transición de estado es válida
     */
    private boolean isValidTransition(String estadoActual, String nuevoEstado) {
        Map<String, List<String>> transicionesValidas = Map.of(
            "INGRESADA", List.of("EN_REVISION", "CANCELADA"),
            "EN_REVISION", List.of("DOC_PEND", "EN_ANALISIS", "RECHAZADA", "SUSPENDIDA"),
            "DOC_PEND", List.of("EN_REVISION", "EN_ANALISIS", "CANCELADA"),
            "EN_ANALISIS", List.of("APROBADA", "RECHAZADA", "DOC_PEND", "SUSPENDIDA"),
            "APROBADA", List.of("DESEMBOL", "SUSPENDIDA"),
            "RECHAZADA", List.of("EN_REVISION"), // Solo si hay nueva información
            "SUSPENDIDA", List.of("EN_REVISION", "CANCELADA"),
            "CANCELADA", List.of(), // Estado final
            "DESEMBOL", List.of()   // Estado final
        );

        return transicionesValidas.getOrDefault(estadoActual, List.of()).contains(nuevoEstado);
    }

    /**
     * Aplica reglas específicas por estado
     */
    private void validateSpecificStateRules(Integer idSolicitud, String nuevoEstado, String usuario) {
        switch (nuevoEstado) {
            case "RECHAZADA":
                validateRejectionRules(idSolicitud, usuario);
                break;
            case "APROBADA":
                validateApprovalRules(idSolicitud, usuario);
                break;
            case "EN_ANALISIS":
                validateAnalysisRules(idSolicitud);
                break;
            case "SUSPENDIDA":
                validateSuspensionRules(idSolicitud, usuario);
                break;
        }
    }

    /**
     * Valida reglas para rechazo
     */
    private void validateRejectionRules(Integer idSolicitud, String usuario) {
        // Debe tener al menos una observación del analista
        try {
            List<ObservacionAnalista> observaciones = observacionAnalistaService.findByIdSolicitud(idSolicitud);
            boolean tieneObservacionRechazo = observaciones.stream()
                .anyMatch(obs -> obs.getRazonIntervencion().toLowerCase().contains("rechazo"));
            
            if (!tieneObservacionRechazo) {
                throw new BusinessRuleException("RECHAZO_SIN_JUSTIFICACION", 
                    "Para rechazar una solicitud debe existir una observación que justifique el rechazo");
            }
        } catch (Exception e) {
            throw new BusinessRuleException("RECHAZO_SIN_OBSERVACION", 
                "Para rechazar una solicitud debe existir al menos una observación del analista");
        }
    }

    /**
     * Valida reglas para aprobación
     */
    private void validateApprovalRules(Integer idSolicitud, String usuario) {
        // La solicitud debe haber estado en análisis
        List<HistorialEstado> historial = historialEstadoService.findByIdSolicitud(idSolicitud);
        boolean estuvoEnAnalisis = historial.stream()
            .anyMatch(h -> h.getEstado().equals("EN_ANALISIS"));
        
        if (!estuvoEnAnalisis) {
            throw new BusinessRuleException("APROBACION_SIN_ANALISIS", 
                "No se puede aprobar una solicitud que no ha pasado por análisis crediticio");
        }
    }

    /**
     * Valida tiempo máximo en análisis (SLA)
     */
    public void validateAnalysisSLA(Integer idSolicitud) {
        try {
            List<HistorialEstado> historial = historialEstadoService.findByIdSolicitud(idSolicitud);
            
            // Buscar cuando entró en análisis
            HistorialEstado ingresoAnalisis = historial.stream()
                .filter(h -> h.getEstado().equals("EN_ANALISIS"))
                .findFirst()
                .orElse(null);
            
            if (ingresoAnalisis != null) {
                long diasEnAnalisis = ChronoUnit.DAYS.between(
                    ingresoAnalisis.getFechaHora(), LocalDateTime.now());
                
                if (diasEnAnalisis > 5) { // SLA de 5 días
                    throw new BusinessRuleException("SLA_ANALYSIS_EXCEEDED", 
                        "La solicitud lleva " + diasEnAnalisis + " días en análisis. SLA máximo: 5 días");
                }
            }
        } catch (Exception e) {
            // Si no se puede validar SLA, continuar sin error
        }
    }

    /**
     * Valida reglas para análisis
     */
    private void validateAnalysisRules(Integer idSolicitud) {
        // Validar que no haya estado en análisis por más de 2 veces
        List<HistorialEstado> historial = historialEstadoService.findByIdSolicitud(idSolicitud);
        long vecesEnAnalisis = historial.stream()
            .filter(h -> h.getEstado().equals("EN_ANALISIS"))
            .count();
        
        if (vecesEnAnalisis >= 2) {
            throw new BusinessRuleException("LIMITE_REANALISIS", 
                "Una solicitud no puede reingresar a análisis más de 2 veces");
        }
    }

    /**
     * Valida reglas para suspensión
     */
    private void validateSuspensionRules(Integer idSolicitud, String usuario) {
        // Debe tener una observación que justifique la suspensión
        try {
            List<ObservacionAnalista> observaciones = observacionAnalistaService.findByIdSolicitud(idSolicitud);
            boolean tieneObservacionSuspension = observaciones.stream()
                .anyMatch(obs -> obs.getRazonIntervencion().toLowerCase().contains("suspend"));
            
            if (!tieneObservacionSuspension) {
                throw new BusinessRuleException("SUSPENSION_SIN_JUSTIFICACION", 
                    "Para suspender una solicitud debe existir una observación que justifique la suspensión");
            }
        } catch (Exception e) {
            throw new BusinessRuleException("SUSPENSION_SIN_OBSERVACION", 
                "Para suspender una solicitud debe existir al menos una observación del analista");
        }
    }

    /**
     * Obtiene estadísticas de análisis por usuario
     */
    public Map<String, Object> getAnalysisStatsByUser(String usuario) {
        try {
            List<ObservacionAnalista> observaciones = observacionAnalistaService.findByUsuario(usuario);
            
            Map<String, Long> observacionesPorMes = observaciones.stream()
                .collect(Collectors.groupingBy(
                    obs -> obs.getFechaHora().getYear() + "-" + obs.getFechaHora().getMonthValue(),
                    Collectors.counting()
                ));

            return Map.of(
                "totalObservaciones", observaciones.size(),
                "observacionesPorMes", observacionesPorMes,
                "promedioObservacionesDiarias", observaciones.size() / Math.max(1, 
                    ChronoUnit.DAYS.between(
                        observaciones.stream().map(ObservacionAnalista::getFechaHora).min(LocalDateTime::compareTo).orElse(LocalDateTime.now()),
                        LocalDateTime.now()
                    ))
            );
        } catch (Exception e) {
            return Map.of("error", "No se pudieron obtener estadísticas para el usuario: " + usuario);
        }
    }
} 