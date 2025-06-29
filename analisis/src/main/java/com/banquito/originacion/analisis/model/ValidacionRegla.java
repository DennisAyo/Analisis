package com.banquito.originacion.analisis.model;

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
@Table(name = "validacion_regla")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ValidacionRegla {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_validacion_regla")
    private Long idValidacionRegla;

    @ManyToOne
    @JoinColumn(name = "id_evaluaciones_crediticias", nullable = false)
    private EvaluacionCrediticia evaluacionCrediticia;

    @ManyToOne
    @JoinColumn(name = "id_regla", nullable = false)
    private ReglaEvaluacionCrediticia regla;

    @Column(name = "resultado", nullable = false)
    private Boolean resultado;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    public ValidacionRegla(Long idValidacionRegla) {
        this.idValidacionRegla = idValidacionRegla;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idValidacionRegla == null) ? 0 : idValidacionRegla.hashCode());
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
        ValidacionRegla other = (ValidacionRegla) obj;
        if (idValidacionRegla == null) {
            if (other.idValidacionRegla != null)
                return false;
        } else if (!idValidacionRegla.equals(other.idValidacionRegla))
            return false;
        return true;
    }
} 