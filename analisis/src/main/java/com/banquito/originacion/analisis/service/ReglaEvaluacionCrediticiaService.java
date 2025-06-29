package com.banquito.originacion.analisis.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.banquito.originacion.analisis.exception.CreateException;
import com.banquito.originacion.analisis.exception.NotFoundException;
import com.banquito.originacion.analisis.exception.UpdateException;
import com.banquito.originacion.analisis.model.ReglaEvaluacionCrediticia;
import com.banquito.originacion.analisis.repository.ReglaEvaluacionCrediticiaRepository;

@Service
public class ReglaEvaluacionCrediticiaService {

    private final ReglaEvaluacionCrediticiaRepository repository;

    public ReglaEvaluacionCrediticiaService(ReglaEvaluacionCrediticiaRepository repository) {
        this.repository = repository;
    }

    public ReglaEvaluacionCrediticia findById(Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new NotFoundException(id.toString(), "ReglaEvaluacionCrediticia"));
    }

    public List<ReglaEvaluacionCrediticia> findActive() {
        List<ReglaEvaluacionCrediticia> reglas = this.repository.findByActivaTrueOrderByPrioridadAsc();
        if (reglas.isEmpty()) {
            throw new NotFoundException("true", "ReglaEvaluacionCrediticia activas");
        }
        return reglas;
    }

    public List<ReglaEvaluacionCrediticia> findByNombre(String nombre) {
        List<ReglaEvaluacionCrediticia> reglas = this.repository.findByNombreContainingIgnoreCase(nombre);
        if (reglas.isEmpty()) {
            throw new NotFoundException(nombre, "ReglaEvaluacionCrediticia por nombre");
        }
        return reglas;
    }

    public List<ReglaEvaluacionCrediticia> findByPrioridadRange(Integer prioridadMinima, Integer prioridadMaxima) {
        List<ReglaEvaluacionCrediticia> reglas = this.repository.findByActivaAndPrioridadBetween(true, prioridadMinima, prioridadMaxima);
        if (reglas.isEmpty()) {
            throw new NotFoundException(prioridadMinima + "-" + prioridadMaxima, "ReglaEvaluacionCrediticia por prioridad");
        }
        return reglas;
    }

    public ReglaEvaluacionCrediticia create(ReglaEvaluacionCrediticia regla) {
        try {
            regla.setVersion(BigDecimal.ONE);
            return this.repository.save(regla);
        } catch (Exception e) {
            throw new CreateException("ReglaEvaluacionCrediticia", e.getMessage());
        }
    }

    public ReglaEvaluacionCrediticia update(ReglaEvaluacionCrediticia regla) {
        try {
            ReglaEvaluacionCrediticia existing = findById(regla.getIdRegla());
            regla.setVersion(existing.getVersion().add(BigDecimal.ONE));
            return this.repository.save(regla);
        } catch (Exception e) {
            throw new UpdateException("ReglaEvaluacionCrediticia", e.getMessage());
        }
    }

    /**
     * Activa o desactiva una regla
     */
    public ReglaEvaluacionCrediticia toggleActive(Long id) {
        try {
            ReglaEvaluacionCrediticia regla = findById(id);
            regla.setActiva(!regla.getActiva());
            return update(regla);
        } catch (Exception e) {
            throw new UpdateException("ReglaEvaluacionCrediticia", "Error al cambiar estado activo: " + e.getMessage());
        }
    }

    /**
     * Obtiene reglas ordenadas por prioridad para ejecución
     */
    public List<ReglaEvaluacionCrediticia> getReglasForExecution() {
        return this.repository.findByActivaTrueOrderByPrioridadAsc();
    }
} 