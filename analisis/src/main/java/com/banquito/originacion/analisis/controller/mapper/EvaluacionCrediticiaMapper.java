package com.banquito.originacion.analisis.controller.mapper;

import com.banquito.originacion.analisis.controller.dto.EvaluacionCrediticiaDTO;
import com.banquito.originacion.analisis.model.InformeBuro;
import com.banquito.originacion.analisis.model.EvaluacionCrediticia;

public class EvaluacionCrediticiaMapper {

    public static EvaluacionCrediticia toEntity(EvaluacionCrediticiaDTO dto) {
        if (dto == null) {
            return null;
        }
        EvaluacionCrediticia entity = new EvaluacionCrediticia();
        entity.setIdEvaluacion(dto.getIdEvaluacion());
        entity.setIdSolicitud(dto.getIdSolicitud());
        
        if (dto.getIdInformeBuro() != null) {
            entity.setInformeBuro(new InformeBuro(dto.getIdInformeBuro()));
        }
        
        entity.setFechaEvaluacion(dto.getFechaEvaluacion());
        entity.setScoreInternoCalculado(dto.getScoreInternoCalculado());
        entity.setResultadoAutomatico(dto.getResultadoAutomatico());
        entity.setObservacionesMotorReglas(dto.getObservacionesMotorReglas());
        entity.setDecisionFinalAnalista(dto.getDecisionFinalAnalista());
        entity.setJustificacionAnalista(dto.getJustificacionAnalista());
        entity.setVersion(dto.getVersion());
        return entity;
    }

    public static EvaluacionCrediticiaDTO toDTO(EvaluacionCrediticia entity) {
        if (entity == null) {
            return null;
        }
        EvaluacionCrediticiaDTO dto = new EvaluacionCrediticiaDTO();
        dto.setIdEvaluacion(entity.getIdEvaluacion());
        dto.setIdSolicitud(entity.getIdSolicitud());
    
        dto.setIdInformeBuro(entity.getInformeBuro() != null ? 
            entity.getInformeBuro().getIdInformeBuro() : null);
        
        dto.setInformeBuro(entity.getInformeBuro() != null ? 
            toInformeBuroBasicDTO(entity.getInformeBuro()) : null);
        
        dto.setFechaEvaluacion(entity.getFechaEvaluacion());
        dto.setScoreInternoCalculado(entity.getScoreInternoCalculado());
        dto.setResultadoAutomatico(entity.getResultadoAutomatico());
        dto.setObservacionesMotorReglas(entity.getObservacionesMotorReglas());
        dto.setDecisionFinalAnalista(entity.getDecisionFinalAnalista());
        dto.setJustificacionAnalista(entity.getJustificacionAnalista());
        dto.setVersion(entity.getVersion());
        return dto;
    }

    private static EvaluacionCrediticiaDTO.InformeBuroBasicDTO toInformeBuroBasicDTO(InformeBuro informeBuro) {
        if (informeBuro == null) {
            return null;
        }
        EvaluacionCrediticiaDTO.InformeBuroBasicDTO dto = new EvaluacionCrediticiaDTO.InformeBuroBasicDTO();
        dto.setIdInformeBuro(informeBuro.getIdInformeBuro());
        dto.setScore(informeBuro.getScore());
        dto.setNumeroDeudaImpagas(informeBuro.getNumeroDeudaImpagas());
        dto.setMontoTotalAdeudado(informeBuro.getMontoTotalAdeudado());
        return dto;
    }
} 