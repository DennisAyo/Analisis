package com.banquito.originacion.analisis.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.banquito.originacion.analisis.exception.CreateException;
import com.banquito.originacion.analisis.exception.NotFoundException;
import com.banquito.originacion.analisis.exception.UpdateException;
import com.banquito.originacion.analisis.model.EvaluacionCrediticia;
import com.banquito.originacion.analisis.repository.EvaluacionCrediticiaRepository;

@Service
public class EvaluacionCrediticiaService {

    private final EvaluacionCrediticiaRepository repository;
    private final ConsultaBuroService consultaBuroService;

    public EvaluacionCrediticiaService(EvaluacionCrediticiaRepository repository, 
                                     ConsultaBuroService consultaBuroService) {
        this.repository = repository;
        this.consultaBuroService = consultaBuroService;
    }

    public EvaluacionCrediticia findById(Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new NotFoundException(id.toString(), "EvaluacionCrediticia"));
    }

    public List<EvaluacionCrediticia> findByIdSolicitud(Integer idSolicitud) {
        List<EvaluacionCrediticia> evaluaciones = this.repository.findByIdSolicitudOrderByFechaEvaluacionDesc(idSolicitud);
        if (evaluaciones.isEmpty()) {
            throw new NotFoundException(idSolicitud.toString(), "EvaluacionCrediticia por solicitud");
        }
        return evaluaciones;
    }

    public EvaluacionCrediticia findLastByIdSolicitud(Integer idSolicitud) {
        return this.repository.findTopByIdSolicitudOrderByFechaEvaluacionDesc(idSolicitud)
                .orElseThrow(() -> new NotFoundException(idSolicitud.toString(), "Última EvaluacionCrediticia por solicitud"));
    }

    public List<EvaluacionCrediticia> findByCategoriaRiesgo(String categoriaRiesgo) {
        List<EvaluacionCrediticia> evaluaciones = this.repository.findByCategoriaRiesgo(categoriaRiesgo);
        if (evaluaciones.isEmpty()) {
            throw new NotFoundException(categoriaRiesgo, "EvaluacionCrediticia por categoría de riesgo");
        }
        return evaluaciones;
    }

    public List<EvaluacionCrediticia> findByTipoEvaluacion(Boolean esAutomatico) {
        List<EvaluacionCrediticia> evaluaciones = this.repository.findByEsAutomatico(esAutomatico);
        if (evaluaciones.isEmpty()) {
            throw new NotFoundException(esAutomatico.toString(), "EvaluacionCrediticia por tipo");
        }
        return evaluaciones;
    }

    public EvaluacionCrediticia create(EvaluacionCrediticia evaluacion) {
        try {
            // Validar que existe la consulta de buró
            consultaBuroService.findById(evaluacion.getConsultaBuro().getIdConsultaBuro());
            
            evaluacion.setFechaEvaluacion(LocalDateTime.now());
            evaluacion.setVersion(BigDecimal.ONE);
            return this.repository.save(evaluacion);
        } catch (Exception e) {
            throw new CreateException("EvaluacionCrediticia", e.getMessage());
        }
    }

    public EvaluacionCrediticia update(EvaluacionCrediticia evaluacion) {
        try {
            EvaluacionCrediticia existing = findById(evaluacion.getIdEvaluacionesCrediticias());
            evaluacion.setVersion(existing.getVersion().add(BigDecimal.ONE));
            return this.repository.save(evaluacion);
        } catch (Exception e) {
            throw new UpdateException("EvaluacionCrediticia", e.getMessage());
        }
    }

    /**
     * Calcula la categoría de riesgo basada en el score
     */
    public String calculateRiskCategory(BigDecimal score) {
        if (score == null) {
            return "SIN_SCORE";
        }
        
        BigDecimal scoreValue = score;
        if (scoreValue.compareTo(new BigDecimal("750")) >= 0) {
            return "BAJO";
        } else if (scoreValue.compareTo(new BigDecimal("650")) >= 0) {
            return "MEDIO";
        } else if (scoreValue.compareTo(new BigDecimal("550")) >= 0) {
            return "ALTO";
        } else {
            return "MUY_ALTO";
        }
    }

    /**
     * Crea una evaluación automática basada en la consulta de buró
     */
    public EvaluacionCrediticia createAutomaticEvaluation(Integer idSolicitud, Long idConsultaBuro) {
        try {
            var consultaBuro = consultaBuroService.findById(idConsultaBuro);
            
            EvaluacionCrediticia evaluacion = new EvaluacionCrediticia();
            evaluacion.setIdSolicitud(idSolicitud);
            evaluacion.setConsultaBuro(consultaBuro);
            evaluacion.setScoreInterno(consultaBuro.getScoreObtenido());
            evaluacion.setCategoriaRiesgo(calculateRiskCategory(consultaBuro.getScoreObtenido()));
            evaluacion.setEsAutomatico(true);
            
            return create(evaluacion);
        } catch (Exception e) {
            throw new CreateException("EvaluacionCrediticia", "Error al crear evaluación automática: " + e.getMessage());
        }
    }
} 