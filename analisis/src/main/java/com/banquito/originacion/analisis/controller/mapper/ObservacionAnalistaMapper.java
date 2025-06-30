package com.banquito.originacion.analisis.controller.mapper;

import org.springframework.stereotype.Component;

import com.banquito.originacion.analisis.controller.dto.ObservacionAnalistaDTO;
import com.banquito.originacion.analisis.model.ObservacionAnalista;

@Component
public class ObservacionAnalistaMapper {

    public static ObservacionAnalista toEntity(ObservacionAnalistaDTO dto) {
        if (dto == null) {
            return null;
        }
        ObservacionAnalista entity = new ObservacionAnalista();
        entity.setIdObservacion(dto.getIdObservacion());
        entity.setIdEvaluacion(dto.getIdEvaluacion());
        entity.setIdUsuario(dto.getIdUsuario());
        entity.setRazonIntervencionAutoEnum(dto.getRazonIntervencionAutoEnum());
        entity.setJustificacion(dto.getJustificacion());
        entity.setFechaHora(dto.getFechaHora());
        entity.setVersion(dto.getVersion());
        return entity;
    }

    public static ObservacionAnalistaDTO toDTO(ObservacionAnalista entity) {
        if (entity == null) {
            return null;
        }
        ObservacionAnalistaDTO dto = new ObservacionAnalistaDTO();
        dto.setIdObservacion(entity.getIdObservacion());
        dto.setIdEvaluacion(entity.getIdEvaluacion());
        dto.setIdUsuario(entity.getIdUsuario());
        dto.setRazonIntervencionAutoEnum(entity.getRazonIntervencionAutoEnum());
        dto.setJustificacion(entity.getJustificacion());
        dto.setFechaHora(entity.getFechaHora());
        dto.setVersion(entity.getVersion());
        return dto;
    }
} 