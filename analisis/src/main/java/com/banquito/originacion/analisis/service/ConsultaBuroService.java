package com.banquito.originacion.analisis.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.banquito.originacion.analisis.exception.CreateException;
import com.banquito.originacion.analisis.exception.NotFoundException;
import com.banquito.originacion.analisis.exception.UpdateException;
import com.banquito.originacion.analisis.model.ConsultaBuro;
import com.banquito.originacion.analisis.repository.ConsultaBuroRepository;

@Service
public class ConsultaBuroService {

    private final ConsultaBuroRepository repository;

    public ConsultaBuroService(ConsultaBuroRepository repository) {
        this.repository = repository;
    }

    public ConsultaBuro findById(Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new NotFoundException(id.toString(), "ConsultaBuro"));
    }

    public List<ConsultaBuro> findByIdClienteProspecto(Integer idClienteProspecto) {
        List<ConsultaBuro> consultas = this.repository.findByIdClienteProspectoOrderByFechaConsultaDesc(idClienteProspecto);
        if (consultas.isEmpty()) {
            throw new NotFoundException(idClienteProspecto.toString(), "ConsultaBuro por cliente");
        }
        return consultas;
    }

    public ConsultaBuro findLastByIdClienteProspecto(Integer idClienteProspecto) {
        return this.repository.findTopByIdClienteProspectoOrderByFechaConsultaDesc(idClienteProspecto)
                .orElseThrow(() -> new NotFoundException(idClienteProspecto.toString(), "Última ConsultaBuro por cliente"));
    }

    public List<ConsultaBuro> findByEstadoConsulta(String estadoConsulta) {
        List<ConsultaBuro> consultas = this.repository.findByEstadoConsulta(estadoConsulta);
        if (consultas.isEmpty()) {
            throw new NotFoundException(estadoConsulta, "ConsultaBuro por estado");
        }
        return consultas;
    }

    public ConsultaBuro create(ConsultaBuro consultaBuro) {
        try {
            consultaBuro.setFechaConsulta(LocalDateTime.now());
            consultaBuro.setVersion(BigDecimal.ONE);
            return this.repository.save(consultaBuro);
        } catch (Exception e) {
            throw new CreateException("ConsultaBuro", e.getMessage());
        }
    }

    public ConsultaBuro update(ConsultaBuro consultaBuro) {
        try {
            ConsultaBuro existing = findById(consultaBuro.getIdConsultaBuro());
            consultaBuro.setVersion(existing.getVersion().add(BigDecimal.ONE));
            return this.repository.save(consultaBuro);
        } catch (Exception e) {
            throw new UpdateException("ConsultaBuro", e.getMessage());
        }
    }

    /**
     * Verifica si un cliente tiene consultas previas exitosas
     */
    public boolean hasSuccessfulQueries(Integer idClienteProspecto) {
        try {
            List<ConsultaBuro> consultas = this.repository.findByIdClienteProspectoAndEstadoConsulta(idClienteProspecto, "EXITOSA");
            return !consultas.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtiene el mejor score de un cliente
     */
    public BigDecimal getBestScore(Integer idClienteProspecto) {
        try {
            List<ConsultaBuro> consultas = findByIdClienteProspecto(idClienteProspecto);
            return consultas.stream()
                    .map(ConsultaBuro::getScoreObtenido)
                    .filter(score -> score != null)
                    .max(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
} 