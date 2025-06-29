package com.banquito.originacion.analisis.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.banquito.originacion.analisis.exception.CreateException;
import com.banquito.originacion.analisis.exception.NotFoundException;
import com.banquito.originacion.analisis.model.ValidacionRegla;
import com.banquito.originacion.analisis.repository.ValidacionReglaRepository;

@Service
public class ValidacionReglaService {

    private final ValidacionReglaRepository repository;

    public ValidacionReglaService(ValidacionReglaRepository repository) {
        this.repository = repository;
    }

    public ValidacionRegla findById(Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new NotFoundException(id.toString(), "ValidacionRegla"));
    }

    public List<ValidacionRegla> findByEvaluacionCrediticia(Long idEvaluacionesCrediticias) {
        List<ValidacionRegla> validaciones = this.repository.findByEvaluacionCrediticia_IdEvaluacionesCrediticias(idEvaluacionesCrediticias);
        if (validaciones.isEmpty()) {
            throw new NotFoundException(idEvaluacionesCrediticias.toString(), "ValidacionRegla por evaluación");
        }
        return validaciones;
    }

    public List<ValidacionRegla> findByIdSolicitud(Integer idSolicitud) {
        List<ValidacionRegla> validaciones = this.repository.findByEvaluacionCrediticia_IdSolicitud(idSolicitud);
        if (validaciones.isEmpty()) {
            throw new NotFoundException(idSolicitud.toString(), "ValidacionRegla por solicitud");
        }
        return validaciones;
    }

    public List<ValidacionRegla> findByRegla(Long idRegla) {
        List<ValidacionRegla> validaciones = this.repository.findByRegla_IdRegla(idRegla);
        if (validaciones.isEmpty()) {
            throw new NotFoundException(idRegla.toString(), "ValidacionRegla por regla");
        }
        return validaciones;
    }

    public List<ValidacionRegla> findFailedValidationsByEvaluacion(Long idEvaluacionesCrediticias) {
        List<ValidacionRegla> validaciones = this.repository.findByEvaluacionCrediticia_IdEvaluacionesCrediticiasAndResultado(idEvaluacionesCrediticias, false);
        if (validaciones.isEmpty()) {
            throw new NotFoundException(idEvaluacionesCrediticias.toString(), "ValidacionRegla fallidas por evaluación");
        }
        return validaciones;
    }

    public List<ValidacionRegla> findFailedValidationsBySolicitud(Integer idSolicitud) {
        List<ValidacionRegla> validaciones = this.repository.findByEvaluacionCrediticia_IdSolicitudAndResultado(idSolicitud, false);
        if (validaciones.isEmpty()) {
            throw new NotFoundException(idSolicitud.toString(), "ValidacionRegla fallidas por solicitud");
        }
        return validaciones;
    }

    public ValidacionRegla create(ValidacionRegla validacion) {
        try {
            validacion.setFecha(LocalDateTime.now());
            return this.repository.save(validacion);
        } catch (Exception e) {
            throw new CreateException("ValidacionRegla", e.getMessage());
        }
    }
} 