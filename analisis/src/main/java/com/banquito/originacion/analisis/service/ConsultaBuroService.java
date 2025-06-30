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

    public List<ConsultaBuro> findByIdSolicitud(Integer idSolicitud) {
        List<ConsultaBuro> consultas = this.repository.findByIdSolicitudOrderByFechaConsultaDesc(idSolicitud);
        if (consultas.isEmpty()) {
            throw new NotFoundException(idSolicitud.toString(), "ConsultaBuro por solicitud");
        }
        return consultas;
    }

    public ConsultaBuro findLastByIdSolicitud(Integer idSolicitud) {
        return this.repository.findTopByIdSolicitudOrderByFechaConsultaDesc(idSolicitud)
                .orElseThrow(() -> new NotFoundException(idSolicitud.toString(), "Última ConsultaBuro por solicitud"));
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
            ConsultaBuro existing = findById(consultaBuro.getIdConsulta());
            consultaBuro.setVersion(existing.getVersion().add(BigDecimal.ONE));
            return this.repository.save(consultaBuro);
        } catch (Exception e) {
            throw new UpdateException("ConsultaBuro", e.getMessage());
        }
    }

    public boolean hasSuccessfulQueries(Integer idSolicitud) {
        try {
            List<ConsultaBuro> consultas = this.repository.findByIdSolicitudAndEstadoConsulta(idSolicitud, "EXITOSA");
            return !consultas.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public BigDecimal getBestScore(Integer idSolicitud) {
        try {
            List<ConsultaBuro> consultas = findByIdSolicitud(idSolicitud);
            return consultas.stream()
                    .map(ConsultaBuro::getScoreExterno)
                    .filter(score -> score != null)
                    .max(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    public ConsultaBuro findByIdSolicitudWithBestScore(Integer idSolicitud) {
        List<ConsultaBuro> consultas = this.repository.findByIdSolicitudOrderByFechaConsultaDesc(idSolicitud);
        return consultas.stream()
                .filter(c -> c.getScoreExterno() != null)
                .max((c1, c2) -> c1.getScoreExterno().compareTo(c2.getScoreExterno()))
                .orElseThrow(() -> new NotFoundException(idSolicitud.toString(), 
                    "ConsultaBuro con score para solicitud"));
    }
} 