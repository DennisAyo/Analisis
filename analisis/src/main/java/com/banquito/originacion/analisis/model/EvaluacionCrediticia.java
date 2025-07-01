package com.banquito.originacion.analisis.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "evaluacion_crediticia")
public class EvaluacionCrediticia implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evaluacion")
    private Long idEvaluacion;

    @Column(name = "id_solicitud", nullable = false)
    private Integer idSolicitud;

    @ManyToOne
    @JoinColumn(name = "id_informe_buro", referencedColumnName = "id_informe_buro", insertable = false, updatable = false)
    private InformeBuro informeBuro;

    @Column(name = "id_informe_buro", nullable = false)
    private Long idInformeBuro;

    @Column(name = "fecha_evaluacion", nullable = false)
    private LocalDateTime fechaEvaluacion;

    @Column(name = "score_interno_calculado", precision = 4, scale = 0)
    private BigDecimal scoreInternoCalculado;

    @Column(name = "resultado_automatico", length = 50)
    private String resultadoAutomatico;

    @Lob
    @Column(name = "observaciones_motor_reglas", columnDefinition = "TEXT")
    private String observacionesMotorReglas;

    @Column(name = "decision_final_analista", length = 50)
    private String decisionFinalAnalista;

    @Lob
    @Column(name = "justificacion_analista", columnDefinition = "TEXT")
    private String justificacionAnalista;

    @Version
    @Column(name = "version")
    private Long version;

    public EvaluacionCrediticia() {}

    public EvaluacionCrediticia(Long idEvaluacion) {
        this.idEvaluacion = idEvaluacion;
    }

    public Long getIdEvaluacion() { return idEvaluacion; }
    public void setIdEvaluacion(Long idEvaluacion) { this.idEvaluacion = idEvaluacion; }
    public Integer getIdSolicitud() { return idSolicitud; }
    public void setIdSolicitud(Integer idSolicitud) { this.idSolicitud = idSolicitud; }
    public InformeBuro getInformeBuro() { return informeBuro; }
    public void setInformeBuro(InformeBuro informeBuro) { this.informeBuro = informeBuro; }
    public Long getIdInformeBuro() { return idInformeBuro; }
    public void setIdInformeBuro(Long idInformeBuro) { this.idInformeBuro = idInformeBuro; }
    public LocalDateTime getFechaEvaluacion() { return fechaEvaluacion; }
    public void setFechaEvaluacion(LocalDateTime fechaEvaluacion) { this.fechaEvaluacion = fechaEvaluacion; }
    public BigDecimal getScoreInternoCalculado() { return scoreInternoCalculado; }
    public void setScoreInternoCalculado(BigDecimal scoreInternoCalculado) { this.scoreInternoCalculado = scoreInternoCalculado; }
    public String getResultadoAutomatico() { return resultadoAutomatico; }
    public void setResultadoAutomatico(String resultadoAutomatico) { this.resultadoAutomatico = resultadoAutomatico; }
    public String getObservacionesMotorReglas() { return observacionesMotorReglas; }
    public void setObservacionesMotorReglas(String observacionesMotorReglas) { this.observacionesMotorReglas = observacionesMotorReglas; }
    public String getDecisionFinalAnalista() { return decisionFinalAnalista; }
    public void setDecisionFinalAnalista(String decisionFinalAnalista) { this.decisionFinalAnalista = decisionFinalAnalista; }
    public String getJustificacionAnalista() { return justificacionAnalista; }
    public void setJustificacionAnalista(String justificacionAnalista) { this.justificacionAnalista = justificacionAnalista; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idEvaluacion == null) ? 0 : idEvaluacion.hashCode());
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
        EvaluacionCrediticia other = (EvaluacionCrediticia) obj;
        if (idEvaluacion == null) {
            if (other.idEvaluacion != null)
                return false;
        } else if (!idEvaluacion.equals(other.idEvaluacion))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "EvaluacionCrediticia{" +
                "idEvaluacion=" + idEvaluacion +
                ", idSolicitud=" + idSolicitud +
                ", idInformeBuro=" + idInformeBuro +
                ", fechaEvaluacion=" + fechaEvaluacion +
                ", scoreInternoCalculado=" + scoreInternoCalculado +
                ", resultadoAutomatico='" + resultadoAutomatico + '\'' +
                ", observacionesMotorReglas='" + observacionesMotorReglas + '\'' +
                ", decisionFinalAnalista='" + decisionFinalAnalista + '\'' +
                ", justificacionAnalista='" + justificacionAnalista + '\'' +
                ", version=" + version +
                '}';
    }
} 