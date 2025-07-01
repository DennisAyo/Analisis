package com.banquito.originacion.analisis.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.banquito.originacion.analisis.repository.ConsultaBuroRepository;
import com.banquito.originacion.analisis.repository.HistorialEstadoRepository;
import com.banquito.originacion.analisis.exception.*;
import com.banquito.originacion.analisis.model.*;
import java.util.Optional;
import java.util.List;

@Service
public class ConsultaService {
    private final ConsultaBuroRepository consultaBuroRepository;
    private final HistorialEstadoRepository historialEstadoRepository;

    public ConsultaService(ConsultaBuroRepository consultaBuroRepository,
                          HistorialEstadoRepository historialEstadoRepository) {
        this.consultaBuroRepository = consultaBuroRepository;
        this.historialEstadoRepository = historialEstadoRepository;
    }

    @Transactional
    public ConsultaBuro crearConsulta(ConsultaBuro consulta) {
        boolean existe = consultaBuroRepository
            .findAll()
            .stream()
            .anyMatch(c -> c.getIdSolicitud().equals(consulta.getIdSolicitud())
                        && c.getFechaConsulta().toLocalDate().equals(consulta.getFechaConsulta().toLocalDate()));
        if (existe) {
            throw new BusinessRuleException("idSolicitud: " + consulta.getIdSolicitud(), "ConsultaBuro");
        }
        return consultaBuroRepository.save(consulta);
    }

    @Transactional(readOnly = true)
    public Optional<ConsultaBuro> obtenerConsultaPorId(Long id) {
        return consultaBuroRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<ConsultaBuro> obtenerTodasConsultas() {
        return consultaBuroRepository.findAll();
    }

    @Transactional
    public ConsultaBuro actualizarConsulta(Long id, ConsultaBuro consulta) {
        ConsultaBuro existente = consultaBuroRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("ConsultaBuro", id.toString()));
        existente.setIdSolicitud(consulta.getIdSolicitud());
        existente.setFechaConsulta(consulta.getFechaConsulta());
        existente.setEstadoConsulta(consulta.getEstadoConsulta());
        existente.setScoreExterno(consulta.getScoreExterno());
        existente.setCuentasActivas(consulta.getCuentasActivas());
        existente.setCuentasMorosas(consulta.getCuentasMorosas());
        existente.setMontoMorosoTotal(consulta.getMontoMorosoTotal());
        existente.setDiasMoraPromedio(consulta.getDiasMoraPromedio());
        existente.setFechaPrimeraMora(consulta.getFechaPrimeraMora());
        existente.setDatosBuroEncriptado(consulta.getDatosBuroEncriptado());
        return consultaBuroRepository.save(existente);
    }

    @Transactional
    public void eliminarConsulta(Long id) {
        if (!consultaBuroRepository.existsById(id)) {
            throw new NotFoundException("ConsultaBuro", id.toString());
        }
        consultaBuroRepository.deleteById(id);
    }

    @Transactional
    public HistorialEstado crearHistorialEstado(HistorialEstado historial) {
        if (historial.getId() == null || historial.getId().getIdConsulta() == null) {
            throw new BusinessRuleException("idConsulta: null", "HistorialEstado");
        }
        if (!consultaBuroRepository.existsById(historial.getId().getIdConsulta())) {
            throw new NotFoundException("ConsultaBuro", historial.getId().getIdConsulta().toString());
        }
        return historialEstadoRepository.save(historial);
    }

    @Transactional(readOnly = true)
    public List<HistorialEstado> obtenerHistorialPorConsulta(Long idConsulta) {
        return historialEstadoRepository.findAll().stream()
            .filter(h -> h.getId().getIdConsulta().equals(idConsulta))
            .toList();
    }

    @Transactional
    public void eliminarHistorial(Long idHistorial, Long idConsulta) {
        HistorialEstado.HistorialEstadoId historialId = new HistorialEstado.HistorialEstadoId(idHistorial, idConsulta);
        if (!historialEstadoRepository.existsById(historialId)) {
            throw new NotFoundException("HistorialEstado", historialId.toString());
        }
        historialEstadoRepository.deleteById(historialId);
    }

    // Métodos adicionales para actualizar estado, consultar historial, validar reglas de transición, SLA, etc.
} 