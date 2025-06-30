package com.banquito.originacion.analisis.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.originacion.analisis.model.ObservacionAnalista;

@Repository
public interface ObservacionAnalistaRepository extends JpaRepository<ObservacionAnalista, Long> {

    List<ObservacionAnalista> findByIdEvaluacion(BigDecimal idEvaluacion);
    
    List<ObservacionAnalista> findByIdUsuario(BigDecimal idUsuario);
    
    List<ObservacionAnalista> findByIdEvaluacionAndIdUsuario(BigDecimal idEvaluacion, BigDecimal idUsuario);
    
    List<ObservacionAnalista> findByIdEvaluacionOrderByFechaHoraDesc(BigDecimal idEvaluacion);
    
    List<ObservacionAnalista> findByIdUsuarioOrderByFechaHoraDesc(BigDecimal idUsuario);
    
    List<ObservacionAnalista> findByRazonIntervencionAutoEnum(String razonIntervencion);
} 