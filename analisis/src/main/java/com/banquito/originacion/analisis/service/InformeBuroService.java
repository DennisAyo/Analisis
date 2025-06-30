package com.banquito.originacion.analisis.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.banquito.originacion.analisis.exception.CreateException;
import com.banquito.originacion.analisis.exception.NotFoundException;
import com.banquito.originacion.analisis.exception.UpdateException;
import com.banquito.originacion.analisis.model.InformeBuro;
import com.banquito.originacion.analisis.repository.InformeBuroRepository;

@Service
public class InformeBuroService {

    private final InformeBuroRepository repository;
    private final ConsultaBuroService consultaBuroService;

    public InformeBuroService(InformeBuroRepository repository, ConsultaBuroService consultaBuroService) {
        this.repository = repository;
        this.consultaBuroService = consultaBuroService;
    }

    public InformeBuro findById(Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new NotFoundException(id.toString(), "InformeBuro"));
    }

    public InformeBuro findByConsultaBuro(Long idConsultaBuro) {
        return this.repository.findByConsultaBuro_IdConsultaBuro(idConsultaBuro)
                .orElseThrow(() -> new NotFoundException(idConsultaBuro.toString(), "InformeBuro por consulta de buró"));
    }

    public List<InformeBuro> findByScoreRange(BigDecimal scoreMin, BigDecimal scoreMax) {
        List<InformeBuro> informes = this.repository.findByScoreBetween(scoreMin, scoreMax);
        if (informes.isEmpty()) {
            throw new NotFoundException("Score entre " + scoreMin + " y " + scoreMax, "InformeBuro por rango de score");
        }
        return informes;
    }

    public List<InformeBuro> findByMinScore(BigDecimal scoreMin) {
        List<InformeBuro> informes = this.repository.findByScoreGreaterThanEqual(scoreMin);
        if (informes.isEmpty()) {
            throw new NotFoundException("Score mínimo " + scoreMin, "InformeBuro por score mínimo");
        }
        return informes;
    }

    public List<InformeBuro> findByMaxScore(BigDecimal scoreMax) {
        List<InformeBuro> informes = this.repository.findByScoreLessThanEqual(scoreMax);
        if (informes.isEmpty()) {
            throw new NotFoundException("Score máximo " + scoreMax, "InformeBuro por score máximo");
        }
        return informes;
    }

    public List<InformeBuro> findByDeudaImpagas(Integer numeroDeudas) {
        List<InformeBuro> informes = this.repository.findByNumeroDeudaImpagasEquals(numeroDeudas);
        if (informes.isEmpty()) {
            throw new NotFoundException(numeroDeudas.toString(), "InformeBuro por número de deudas impagas");
        }
        return informes;
    }

    public List<InformeBuro> findWithDeudaImpagasGreaterThan(Integer numeroDeudas) {
        List<InformeBuro> informes = this.repository.findByNumeroDeudaImpagasGreaterThan(numeroDeudas);
        if (informes.isEmpty()) {
            throw new NotFoundException("Más de " + numeroDeudas + " deudas", "InformeBuro con deudas impagas superiores");
        }
        return informes;
    }

    public List<InformeBuro> findByMontoAdeudadoGreaterThan(BigDecimal monto) {
        List<InformeBuro> informes = this.repository.findByMontoTotalAdeudadoGreaterThan(monto);
        if (informes.isEmpty()) {
            throw new NotFoundException("Monto superior a " + monto, "InformeBuro por monto adeudado");
        }
        return informes;
    }

    public List<InformeBuro> findByCapacidadPagoGreaterThan(BigDecimal capacidadPago) {
        List<InformeBuro> informes = this.repository.findByCapacidadPagoReportadaGreaterThan(capacidadPago);
        if (informes.isEmpty()) {
            throw new NotFoundException("Capacidad de pago superior a " + capacidadPago, "InformeBuro por capacidad de pago");
        }
        return informes;
    }

    public InformeBuro findLatestByConsultaBuro(Long idConsultaBuro) {
        return this.repository.findTopByConsultaBuro_IdConsultaBuroOrderByVersionDesc(idConsultaBuro)
                .orElseThrow(() -> new NotFoundException(idConsultaBuro.toString(), "Último InformeBuro por consulta de buró"));
    }

    public InformeBuro create(InformeBuro informe) {
        try {

            consultaBuroService.findById(informe.getConsultaBuro().getIdConsulta());
            
            informe.setVersion(BigDecimal.ONE);
            return this.repository.save(informe);
        } catch (Exception e) {
            throw new CreateException("InformeBuro", e.getMessage());
        }
    }

    public InformeBuro update(InformeBuro informe) {
        try {
            InformeBuro existing = findById(informe.getIdInformeBuro());
            informe.setVersion(existing.getVersion().add(BigDecimal.ONE));
            return this.repository.save(informe);
        } catch (Exception e) {
            throw new UpdateException("InformeBuro", e.getMessage());
        }
    }

    public InformeBuro updateJsonRespuesta(Long id, String jsonRespuesta) {
        try {
            InformeBuro informe = findById(id);
            informe.setJsonRespuestaCompleta(jsonRespuesta);
            return update(informe);
        } catch (Exception e) {
            throw new UpdateException("InformeBuro", "Error al actualizar JSON de respuesta: " + e.getMessage());
        }
    }

    public BigDecimal calculateDebtToPaymentCapacityRatio(Long id) {
        InformeBuro informe = findById(id);
        
        if (informe.getMontoTotalAdeudado() == null || informe.getCapacidadPagoReportada() == null) {
            return BigDecimal.ZERO;
        }
        
        if (informe.getCapacidadPagoReportada().compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.valueOf(999.99); 
        }
        
        return informe.getMontoTotalAdeudado()
                .divide(informe.getCapacidadPagoReportada(), 4, java.math.RoundingMode.HALF_UP);
    }
} 