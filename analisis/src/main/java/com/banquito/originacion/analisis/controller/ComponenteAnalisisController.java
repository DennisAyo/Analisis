package com.banquito.originacion.analisis.controller;

import com.banquito.originacion.analisis.service.ComponenteAnalisisService;
import com.banquito.originacion.analisis.controller.dto.InformeBuroDTO;
import com.banquito.originacion.analisis.controller.dto.EvaluacionCrediticiaDTO;
import com.banquito.originacion.analisis.controller.dto.ObservacionAnalistaDTO;
import com.banquito.originacion.analisis.controller.mapper.InformeBuroMapper;
import com.banquito.originacion.analisis.controller.mapper.EvaluacionCrediticiaMapper;
import com.banquito.originacion.analisis.controller.mapper.ObservacionAnalistaMapper;
import com.banquito.originacion.analisis.model.InformeBuro;
import com.banquito.originacion.analisis.model.EvaluacionCrediticia;
import com.banquito.originacion.analisis.model.ObservacionAnalista;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ComponenteAnalisisController {
    private final ComponenteAnalisisService componenteAnalisisService;
    private final InformeBuroMapper informeBuroMapper;
    private final EvaluacionCrediticiaMapper evaluacionCrediticiaMapper;
    private final ObservacionAnalistaMapper observacionAnalistaMapper;

    public ComponenteAnalisisController(ComponenteAnalisisService componenteAnalisisService,
                                        InformeBuroMapper informeBuroMapper,
                                        EvaluacionCrediticiaMapper evaluacionCrediticiaMapper,
                                        ObservacionAnalistaMapper observacionAnalistaMapper) {
        this.componenteAnalisisService = componenteAnalisisService;
        this.informeBuroMapper = informeBuroMapper;
        this.evaluacionCrediticiaMapper = evaluacionCrediticiaMapper;
        this.observacionAnalistaMapper = observacionAnalistaMapper;
    }

    @PostMapping("/informes")
    public ResponseEntity<InformeBuroDTO> crearInforme(@RequestBody InformeBuroDTO dto) {
        InformeBuro informe = informeBuroMapper.toModel(dto);
        InformeBuro creado = componenteAnalisisService.crearInformeBuro(informe);
        return ResponseEntity.ok(informeBuroMapper.toDTO(creado));
    }

    @PostMapping("/evaluaciones")
    public ResponseEntity<EvaluacionCrediticiaDTO> crearEvaluacion(@RequestBody EvaluacionCrediticiaDTO dto) {
        EvaluacionCrediticia evaluacion = evaluacionCrediticiaMapper.toModel(dto);
        EvaluacionCrediticia creada = componenteAnalisisService.crearEvaluacionCrediticia(evaluacion);
        return ResponseEntity.ok(evaluacionCrediticiaMapper.toDTO(creada));
    }

    @PostMapping("/observaciones")
    public ResponseEntity<ObservacionAnalistaDTO> crearObservacion(@RequestBody ObservacionAnalistaDTO dto) {
        ObservacionAnalista observacion = observacionAnalistaMapper.toModel(dto);
        ObservacionAnalista creada = componenteAnalisisService.crearObservacionAnalista(observacion);
        return ResponseEntity.ok(observacionAnalistaMapper.toDTO(creada));
    }

    // Endpoints adicionales para consultar, actualizar, eliminar y validar reglas de negocio de cada componente
} 