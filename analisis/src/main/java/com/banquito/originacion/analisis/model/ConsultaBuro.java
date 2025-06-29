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
@Table(name = "consulta_buro")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ConsultaBuro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consulta_buro")
    private Long idConsultaBuro;

    @Column(name = "id_cliente_prospecto", nullable = false)
    private Integer idClienteProspecto;

    @Column(name = "score_obtenido", precision = 4, scale = 0)
    private BigDecimal scoreObtenido;

    @Column(name = "fecha_consulta", nullable = false)
    private LocalDateTime fechaConsulta;

    @Column(name = "detalle_deudas", length = 500)
    private String detalleDeudas;

    @Column(name = "estado_consulta", length = 50, nullable = false)
    private String estadoConsulta;

    @Column(name = "version", precision = 9, scale = 0, nullable = false)
    private BigDecimal version;

    public ConsultaBuro(Long idConsultaBuro) {
        this.idConsultaBuro = idConsultaBuro;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idConsultaBuro == null) ? 0 : idConsultaBuro.hashCode());
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
        ConsultaBuro other = (ConsultaBuro) obj;
        if (idConsultaBuro == null) {
            if (other.idConsultaBuro != null)
                return false;
        } else if (!idConsultaBuro.equals(other.idConsultaBuro))
            return false;
        return true;
    }
} 