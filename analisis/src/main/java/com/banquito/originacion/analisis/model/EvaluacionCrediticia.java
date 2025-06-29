package com.banquito.originacion.analisis.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "evaluaciones_crediticias")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class EvaluacionCrediticia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evaluaciones_crediticias")
    private Long idEvaluacionesCrediticias;

    @Column(name = "id_solicitud", nullable = false)
    private Integer idSolicitud;

    @ManyToOne
    @JoinColumn(name = "id_consulta_buro", nullable = false)
    private ConsultaBuro consultaBuro;

    @Column(name = "score_interno", precision = 4, scale = 0)
    private BigDecimal scoreInterno;

    @Column(name = "categoria_riesgo", length = 50)
    private String categoriaRiesgo;

    @Column(name = "es_automatico", nullable = false)
    private Boolean esAutomatico;

    @Column(name = "fecha_evaluacion", nullable = false)
    private LocalDateTime fechaEvaluacion;

    @Column(name = "version", precision = 9, scale = 0, nullable = false)
    private BigDecimal version;

    public EvaluacionCrediticia(Long idEvaluacionesCrediticias) {
        this.idEvaluacionesCrediticias = idEvaluacionesCrediticias;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idEvaluacionesCrediticias == null) ? 0 : idEvaluacionesCrediticias.hashCode());
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
        if (idEvaluacionesCrediticias == null) {
            if (other.idEvaluacionesCrediticias != null)
                return false;
        } else if (!idEvaluacionesCrediticias.equals(other.idEvaluacionesCrediticias))
            return false;
        return true;
    }
} 