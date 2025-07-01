package com.banquito.originacion.analisis.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.banquito.originacion.analisis.repository.*;
import com.banquito.originacion.analisis.exception.*;
import com.banquito.originacion.analisis.model.*;
import java.util.Optional;
import java.util.List;

@Service
public class ComponenteAnalisisService {
    private final InformeBuroRepository informeBuroRepository;
    private final EvaluacionCrediticiaRepository evaluacionCrediticiaRepository;
    private final ObservacionAnalistaRepository observacionAnalistaRepository;
    private final ConsultaBuroRepository consultaBuroRepository;

    public ComponenteAnalisisService(
        InformeBuroRepository informeBuroRepository,
        EvaluacionCrediticiaRepository evaluacionCrediticiaRepository,
        ObservacionAnalistaRepository observacionAnalistaRepository,
        ConsultaBuroRepository consultaBuroRepository
    ) {
        this.informeBuroRepository = informeBuroRepository;
        this.evaluacionCrediticiaRepository = evaluacionCrediticiaRepository;
        this.observacionAnalistaRepository = observacionAnalistaRepository;
        this.consultaBuroRepository = consultaBuroRepository;
    }

    @Transactional
    public InformeBuro crearInformeBuro(InformeBuro informe) {
        if (informe.getIdConsultaBuro() == null) {
            throw new BusinessRuleException("idConsultaBuro: null", "InformeBuro");
        }
        if (!consultaBuroRepository.existsById(informe.getIdConsultaBuro())) {
            throw new NotFoundException("ConsultaBuro", informe.getIdConsultaBuro().toString());
        }
        return informeBuroRepository.save(informe);
    }

    @Transactional(readOnly = true)
    public Optional<InformeBuro> obtenerInformePorId(Long id) {
        return informeBuroRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<InformeBuro> obtenerTodosInformes() {
        return informeBuroRepository.findAll();
    }

    @Transactional
    public InformeBuro actualizarInforme(Long id, InformeBuro informe) {
        InformeBuro existente = informeBuroRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("InformeBuro", id.toString()));
        existente.setIdConsultaBuro(informe.getIdConsultaBuro());
        existente.setScore(informe.getScore());
        existente.setNumeroDeudaImpagas(informe.getNumeroDeudaImpagas());
        existente.setMontoTotalAdeudado(informe.getMontoTotalAdeudado());
        existente.setCapacidadPagoReportada(informe.getCapacidadPagoReportada());
        existente.setJsonRespuestaCompleta(informe.getJsonRespuestaCompleta());
        return informeBuroRepository.save(existente);
    }

    @Transactional
    public void eliminarInforme(Long id) {
        if (!informeBuroRepository.existsById(id)) {
            throw new NotFoundException("InformeBuro", id.toString());
        }
        informeBuroRepository.deleteById(id);
    }

    @Transactional
    public EvaluacionCrediticia crearEvaluacionCrediticia(EvaluacionCrediticia evaluacion) {
        if (evaluacion.getIdInformeBuro() == null) {
            throw new BusinessRuleException("idInformeBuro: null", "EvaluacionCrediticia");
        }
        if (!informeBuroRepository.existsById(evaluacion.getIdInformeBuro())) {
            throw new NotFoundException("InformeBuro", evaluacion.getIdInformeBuro().toString());
        }
        return evaluacionCrediticiaRepository.save(evaluacion);
    }

    @Transactional(readOnly = true)
    public Optional<EvaluacionCrediticia> obtenerEvaluacionPorId(Long id) {
        return evaluacionCrediticiaRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<EvaluacionCrediticia> obtenerTodasEvaluaciones() {
        return evaluacionCrediticiaRepository.findAll();
    }

    @Transactional
    public EvaluacionCrediticia actualizarEvaluacion(Long id, EvaluacionCrediticia evaluacion) {
        EvaluacionCrediticia existente = evaluacionCrediticiaRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("EvaluacionCrediticia", id.toString()));
        existente.setIdSolicitud(evaluacion.getIdSolicitud());
        existente.setIdInformeBuro(evaluacion.getIdInformeBuro());
        existente.setFechaEvaluacion(evaluacion.getFechaEvaluacion());
        existente.setScoreInternoCalculado(evaluacion.getScoreInternoCalculado());
        existente.setResultadoAutomatico(evaluacion.getResultadoAutomatico());
        existente.setObservacionesMotorReglas(evaluacion.getObservacionesMotorReglas());
        existente.setDecisionFinalAnalista(evaluacion.getDecisionFinalAnalista());
        existente.setJustificacionAnalista(evaluacion.getJustificacionAnalista());
        return evaluacionCrediticiaRepository.save(existente);
    }

    @Transactional
    public void eliminarEvaluacion(Long id) {
        if (!evaluacionCrediticiaRepository.existsById(id)) {
            throw new NotFoundException("EvaluacionCrediticia", id.toString());
        }
        evaluacionCrediticiaRepository.deleteById(id);
    }

    @Transactional
    public ObservacionAnalista crearObservacionAnalista(ObservacionAnalista observacion) {
        if (observacion.getIdEvaluacion() == null) {
            throw new BusinessRuleException("idEvaluacion: null", "ObservacionAnalista");
        }
        return observacionAnalistaRepository.save(observacion);
    }

    @Transactional(readOnly = true)
    public Optional<ObservacionAnalista> obtenerObservacionPorId(Long id) {
        return observacionAnalistaRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<ObservacionAnalista> obtenerTodasObservaciones() {
        return observacionAnalistaRepository.findAll();
    }

    @Transactional
    public ObservacionAnalista actualizarObservacion(Long id, ObservacionAnalista observacion) {
        ObservacionAnalista existente = observacionAnalistaRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("ObservacionAnalista", id.toString()));
        existente.setIdEvaluacion(observacion.getIdEvaluacion());
        existente.setIdUsuario(observacion.getIdUsuario());
        existente.setRazonIntervencionAutoEnum(observacion.getRazonIntervencionAutoEnum());
        existente.setJustificacion(observacion.getJustificacion());
        existente.setFechaHora(observacion.getFechaHora());
        return observacionAnalistaRepository.save(existente);
    }

    @Transactional
    public void eliminarObservacion(Long id) {
        if (!observacionAnalistaRepository.existsById(id)) {
            throw new NotFoundException("ObservacionAnalista", id.toString());
        }
        observacionAnalistaRepository.deleteById(id);
    }
} 