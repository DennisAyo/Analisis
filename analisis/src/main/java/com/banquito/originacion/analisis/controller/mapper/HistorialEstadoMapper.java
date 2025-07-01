package com.banquito.originacion.analisis.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import com.banquito.originacion.analisis.model.HistorialEstado;
import com.banquito.originacion.analisis.controller.dto.HistorialEstadoDTO;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface HistorialEstadoMapper {
    HistorialEstadoDTO toDTO(HistorialEstado model);
    HistorialEstado toModel(HistorialEstadoDTO dto);
} 