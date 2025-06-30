package com.banquito.originacion.analisis.model;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "evaluacion_crediticia")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class EvaluacionCrediticia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evaluacion")
    private Long idEvaluacion;

    @Column(name = "id_solicitud", nullable = false)
    private Integer idSolicitud;

    @ManyToOne
    @JoinColumn(name = "id_informe_buro", nullable = false)
    private InformeBuro informeBuro;

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

    @Column(name = "version", precision = 9, scale = 0, nullable = false)
    private BigDecimal version;

    public EvaluacionCrediticia(Long idEvaluacion) {
        this.idEvaluacion = idEvaluacion;
    }

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
} 