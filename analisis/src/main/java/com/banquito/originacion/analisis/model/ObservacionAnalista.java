package com.banquito.originacion.analisis.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "observacion_analista")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ObservacionAnalista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_observacion")
    private Long idObservacion;

    @Column(name = "id_evaluacion", precision = 10, scale = 0, nullable = false)
    private BigDecimal idEvaluacion;

    @Column(name = "id_usuario", precision = 10, scale = 0, nullable = false)
    private BigDecimal idUsuario;

    @Column(name = "razon_intervencion_auto_enum", length = 100, nullable = false)
    private String razonIntervencionAutoEnum;

    @Lob
    @Column(name = "justificacion", columnDefinition = "TEXT")
    private String justificacion;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "version", precision = 9, scale = 0, nullable = false)
    private BigDecimal version;

    public ObservacionAnalista(Long idObservacion) {
        this.idObservacion = idObservacion;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idObservacion == null) ? 0 : idObservacion.hashCode());
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
        ObservacionAnalista other = (ObservacionAnalista) obj;
        if (idObservacion == null) {
            if (other.idObservacion != null)
                return false;
        } else if (!idObservacion.equals(other.idObservacion))
            return false;
        return true;
    }
} 