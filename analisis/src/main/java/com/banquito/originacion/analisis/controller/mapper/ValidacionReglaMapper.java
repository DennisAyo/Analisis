package com.banquito.originacion.analisis.controller.mapper;

import com.banquito.originacion.analisis.controller.dto.ValidacionReglaDTO;
import com.banquito.originacion.analisis.model.EvaluacionCrediticia;
import com.banquito.originacion.analisis.model.ReglaEvaluacionCrediticia;
import com.banquito.originacion.analisis.model.ValidacionRegla;

public class ValidacionReglaMapper {

    public static ValidacionRegla toEntity(ValidacionReglaDTO dto) {
        if (dto == null) {
            return null;
        }
        ValidacionRegla entity = new ValidacionRegla();
        entity.setIdValidacionRegla(dto.getIdValidacionRegla());
        
        // Mapear la relación FK con EvaluacionCrediticia
        if (dto.getIdEvaluacionesCrediticias() != null) {
            entity.setEvaluacionCrediticia(new EvaluacionCrediticia(dto.getIdEvaluacionesCrediticias()));
        }
        
        // Mapear la relación FK con ReglaEvaluacionCrediticia
        if (dto.getIdRegla() != null) {
            entity.setRegla(new ReglaEvaluacionCrediticia(dto.getIdRegla()));
        }
        
        entity.setResultado(dto.getResultado());
        entity.setFecha(dto.getFecha());
        return entity;
    }

    public static ValidacionReglaDTO toDTO(ValidacionRegla entity) {
        if (entity == null) {
            return null;
        }
        ValidacionReglaDTO dto = new ValidacionReglaDTO();
        dto.setIdValidacionRegla(entity.getIdValidacionRegla());
        
        // Mapear los IDs de las relaciones FK
        dto.setIdEvaluacionesCrediticias(entity.getEvaluacionCrediticia() != null ? 
            entity.getEvaluacionCrediticia().getIdEvaluacionesCrediticias() : null);
        dto.setIdRegla(entity.getRegla() != null ? 
            entity.getRegla().getIdRegla() : null);
        
        // Mapear la información básica de evaluación crediticia
        dto.setEvaluacionCrediticia(entity.getEvaluacionCrediticia() != null ? 
            toEvaluacionBasicDTO(entity.getEvaluacionCrediticia()) : null);
        
        // Mapear la información básica de regla
        dto.setRegla(entity.getRegla() != null ? 
            toReglaBasicDTO(entity.getRegla()) : null);
        
        dto.setResultado(entity.getResultado());
        dto.setFecha(entity.getFecha());
        return dto;
    }

    private static ValidacionReglaDTO.EvaluacionBasicDTO toEvaluacionBasicDTO(EvaluacionCrediticia evaluacionCrediticia) {
        if (evaluacionCrediticia == null) {
            return null;
        }
        ValidacionReglaDTO.EvaluacionBasicDTO dto = new ValidacionReglaDTO.EvaluacionBasicDTO();
        dto.setIdEvaluacionesCrediticias(evaluacionCrediticia.getIdEvaluacionesCrediticias());
        dto.setIdSolicitud(evaluacionCrediticia.getIdSolicitud());
        dto.setCategoriaRiesgo(evaluacionCrediticia.getCategoriaRiesgo());
        return dto;
    }

    private static ValidacionReglaDTO.ReglaBasicDTO toReglaBasicDTO(ReglaEvaluacionCrediticia regla) {
        if (regla == null) {
            return null;
        }
        ValidacionReglaDTO.ReglaBasicDTO dto = new ValidacionReglaDTO.ReglaBasicDTO();
        dto.setIdRegla(regla.getIdRegla());
        dto.setNombre(regla.getNombre());
        dto.setPrioridad(regla.getPrioridad());
        return dto;
    }
} 