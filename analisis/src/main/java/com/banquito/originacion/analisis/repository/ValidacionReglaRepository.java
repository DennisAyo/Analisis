package com.banquito.originacion.analisis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.originacion.analisis.model.ValidacionRegla;

@Repository
public interface ValidacionReglaRepository extends JpaRepository<ValidacionRegla, Long> {

    List<ValidacionRegla> findByEvaluacionCrediticia_IdEvaluacionesCrediticias(Long idEvaluacionesCrediticias);
    
    List<ValidacionRegla> findByRegla_IdRegla(Long idRegla);
    
    List<ValidacionRegla> findByResultado(Boolean resultado);
    
    List<ValidacionRegla> findByEvaluacionCrediticia_IdEvaluacionesCrediticiasAndResultado(Long idEvaluacionesCrediticias, Boolean resultado);
    
    List<ValidacionRegla> findByEvaluacionCrediticia_IdSolicitud(Integer idSolicitud);
    
    List<ValidacionRegla> findByEvaluacionCrediticia_IdSolicitudAndResultado(Integer idSolicitud, Boolean resultado);
} 