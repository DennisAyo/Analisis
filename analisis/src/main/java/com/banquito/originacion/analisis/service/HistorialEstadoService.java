package com.banquito.originacion.analisis.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.banquito.originacion.analisis.exception.CreateException;
import com.banquito.originacion.analisis.exception.NotFoundException;
import com.banquito.originacion.analisis.exception.UpdateException;
import com.banquito.originacion.analisis.model.HistorialEstado;
import com.banquito.originacion.analisis.repository.HistorialEstadoRepository;

@Service
public class HistorialEstadoService {

    private final HistorialEstadoRepository repository;

    public HistorialEstadoService(HistorialEstadoRepository repository) {
        this.repository = repository;
    }

    public List<HistorialEstado> findAll() {
        return this.repository.findAll();
    }

    public HistorialEstado findById(Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new NotFoundException(id.toString(), "HistorialEstado"));
    }

    public List<HistorialEstado> findByIdSolicitud(Integer idSolicitud) {
        List<HistorialEstado> historiales = this.repository.findByIdSolicitudOrderByFechaHoraDesc(idSolicitud);
        if (historiales.isEmpty()) {
            throw new NotFoundException(idSolicitud.toString(), "HistorialEstado por solicitud");
        }
        return historiales;
    }

    public List<HistorialEstado> findByEstado(String estado) {
        List<HistorialEstado> historiales = this.repository.findByEstado(estado);
        if (historiales.isEmpty()) {
            throw new NotFoundException(estado, "HistorialEstado por estado");
        }
        return historiales;
    }

    public List<HistorialEstado> findByUsuario(String usuario) {
        List<HistorialEstado> historiales = this.repository.findByUsuario(usuario);
        if (historiales.isEmpty()) {
            throw new NotFoundException(usuario, "HistorialEstado por usuario");
        }
        return historiales;
    }

    public HistorialEstado findLastByIdSolicitud(Integer idSolicitud) {
        return this.repository.findTopByIdSolicitudOrderByFechaHoraDesc(idSolicitud)
                .orElseThrow(() -> new NotFoundException(idSolicitud.toString(), "Último HistorialEstado por solicitud"));
    }

    public HistorialEstado create(HistorialEstado historialEstado) {
        try {
            historialEstado.setFechaHora(LocalDateTime.now());
            historialEstado.setVersion(BigDecimal.ONE);
            return this.repository.save(historialEstado);
        } catch (Exception e) {
            throw new CreateException("HistorialEstado", e.getMessage());
        }
    }

    public HistorialEstado createWithValidation(HistorialEstado historialEstado, AnalisisBusinessRuleService businessRuleService) {
        try {
            // Aplicar validaciones de reglas de negocio
            businessRuleService.validateStateTransition(
                historialEstado.getIdSolicitud(), 
                historialEstado.getEstado(), 
                historialEstado.getUsuario()
            );
            
            // Validar SLA si aplica
            if ("EN_ANALISIS".equals(historialEstado.getEstado())) {
                businessRuleService.validateAnalysisSLA(historialEstado.getIdSolicitud());
            }
            
            return create(historialEstado);
        } catch (Exception e) {
            throw new CreateException("HistorialEstado", e.getMessage());
        }
    }

    public HistorialEstado update(HistorialEstado historialEstado) {
        try {
            HistorialEstado existing = findById(historialEstado.getIdHistorial());
            historialEstado.setVersion(existing.getVersion().add(BigDecimal.ONE));
            return this.repository.save(historialEstado);
        } catch (Exception e) {
            throw new UpdateException("HistorialEstado", e.getMessage());
        }
    }
} 