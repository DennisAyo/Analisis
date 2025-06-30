package com.banquito.originacion.analisis.model;

import java.math.BigDecimal;

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
@Table(name = "informes_buro")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class InformeBuro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_informe_buro")
    private Long idInformeBuro;

    @ManyToOne
    @JoinColumn(name = "id_consulta_buro", nullable = false)
    private ConsultaBuro consultaBuro;

    @Column(name = "score", precision = 4, scale = 0)
    private BigDecimal score;

    @Column(name = "numero_deudas_impagas")
    private Integer numeroDeudaImpagas;

    @Column(name = "monto_total_adeudado", precision = 12, scale = 2)
    private BigDecimal montoTotalAdeudado;

    @Column(name = "capacidad_pago_reportada", precision = 10, scale = 2)
    private BigDecimal capacidadPagoReportada;

    @Column(name = "json_respuesta_completa", columnDefinition = "jsonb")
    private String jsonRespuestaCompleta;

    @Column(name = "version", precision = 9, scale = 0, nullable = false)
    private BigDecimal version;

    public InformeBuro(Long idInformeBuro) {
        this.idInformeBuro = idInformeBuro;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idInformeBuro == null) ? 0 : idInformeBuro.hashCode());
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
        InformeBuro other = (InformeBuro) obj;
        if (idInformeBuro == null) {
            if (other.idInformeBuro != null)
                return false;
        } else if (!idInformeBuro.equals(other.idInformeBuro))
            return false;
        return true;
    }
} 