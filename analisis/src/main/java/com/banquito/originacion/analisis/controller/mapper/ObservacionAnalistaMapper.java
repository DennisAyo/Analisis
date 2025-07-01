package com.banquito.originacion.analisis.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import com.banquito.originacion.analisis.model.ObservacionAnalista;
import com.banquito.originacion.analisis.controller.dto.ObservacionAnalistaDTO;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ObservacionAnalistaMapper {
    ObservacionAnalistaDTO toDTO(ObservacionAnalista model);
    ObservacionAnalista toModel(ObservacionAnalistaDTO dto);
} 