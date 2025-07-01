package com.banquito.originacion.analisis.controller;

import com.banquito.originacion.analisis.service.ConsultaService;
import com.banquito.originacion.analisis.controller.dto.ConsultaBuroDTO;
import com.banquito.originacion.analisis.controller.dto.HistorialEstadoDTO;
import com.banquito.originacion.analisis.controller.mapper.ConsultaBuroMapper;
import com.banquito.originacion.analisis.controller.mapper.HistorialEstadoMapper;
import com.banquito.originacion.analisis.model.ConsultaBuro;
import com.banquito.originacion.analisis.model.HistorialEstado;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {
    private final ConsultaService consultaService;
    private final ConsultaBuroMapper consultaBuroMapper;
    private final HistorialEstadoMapper historialEstadoMapper;

    public ConsultaController(ConsultaService consultaService,
                             ConsultaBuroMapper consultaBuroMapper,
                             HistorialEstadoMapper historialEstadoMapper) {
        this.consultaService = consultaService;
        this.consultaBuroMapper = consultaBuroMapper;
        this.historialEstadoMapper = historialEstadoMapper;
    }

    @PostMapping
    public ResponseEntity<ConsultaBuroDTO> crearConsulta(@RequestBody ConsultaBuroDTO dto) {
        ConsultaBuro consulta = consultaBuroMapper.toModel(dto);
        ConsultaBuro creada = consultaService.crearConsulta(consulta);
        return ResponseEntity.ok(consultaBuroMapper.toDTO(creada));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultaBuroDTO> obtenerConsulta(@PathVariable Long id) {
        Optional<ConsultaBuro> consulta = consultaService.obtenerConsultaPorId(id);
        return consulta.map(value -> ResponseEntity.ok(consultaBuroMapper.toDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/historial")
    public ResponseEntity<HistorialEstadoDTO> crearHistorial(@PathVariable Long id, @RequestBody HistorialEstadoDTO dto) {
        dto.setIdConsulta(id);
        HistorialEstado historial = historialEstadoMapper.toModel(dto);
        HistorialEstado creado = consultaService.crearHistorialEstado(historial);
        return ResponseEntity.ok(historialEstadoMapper.toDTO(creado));
    }

    // Endpoints adicionales para actualizar estado, consultar historial, validar reglas de transición, SLA, etc.
} 