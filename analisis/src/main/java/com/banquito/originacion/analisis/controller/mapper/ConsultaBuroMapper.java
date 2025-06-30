package com.banquito.originacion.analisis.controller.mapper;

import com.banquito.originacion.analisis.controller.dto.ConsultaBuroDTO;
import com.banquito.originacion.analisis.model.ConsultaBuro;

public class ConsultaBuroMapper {

    public static ConsultaBuro toEntity(ConsultaBuroDTO dto) {
        if (dto == null) {
            return null;
        }
        ConsultaBuro entity = new ConsultaBuro();
        entity.setIdConsulta(dto.getIdConsulta());
        entity.setIdSolicitud(dto.getIdSolicitud());
        entity.setFechaConsulta(dto.getFechaConsulta());
        entity.setEstadoConsulta(dto.getEstadoConsulta());
        entity.setScoreExterno(dto.getScoreExterno());
        entity.setCuentasActivas(dto.getCuentasActivas());
        entity.setCuentasMorosas(dto.getCuentasMorosas());
        entity.setMontoMorosoTotal(dto.getMontoMorosoTotal());
        entity.setDiasMoraPromedio(dto.getDiasMoraPromedio());
        entity.setFechaPrimeraMora(dto.getFechaPrimeraMora());
        entity.setVersion(dto.getVersion());
        return entity;
    }

    public static ConsultaBuroDTO toDTO(ConsultaBuro entity) {
        if (entity == null) {
            return null;
        }
        ConsultaBuroDTO dto = new ConsultaBuroDTO();
        dto.setIdConsulta(entity.getIdConsulta());
        dto.setIdSolicitud(entity.getIdSolicitud());
        dto.setFechaConsulta(entity.getFechaConsulta());
        dto.setEstadoConsulta(entity.getEstadoConsulta());
        dto.setScoreExterno(entity.getScoreExterno());
        dto.setCuentasActivas(entity.getCuentasActivas());
        dto.setCuentasMorosas(entity.getCuentasMorosas());
        dto.setMontoMorosoTotal(entity.getMontoMorosoTotal());
        dto.setDiasMoraPromedio(entity.getDiasMoraPromedio());
        dto.setFechaPrimeraMora(entity.getFechaPrimeraMora());
        dto.setDatosBuroEncriptadoInfo(entity.getDatosBuroEncriptado() != null ? 
            "Datos encriptados disponibles" : "Sin datos encriptados");
        dto.setVersion(entity.getVersion());
        return dto;
    }
} 