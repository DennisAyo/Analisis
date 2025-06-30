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
    private final InformeBuroService informeBuroService;

    public EvaluacionCrediticiaService(EvaluacionCrediticiaRepository repository, 
                                     InformeBuroService informeBuroService) {
        this.repository = repository;
        this.informeBuroService = informeBuroService;
    }

    public EvaluacionCrediticia findById(Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new NotFoundException(id.toString(), "EvaluacionCrediticia"));
    }

    public List<EvaluacionCrediticia> findByIdSolicitud(Integer idSolicitud) {
        List<EvaluacionCrediticia> evaluaciones = this.repository.findByIdSolicitudOrderByVersionDesc(idSolicitud);
        if (evaluaciones.isEmpty()) {
            throw new NotFoundException(idSolicitud.toString(), "EvaluacionCrediticia por solicitud");
        }
        return evaluaciones;
    }

    public EvaluacionCrediticia findLastByIdSolicitud(Integer idSolicitud) {
        return this.repository.findTopByIdSolicitudOrderByVersionDesc(idSolicitud)
                .orElseThrow(() -> new NotFoundException(idSolicitud.toString(), "Última EvaluacionCrediticia por solicitud"));
    }

    public List<EvaluacionCrediticia> findByScoreInterno(BigDecimal scoreInterno) {
        List<EvaluacionCrediticia> evaluaciones = this.repository.findByScoreInternoCalculado(scoreInterno);
        if (evaluaciones.isEmpty()) {
            throw new NotFoundException(scoreInterno.toString(), "EvaluacionCrediticia por score interno");
        }
        return evaluaciones;
    }

    public List<EvaluacionCrediticia> findByDecisionAnalista(String decision) {
        List<EvaluacionCrediticia> evaluaciones = this.repository.findByDecisionFinalAnalista(decision);
        if (evaluaciones.isEmpty()) {
            throw new NotFoundException(decision, "EvaluacionCrediticia por decisión del analista");
        }
        return evaluaciones;
    }

    public List<EvaluacionCrediticia> findByInformeBuro(Long idInformeBuro) {
        List<EvaluacionCrediticia> evaluaciones = this.repository.findByInformeBuro_IdInformeBuro(idInformeBuro);
        if (evaluaciones.isEmpty()) {
            throw new NotFoundException(idInformeBuro.toString(), "EvaluacionCrediticia por informe de buró");
        }
        return evaluaciones;
    }

    public List<EvaluacionCrediticia> findByResultadoAutomatico(String resultado) {
        List<EvaluacionCrediticia> evaluaciones = this.repository.findByResultadoAutomatico(resultado);
        if (evaluaciones.isEmpty()) {
            throw new NotFoundException(resultado, "EvaluacionCrediticia por resultado automático");
        }
        return evaluaciones;
    }

    public EvaluacionCrediticia create(EvaluacionCrediticia evaluacion) {
        try {

            informeBuroService.findById(evaluacion.getInformeBuro().getIdInformeBuro());
            
            if (evaluacion.getFechaEvaluacion() == null) {
                evaluacion.setFechaEvaluacion(LocalDateTime.now());
            }
            
            evaluacion.setVersion(BigDecimal.ONE);
            return this.repository.save(evaluacion);
        } catch (Exception e) {
            throw new CreateException("EvaluacionCrediticia", e.getMessage());
        }
    }

    public EvaluacionCrediticia update(EvaluacionCrediticia evaluacion) {
        try {
            EvaluacionCrediticia existing = findById(evaluacion.getIdEvaluacion());
            evaluacion.setVersion(existing.getVersion().add(BigDecimal.ONE));
            return this.repository.save(evaluacion);
        } catch (Exception e) {
            throw new UpdateException("EvaluacionCrediticia", e.getMessage());
        }
    }

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

    public String determineAutomaticResult(BigDecimal score) {
        if (score == null) {
            return "REVISION_MANUAL";
        }
        
        if (score.compareTo(new BigDecimal("700")) >= 0) {
            return "APROBADO";
        } else if (score.compareTo(new BigDecimal("500")) < 0) {
            return "RECHAZADO";
        } else {
            return "REVISION_MANUAL";
        }
    }

    public EvaluacionCrediticia createAutomaticEvaluation(Integer idSolicitud, Long idInformeBuro) {
        try {
            var informeBuro = informeBuroService.findById(idInformeBuro);
            
            EvaluacionCrediticia evaluacion = new EvaluacionCrediticia();
            evaluacion.setIdSolicitud(idSolicitud);
            evaluacion.setInformeBuro(informeBuro);
            evaluacion.setFechaEvaluacion(LocalDateTime.now());
            evaluacion.setScoreInternoCalculado(informeBuro.getScore());
            
            String resultadoAutomatico = determineAutomaticResult(informeBuro.getScore());
            evaluacion.setResultadoAutomatico(resultadoAutomatico);
            
            evaluacion.setObservacionesMotorReglas(
                "Evaluación automática. Score: " + informeBuro.getScore() + 
                ", Resultado: " + resultadoAutomatico + 
                ", Categoría riesgo: " + calculateRiskCategory(informeBuro.getScore())
            );
            
            return create(evaluacion);
        } catch (Exception e) {
            throw new CreateException("EvaluacionCrediticia", "Error al crear evaluación automática: " + e.getMessage());
        }
    }
} 