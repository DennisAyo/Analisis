package com.banquito.originacion.analisis.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.banquito.originacion.analisis.exception.CreateException;
import com.banquito.originacion.analisis.exception.NotFoundException;
import com.banquito.originacion.analisis.exception.UpdateException;
import com.banquito.originacion.analisis.model.ObservacionAnalista;
import com.banquito.originacion.analisis.repository.ObservacionAnalistaRepository;

@Service
public class ObservacionAnalistaService {

    private final ObservacionAnalistaRepository repository;

    public ObservacionAnalistaService(ObservacionAnalistaRepository repository) {
        this.repository = repository;
    }

    public List<ObservacionAnalista> findAll() {
        return this.repository.findAll();
    }

    public ObservacionAnalista findById(Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new NotFoundException(id.toString(), "ObservacionAnalista"));
    }

    public List<ObservacionAnalista> findByIdEvaluacion(BigDecimal idEvaluacion) {
        List<ObservacionAnalista> observaciones = this.repository.findByIdEvaluacionOrderByFechaHoraDesc(idEvaluacion);
        if (observaciones.isEmpty()) {
            throw new NotFoundException(idEvaluacion.toString(), "ObservacionAnalista por evaluación");
        }
        return observaciones;
    }

    public List<ObservacionAnalista> findByIdUsuario(BigDecimal idUsuario) {
        List<ObservacionAnalista> observaciones = this.repository.findByIdUsuarioOrderByFechaHoraDesc(idUsuario);
        if (observaciones.isEmpty()) {
            throw new NotFoundException(idUsuario.toString(), "ObservacionAnalista por usuario");
        }
        return observaciones;
    }

    public List<ObservacionAnalista> findByIdEvaluacionAndIdUsuario(BigDecimal idEvaluacion, BigDecimal idUsuario) {
        List<ObservacionAnalista> observaciones = this.repository.findByIdEvaluacionAndIdUsuario(idEvaluacion, idUsuario);
        if (observaciones.isEmpty()) {
            throw new NotFoundException(idEvaluacion + "-" + idUsuario, "ObservacionAnalista por evaluación y usuario");
        }
        return observaciones;
    }

    public List<ObservacionAnalista> findByRazonIntervencion(String razonIntervencion) {
        List<ObservacionAnalista> observaciones = this.repository.findByRazonIntervencionAutoEnum(razonIntervencion);
        if (observaciones.isEmpty()) {
            throw new NotFoundException(razonIntervencion, "ObservacionAnalista por razón de intervención");
        }
        return observaciones;
    }

    public ObservacionAnalista create(ObservacionAnalista observacionAnalista) {
        try {
            observacionAnalista.setFechaHora(LocalDateTime.now());
            observacionAnalista.setVersion(BigDecimal.ONE);
            return this.repository.save(observacionAnalista);
        } catch (Exception e) {
            throw new CreateException("ObservacionAnalista", e.getMessage());
        }
    }

    public ObservacionAnalista update(ObservacionAnalista observacionAnalista) {
        try {
            ObservacionAnalista existing = findById(observacionAnalista.getIdObservacion());
            observacionAnalista.setVersion(existing.getVersion().add(BigDecimal.ONE));
            return this.repository.save(observacionAnalista);
        } catch (Exception e) {
            throw new UpdateException("ObservacionAnalista", e.getMessage());
        }
    }

    public void delete(Long id) {
        try {
            ObservacionAnalista observacionAnalista = findById(id);
            this.repository.delete(observacionAnalista);
        } catch (Exception e) {
            throw new UpdateException("ObservacionAnalista", "Error al eliminar: " + e.getMessage());
        }
    }
} 