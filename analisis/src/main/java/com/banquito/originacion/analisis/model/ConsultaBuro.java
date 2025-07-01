package com.banquito.originacion.analisis.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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
@Table(name = "consultas_buro")
public class ConsultaBuro implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consulta")
    private Long idConsulta;

    @Column(name = "id_solicitud", nullable = false)
    private Integer idSolicitud;

    @Column(name = "fecha_consulta", nullable = false)
    private LocalDateTime fechaConsulta;

    @Column(name = "estado_consulta", length = 50, nullable = false)
    private String estadoConsulta;

    @Column(name = "score_externo", precision = 4, scale = 0)
    private BigDecimal scoreExterno;

    @Column(name = "cuentas_activas", precision = 3, scale = 0)
    private BigDecimal cuentasActivas;

    @Column(name = "cuentas_morosas", precision = 3, scale = 0)
    private BigDecimal cuentasMorosas;

    @Column(name = "monto_moroso_total", precision = 12, scale = 2)
    private BigDecimal montoMorosoTotal;

    @Column(name = "dias_mora_promedio", precision = 5, scale = 2)
    private BigDecimal diasMoraPromedio;

    @Column(name = "fecha_primera_mora")
    private LocalDate fechaPrimeraMora;

    @Lob
    @Column(name = "datos_buro_encriptado")
    private byte[] datosBuroEncriptado;

    @Version
    @Column(name = "version")
    private Long version;

    public ConsultaBuro() {}

    public ConsultaBuro(Long idConsulta) {
        this.idConsulta = idConsulta;
    }

    public Long getIdConsulta() { return idConsulta; }
    public void setIdConsulta(Long idConsulta) { this.idConsulta = idConsulta; }
    public Integer getIdSolicitud() { return idSolicitud; }
    public void setIdSolicitud(Integer idSolicitud) { this.idSolicitud = idSolicitud; }
    public LocalDateTime getFechaConsulta() { return fechaConsulta; }
    public void setFechaConsulta(LocalDateTime fechaConsulta) { this.fechaConsulta = fechaConsulta; }
    public String getEstadoConsulta() { return estadoConsulta; }
    public void setEstadoConsulta(String estadoConsulta) { this.estadoConsulta = estadoConsulta; }
    public BigDecimal getScoreExterno() { return scoreExterno; }
    public void setScoreExterno(BigDecimal scoreExterno) { this.scoreExterno = scoreExterno; }
    public BigDecimal getCuentasActivas() { return cuentasActivas; }
    public void setCuentasActivas(BigDecimal cuentasActivas) { this.cuentasActivas = cuentasActivas; }
    public BigDecimal getCuentasMorosas() { return cuentasMorosas; }
    public void setCuentasMorosas(BigDecimal cuentasMorosas) { this.cuentasMorosas = cuentasMorosas; }
    public BigDecimal getMontoMorosoTotal() { return montoMorosoTotal; }
    public void setMontoMorosoTotal(BigDecimal montoMorosoTotal) { this.montoMorosoTotal = montoMorosoTotal; }
    public BigDecimal getDiasMoraPromedio() { return diasMoraPromedio; }
    public void setDiasMoraPromedio(BigDecimal diasMoraPromedio) { this.diasMoraPromedio = diasMoraPromedio; }
    public LocalDate getFechaPrimeraMora() { return fechaPrimeraMora; }
    public void setFechaPrimeraMora(LocalDate fechaPrimeraMora) { this.fechaPrimeraMora = fechaPrimeraMora; }
    public byte[] getDatosBuroEncriptado() { return datosBuroEncriptado; }
    public void setDatosBuroEncriptado(byte[] datosBuroEncriptado) { this.datosBuroEncriptado = datosBuroEncriptado; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idConsulta == null) ? 0 : idConsulta.hashCode());
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
        if (idConsulta == null) {
            if (other.idConsulta != null)
                return false;
        } else if (!idConsulta.equals(other.idConsulta))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ConsultaBuro{" +
                "idConsulta=" + idConsulta +
                ", idSolicitud=" + idSolicitud +
                ", fechaConsulta=" + fechaConsulta +
                ", estadoConsulta='" + estadoConsulta + '\'' +
                ", scoreExterno=" + scoreExterno +
                ", cuentasActivas=" + cuentasActivas +
                ", cuentasMorosas=" + cuentasMorosas +
                ", montoMorosoTotal=" + montoMorosoTotal +
                ", diasMoraPromedio=" + diasMoraPromedio +
                ", fechaPrimeraMora=" + fechaPrimeraMora +
                ", version=" + version +
                '}';
    }
} 