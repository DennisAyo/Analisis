package com.banquito.originacion.analisis.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "observacion_analista")
public class ObservacionAnalista implements Serializable {

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

    @Version
    @Column(name = "version")
    private Long version;

    public ObservacionAnalista() {}

    public ObservacionAnalista(Long idObservacion) {
        this.idObservacion = idObservacion;
    }

    public Long getIdObservacion() { return idObservacion; }
    public void setIdObservacion(Long idObservacion) { this.idObservacion = idObservacion; }
    public BigDecimal getIdEvaluacion() { return idEvaluacion; }
    public void setIdEvaluacion(BigDecimal idEvaluacion) { this.idEvaluacion = idEvaluacion; }
    public BigDecimal getIdUsuario() { return idUsuario; }
    public void setIdUsuario(BigDecimal idUsuario) { this.idUsuario = idUsuario; }
    public String getRazonIntervencionAutoEnum() { return razonIntervencionAutoEnum; }
    public void setRazonIntervencionAutoEnum(String razonIntervencionAutoEnum) { this.razonIntervencionAutoEnum = razonIntervencionAutoEnum; }
    public String getJustificacion() { return justificacion; }
    public void setJustificacion(String justificacion) { this.justificacion = justificacion; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

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

    @Override
    public String toString() {
        return "ObservacionAnalista{" +
                "idObservacion=" + idObservacion +
                ", idEvaluacion=" + idEvaluacion +
                ", idUsuario=" + idUsuario +
                ", razonIntervencionAutoEnum='" + razonIntervencionAutoEnum + '\'' +
                ", justificacion='" + justificacion + '\'' +
                ", fechaHora=" + fechaHora +
                ", version=" + version +
                '}';
    }
} 