package com.banquito.originacion.analisis.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import com.banquito.originacion.analisis.model.InformeBuro;
import com.banquito.originacion.analisis.controller.dto.InformeBuroDTO;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface InformeBuroMapper {
    InformeBuroDTO toDTO(InformeBuro model);
    InformeBuro toModel(InformeBuroDTO dto);
} 