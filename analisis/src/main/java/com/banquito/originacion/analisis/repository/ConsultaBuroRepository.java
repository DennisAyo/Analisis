package com.banquito.originacion.analisis.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.originacion.analisis.model.ConsultaBuro;

@Repository
public interface ConsultaBuroRepository extends JpaRepository<ConsultaBuro, Long> {

    List<ConsultaBuro> findByIdClienteProspecto(Integer idClienteProspecto);
    
    Optional<ConsultaBuro> findTopByIdClienteProspectoOrderByFechaConsultaDesc(Integer idClienteProspecto);
    
    List<ConsultaBuro> findByEstadoConsulta(String estadoConsulta);
    
    List<ConsultaBuro> findByIdClienteProspectoAndEstadoConsulta(Integer idClienteProspecto, String estadoConsulta);
    
    List<ConsultaBuro> findByIdClienteProspectoOrderByFechaConsultaDesc(Integer idClienteProspecto);
} 