package com.banquito.originacion.analisis.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.originacion.analisis.model.ConsultaBuro;

@Repository
public interface ConsultaBuroRepository extends JpaRepository<ConsultaBuro, Long> {

    List<ConsultaBuro> findByIdSolicitud(Integer idSolicitud);
    
    Optional<ConsultaBuro> findTopByIdSolicitudOrderByFechaConsultaDesc(Integer idSolicitud);
    
    List<ConsultaBuro> findByEstadoConsulta(String estadoConsulta);
    
    List<ConsultaBuro> findByIdSolicitudAndEstadoConsulta(Integer idSolicitud, String estadoConsulta);
    
    List<ConsultaBuro> findByIdSolicitudOrderByFechaConsultaDesc(Integer idSolicitud);
    
    List<ConsultaBuro> findByScoreExternoGreaterThanEqual(java.math.BigDecimal scoreMinimo);
    
    List<ConsultaBuro> findByCuentasMorosasBetween(java.math.BigDecimal morosaMin, java.math.BigDecimal morosaMax);
} 