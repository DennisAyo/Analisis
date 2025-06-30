package com.banquito.originacion.analisis.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.originacion.analisis.model.EvaluacionCrediticia;

@Repository
public interface EvaluacionCrediticiaRepository extends JpaRepository<EvaluacionCrediticia, Long> {

    List<EvaluacionCrediticia> findByIdSolicitud(Integer idSolicitud);
    
    Optional<EvaluacionCrediticia> findTopByIdSolicitudOrderByVersionDesc(Integer idSolicitud);
    
    List<EvaluacionCrediticia> findByScoreInternoCalculado(BigDecimal scoreInterno);
    
    List<EvaluacionCrediticia> findByInformeBuro_IdInformeBuro(Long idInformeBuro);
    
    Optional<EvaluacionCrediticia> findTopByInformeBuro_IdInformeBuroOrderByVersionDesc(Long idInformeBuro);
    
    List<EvaluacionCrediticia> findByIdSolicitudOrderByVersionDesc(Integer idSolicitud);
    
    List<EvaluacionCrediticia> findByScoreInternoCalculadoBetween(BigDecimal scoreMin, BigDecimal scoreMax);
    
    List<EvaluacionCrediticia> findByDecisionFinalAnalista(String decision);
    
    List<EvaluacionCrediticia> findByResultadoAutomatico(String resultadoAutomatico);
} 