package com.banquito.originacion.analisis.controller.mapper;

import com.banquito.originacion.analisis.controller.dto.ReglaEvaluacionCrediticiaDTO;
import com.banquito.originacion.analisis.model.ReglaEvaluacionCrediticia;

public class ReglaEvaluacionCrediticiaMapper {

    public static ReglaEvaluacionCrediticia toEntity(ReglaEvaluacionCrediticiaDTO dto) {
        if (dto == null) {
            return null;
        }
        ReglaEvaluacionCrediticia entity = new ReglaEvaluacionCrediticia();
        entity.setIdRegla(dto.getIdRegla());
        entity.setNombre(dto.getNombre());
        entity.setDescripcion(dto.getDescripcion());
        entity.setCondicion(dto.getCondicion());
        entity.setPrioridad(dto.getPrioridad());
        entity.setActiva(dto.getActiva());
        entity.setVersion(dto.getVersion());
        return entity;
    }

    public static ReglaEvaluacionCrediticiaDTO toDTO(ReglaEvaluacionCrediticia entity) {
        if (entity == null) {
            return null;
        }
        ReglaEvaluacionCrediticiaDTO dto = new ReglaEvaluacionCrediticiaDTO();
        dto.setIdRegla(entity.getIdRegla());
        dto.setNombre(entity.getNombre());
        dto.setDescripcion(entity.getDescripcion());
        dto.setCondicion(entity.getCondicion());
        dto.setPrioridad(entity.getPrioridad());
        dto.setActiva(entity.getActiva());
        dto.setVersion(entity.getVersion());
        return dto;
    }
} 