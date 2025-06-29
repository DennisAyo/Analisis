package com.banquito.originacion.analisis.controller.mapper;

import com.banquito.originacion.analisis.controller.dto.EvaluacionCrediticiaDTO;
import com.banquito.originacion.analisis.model.ConsultaBuro;
import com.banquito.originacion.analisis.model.EvaluacionCrediticia;

public class EvaluacionCrediticiaMapper {

    public static EvaluacionCrediticia toEntity(EvaluacionCrediticiaDTO dto) {
        if (dto == null) {
            return null;
        }
        EvaluacionCrediticia entity = new EvaluacionCrediticia();
        entity.setIdEvaluacionesCrediticias(dto.getIdEvaluacionesCrediticias());
        entity.setIdSolicitud(dto.getIdSolicitud());
        
        // Mapear la relación FK con ConsultaBuro
        if (dto.getIdConsultaBuro() != null) {
            entity.setConsultaBuro(new ConsultaBuro(dto.getIdConsultaBuro()));
        }
        
        entity.setScoreInterno(dto.getScoreInterno());
        entity.setCategoriaRiesgo(dto.getCategoriaRiesgo());
        entity.setEsAutomatico(dto.getEsAutomatico());
        entity.setFechaEvaluacion(dto.getFechaEvaluacion());
        entity.setVersion(dto.getVersion());
        return entity;
    }

    public static EvaluacionCrediticiaDTO toDTO(EvaluacionCrediticia entity) {
        if (entity == null) {
            return null;
        }
        EvaluacionCrediticiaDTO dto = new EvaluacionCrediticiaDTO();
        dto.setIdEvaluacionesCrediticias(entity.getIdEvaluacionesCrediticias());
        dto.setIdSolicitud(entity.getIdSolicitud());
        
        // Mapear el ID de la consulta de buró
        dto.setIdConsultaBuro(entity.getConsultaBuro() != null ? 
            entity.getConsultaBuro().getIdConsultaBuro() : null);
        
        // Mapear la información básica de consulta de buró
        dto.setConsultaBuro(entity.getConsultaBuro() != null ? 
            toConsultaBuroBasicDTO(entity.getConsultaBuro()) : null);
        
        dto.setScoreInterno(entity.getScoreInterno());
        dto.setCategoriaRiesgo(entity.getCategoriaRiesgo());
        dto.setEsAutomatico(entity.getEsAutomatico());
        dto.setFechaEvaluacion(entity.getFechaEvaluacion());
        dto.setVersion(entity.getVersion());
        return dto;
    }

    private static EvaluacionCrediticiaDTO.ConsultaBuroBasicDTO toConsultaBuroBasicDTO(ConsultaBuro consultaBuro) {
        if (consultaBuro == null) {
            return null;
        }
        EvaluacionCrediticiaDTO.ConsultaBuroBasicDTO dto = new EvaluacionCrediticiaDTO.ConsultaBuroBasicDTO();
        dto.setIdConsultaBuro(consultaBuro.getIdConsultaBuro());
        dto.setScoreObtenido(consultaBuro.getScoreObtenido());
        dto.setEstadoConsulta(consultaBuro.getEstadoConsulta());
        return dto;
    }
} 