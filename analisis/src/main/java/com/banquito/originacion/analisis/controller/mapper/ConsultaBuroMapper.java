package com.banquito.originacion.analisis.controller.mapper;

import com.banquito.originacion.analisis.controller.dto.ConsultaBuroDTO;
import com.banquito.originacion.analisis.model.ConsultaBuro;

public class ConsultaBuroMapper {

    public static ConsultaBuro toEntity(ConsultaBuroDTO dto) {
        if (dto == null) {
            return null;
        }
        ConsultaBuro entity = new ConsultaBuro();
        entity.setIdConsultaBuro(dto.getIdConsultaBuro());
        entity.setIdClienteProspecto(dto.getIdClienteProspecto());
        entity.setScoreObtenido(dto.getScoreObtenido());
        entity.setFechaConsulta(dto.getFechaConsulta());
        entity.setDetalleDeudas(dto.getDetalleDeudas());
        entity.setEstadoConsulta(dto.getEstadoConsulta());
        entity.setVersion(dto.getVersion());
        return entity;
    }

    public static ConsultaBuroDTO toDTO(ConsultaBuro entity) {
        if (entity == null) {
            return null;
        }
        ConsultaBuroDTO dto = new ConsultaBuroDTO();
        dto.setIdConsultaBuro(entity.getIdConsultaBuro());
        dto.setIdClienteProspecto(entity.getIdClienteProspecto());
        dto.setScoreObtenido(entity.getScoreObtenido());
        dto.setFechaConsulta(entity.getFechaConsulta());
        dto.setDetalleDeudas(entity.getDetalleDeudas());
        dto.setEstadoConsulta(entity.getEstadoConsulta());
        dto.setVersion(entity.getVersion());
        return dto;
    }
} 