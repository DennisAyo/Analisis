package com.banquito.originacion.analisis.model;

import java.math.BigDecimal;

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
@Table(name = "reglas_evaluacion_crediticia")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ReglaEvaluacionCrediticia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_regla")
    private Long idRegla;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Column(name = "condicion", length = 100, nullable = false)
    private String condicion;

    @Column(name = "prioridad", nullable = false)
    private Integer prioridad;

    @Column(name = "activa", nullable = false)
    private Boolean activa;

    @Column(name = "version", precision = 9, scale = 0, nullable = false)
    private BigDecimal version;

    public ReglaEvaluacionCrediticia(Long idRegla) {
        this.idRegla = idRegla;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idRegla == null) ? 0 : idRegla.hashCode());
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
        ReglaEvaluacionCrediticia other = (ReglaEvaluacionCrediticia) obj;
        if (idRegla == null) {
            if (other.idRegla != null)
                return false;
        } else if (!idRegla.equals(other.idRegla))
            return false;
        return true;
    }
} 