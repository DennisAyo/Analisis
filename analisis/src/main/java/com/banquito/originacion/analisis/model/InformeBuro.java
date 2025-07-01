package com.banquito.originacion.analisis.model;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "informes_buro")
public class InformeBuro implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_informe_buro")
    private Long idInformeBuro;

    @ManyToOne
    @JoinColumn(name = "id_consulta_buro", referencedColumnName = "id_consulta", insertable = false, updatable = false)
    private ConsultaBuro consultaBuro;

    @Column(name = "id_consulta_buro", nullable = false)
    private Long idConsultaBuro;

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

    @Version
    @Column(name = "version")
    private Long version;

    public InformeBuro() {}

    public InformeBuro(Long idInformeBuro) {
        this.idInformeBuro = idInformeBuro;
    }

    public Long getIdInformeBuro() { return idInformeBuro; }
    public void setIdInformeBuro(Long idInformeBuro) { this.idInformeBuro = idInformeBuro; }
    public ConsultaBuro getConsultaBuro() { return consultaBuro; }
    public void setConsultaBuro(ConsultaBuro consultaBuro) { this.consultaBuro = consultaBuro; }
    public Long getIdConsultaBuro() { return idConsultaBuro; }
    public void setIdConsultaBuro(Long idConsultaBuro) { this.idConsultaBuro = idConsultaBuro; }
    public BigDecimal getScore() { return score; }
    public void setScore(BigDecimal score) { this.score = score; }
    public Integer getNumeroDeudaImpagas() { return numeroDeudaImpagas; }
    public void setNumeroDeudaImpagas(Integer numeroDeudaImpagas) { this.numeroDeudaImpagas = numeroDeudaImpagas; }
    public BigDecimal getMontoTotalAdeudado() { return montoTotalAdeudado; }
    public void setMontoTotalAdeudado(BigDecimal montoTotalAdeudado) { this.montoTotalAdeudado = montoTotalAdeudado; }
    public BigDecimal getCapacidadPagoReportada() { return capacidadPagoReportada; }
    public void setCapacidadPagoReportada(BigDecimal capacidadPagoReportada) { this.capacidadPagoReportada = capacidadPagoReportada; }
    public String getJsonRespuestaCompleta() { return jsonRespuestaCompleta; }
    public void setJsonRespuestaCompleta(String jsonRespuestaCompleta) { this.jsonRespuestaCompleta = jsonRespuestaCompleta; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

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

    @Override
    public String toString() {
        return "InformeBuro{" +
                "idInformeBuro=" + idInformeBuro +
                ", idConsultaBuro=" + idConsultaBuro +
                ", score=" + score +
                ", numeroDeudaImpagas=" + numeroDeudaImpagas +
                ", montoTotalAdeudado=" + montoTotalAdeudado +
                ", capacidadPagoReportada=" + capacidadPagoReportada +
                ", jsonRespuestaCompleta='" + jsonRespuestaCompleta + '\'' +
                ", version=" + version +
                '}';
    }
} 