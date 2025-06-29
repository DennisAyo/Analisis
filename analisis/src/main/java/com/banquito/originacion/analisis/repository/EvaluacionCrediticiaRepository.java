package com.banquito.originacion.analisis.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.originacion.analisis.model.EvaluacionCrediticia;

@Repository
public interface EvaluacionCrediticiaRepository extends JpaRepository<EvaluacionCrediticia, Long> {

    List<EvaluacionCrediticia> findByIdSolicitud(Integer idSolicitud);
    
    Optional<EvaluacionCrediticia> findTopByIdSolicitudOrderByFechaEvaluacionDesc(Integer idSolicitud);
    
    List<EvaluacionCrediticia> findByCategoriaRiesgo(String categoriaRiesgo);
    
    List<EvaluacionCrediticia> findByEsAutomatico(Boolean esAutomatico);
    
    List<EvaluacionCrediticia> findByConsultaBuro_IdConsultaBuro(Long idConsultaBuro);
    
    List<EvaluacionCrediticia> findByIdSolicitudOrderByFechaEvaluacionDesc(Integer idSolicitud);
} 