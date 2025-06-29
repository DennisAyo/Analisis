package com.banquito.originacion.analisis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.originacion.analisis.model.ReglaEvaluacionCrediticia;

@Repository
public interface ReglaEvaluacionCrediticiaRepository extends JpaRepository<ReglaEvaluacionCrediticia, Long> {

    List<ReglaEvaluacionCrediticia> findByActivaTrue();
    
    List<ReglaEvaluacionCrediticia> findByActivaTrueOrderByPrioridadAsc();
    
    List<ReglaEvaluacionCrediticia> findByNombreContainingIgnoreCase(String nombre);
    
    List<ReglaEvaluacionCrediticia> findByActivaAndPrioridadBetween(Boolean activa, Integer prioridadMinima, Integer prioridadMaxima);
} 