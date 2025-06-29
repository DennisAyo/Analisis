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

    public List<ObservacionAnalista> findByIdSolicitud(Integer idSolicitud) {
        List<ObservacionAnalista> observaciones = this.repository.findByIdSolicitudOrderByFechaHoraDesc(idSolicitud);
        if (observaciones.isEmpty()) {
            throw new NotFoundException(idSolicitud.toString(), "ObservacionAnalista por solicitud");
        }
        return observaciones;
    }

    public List<ObservacionAnalista> findByUsuario(String usuario) {
        List<ObservacionAnalista> observaciones = this.repository.findByUsuarioOrderByFechaHoraDesc(usuario);
        if (observaciones.isEmpty()) {
            throw new NotFoundException(usuario, "ObservacionAnalista por usuario");
        }
        return observaciones;
    }

    public List<ObservacionAnalista> findByIdSolicitudAndUsuario(Integer idSolicitud, String usuario) {
        List<ObservacionAnalista> observaciones = this.repository.findByIdSolicitudAndUsuario(idSolicitud, usuario);
        if (observaciones.isEmpty()) {
            throw new NotFoundException(idSolicitud + " - " + usuario, "ObservacionAnalista por solicitud y usuario");
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
            ObservacionAnalista existing = findById(observacionAnalista.getIdObservacionAnalista());
            observacionAnalista.setVersion(existing.getVersion().add(BigDecimal.ONE));
            return this.repository.save(observacionAnalista);
        } catch (Exception e) {
            throw new UpdateException("ObservacionAnalista", e.getMessage());
        }
    }
} 