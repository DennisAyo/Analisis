package com.banquito.originacion.analisis.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "historial_estados")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class HistorialEstado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial")
    private Long idHistorial;

    @Column(name = "id_solicitud", nullable = false)
    private Integer idSolicitud;

    @Column(name = "estado", length = 12, nullable = false)
    private String estado;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "usuario", length = 50, nullable = false)
    private String usuario;

    @Column(name = "motivo", length = 120)
    private String motivo;

    @Column(name = "version", precision = 9, scale = 0, nullable = false)
    private BigDecimal version;

    public HistorialEstado(Long idHistorial) {
        this.idHistorial = idHistorial;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idHistorial == null) ? 0 : idHistorial.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HistorialEstado other = (HistorialEstado) obj;
        if (idHistorial == null) {
            if (other.idHistorial != null)
                return false;
        } else if (!idHistorial.equals(other.idHistorial))
            return false;
        return true;
    }
} 