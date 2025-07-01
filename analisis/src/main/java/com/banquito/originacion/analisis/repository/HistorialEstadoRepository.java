package com.banquito.originacion.analisis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.banquito.originacion.analisis.model.HistorialEstado;
import com.banquito.originacion.analisis.model.HistorialEstado.HistorialEstadoId;

public interface HistorialEstadoRepository extends JpaRepository<HistorialEstado, HistorialEstadoId> {

} 