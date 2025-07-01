package com.banquito.originacion.analisis.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import com.banquito.originacion.analisis.model.EvaluacionCrediticia;
import com.banquito.originacion.analisis.controller.dto.EvaluacionCrediticiaDTO;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface EvaluacionCrediticiaMapper {
    EvaluacionCrediticiaDTO toDTO(EvaluacionCrediticia model);
    EvaluacionCrediticia toModel(EvaluacionCrediticiaDTO dto);
} 