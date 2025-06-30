package com.banquito.originacion.analisis.controller.mapper;

import com.banquito.originacion.analisis.controller.dto.InformeBuroDTO;
import com.banquito.originacion.analisis.model.ConsultaBuro;
import com.banquito.originacion.analisis.model.InformeBuro;

public class InformeBuroMapper {

    public static InformeBuro toEntity(InformeBuroDTO dto) {
        if (dto == null) {
            return null;
        }
        InformeBuro entity = new InformeBuro();
        entity.setIdInformeBuro(dto.getIdInformeBuro());
        
        if (dto.getIdConsultaBuro() != null) {
            entity.setConsultaBuro(new ConsultaBuro(dto.getIdConsultaBuro()));
        }
        
        entity.setScore(dto.getScore());
        entity.setNumeroDeudaImpagas(dto.getNumeroDeudaImpagas());
        entity.setMontoTotalAdeudado(dto.getMontoTotalAdeudado());
        entity.setCapacidadPagoReportada(dto.getCapacidadPagoReportada());
        entity.setJsonRespuestaCompleta(dto.getJsonRespuestaCompleta());
        entity.setVersion(dto.getVersion());
        return entity;
    }

    public static InformeBuroDTO toDTO(InformeBuro entity) {
        if (entity == null) {
            return null;
        }
        InformeBuroDTO dto = new InformeBuroDTO();
        dto.setIdInformeBuro(entity.getIdInformeBuro());
        
        dto.setIdConsultaBuro(entity.getConsultaBuro() != null ? 
            entity.getConsultaBuro().getIdConsulta() : null);
        
        dto.setConsultaBuro(entity.getConsultaBuro() != null ? 
            toConsultaBuroBasicDTO(entity.getConsultaBuro()) : null);
        
        dto.setScore(entity.getScore());
        dto.setNumeroDeudaImpagas(entity.getNumeroDeudaImpagas());
        dto.setMontoTotalAdeudado(entity.getMontoTotalAdeudado());
        dto.setCapacidadPagoReportada(entity.getCapacidadPagoReportada());
        dto.setJsonRespuestaCompleta(entity.getJsonRespuestaCompleta());
        dto.setVersion(entity.getVersion());
        return dto;
    }

    private static InformeBuroDTO.ConsultaBuroBasicDTO toConsultaBuroBasicDTO(ConsultaBuro consultaBuro) {
        if (consultaBuro == null) {
            return null;
        }
        InformeBuroDTO.ConsultaBuroBasicDTO dto = new InformeBuroDTO.ConsultaBuroBasicDTO();
        dto.setIdConsulta(consultaBuro.getIdConsulta());
        dto.setIdSolicitud(consultaBuro.getIdSolicitud());
        dto.setScoreExterno(consultaBuro.getScoreExterno());
        dto.setEstadoConsulta(consultaBuro.getEstadoConsulta());
        return dto;
    }
} 