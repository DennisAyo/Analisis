package com.banquito.originacion.analisis.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.originacion.analisis.model.InformeBuro;

@Repository
public interface InformeBuroRepository extends JpaRepository<InformeBuro, Long> {

    Optional<InformeBuro> findByConsultaBuro_IdConsultaBuro(Long idConsultaBuro);
    
    List<InformeBuro> findByScoreBetween(BigDecimal scoreMin, BigDecimal scoreMax);
    
    List<InformeBuro> findByScoreGreaterThanEqual(BigDecimal scoreMin);
    
    List<InformeBuro> findByScoreLessThanEqual(BigDecimal scoreMax);
    
    List<InformeBuro> findByNumeroDeudaImpagasGreaterThan(Integer numeroDeudas);
    
    List<InformeBuro> findByNumeroDeudaImpagasEquals(Integer numeroDeudas);
    
    List<InformeBuro> findByMontoTotalAdeudadoGreaterThan(BigDecimal monto);
    
    List<InformeBuro> findByCapacidadPagoReportadaGreaterThan(BigDecimal capacidadPago);
    
    Optional<InformeBuro> findTopByConsultaBuro_IdConsultaBuroOrderByVersionDesc(Long idConsultaBuro);
} 