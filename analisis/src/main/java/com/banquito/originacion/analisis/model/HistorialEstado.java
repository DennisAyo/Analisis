package com.banquito.originacion.analisis.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.persistence.Embeddable;

@Entity
@Table(name = "historial_estado")
public class HistorialEstado implements Serializable {

    @EmbeddedId
    private HistorialEstadoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_consulta", referencedColumnName = "id_consulta", insertable = false, updatable = false)
    private ConsultaBuro consultaBuro;

    @Column(name = "estado_anterior", length = 50, nullable = false)
    private String estadoAnterior;

    @Column(name = "estado_nuevo", length = 50, nullable = false)
    private String estadoNuevo;

    @Column(name = "fecha_cambio", nullable = false)
    private LocalDateTime fechaCambio;

    @Column(name = "usuario", length = 100, nullable = false)
    private String usuario;

    @Version
    @Column(name = "version")
    private Long version;

    public HistorialEstado() {}

    public HistorialEstado(HistorialEstadoId id) {
        this.id = id;
    }

    public HistorialEstadoId getId() { return id; }
    public void setId(HistorialEstadoId id) { this.id = id; }
    public ConsultaBuro getConsultaBuro() { return consultaBuro; }
    public void setConsultaBuro(ConsultaBuro consultaBuro) { this.consultaBuro = consultaBuro; }
    public String getEstadoAnterior() { return estadoAnterior; }
    public void setEstadoAnterior(String estadoAnterior) { this.estadoAnterior = estadoAnterior; }
    public String getEstadoNuevo() { return estadoNuevo; }
    public void setEstadoNuevo(String estadoNuevo) { this.estadoNuevo = estadoNuevo; }
    public LocalDateTime getFechaCambio() { return fechaCambio; }
    public void setFechaCambio(LocalDateTime fechaCambio) { this.fechaCambio = fechaCambio; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        HistorialEstado other = (HistorialEstado) obj;
        return id != null && id.equals(other.id);
    }

    @Override
    public String toString() {
        return "HistorialEstado{" +
                "id=" + id +
                ", estadoAnterior='" + estadoAnterior + '\'' +
                ", estadoNuevo='" + estadoNuevo + '\'' +
                ", fechaCambio=" + fechaCambio +
                ", usuario='" + usuario + '\'' +
                ", version=" + version +
                '}';
    }

    @Embeddable
    public static class HistorialEstadoId implements Serializable {
        @Column(name = "id_historial")
        private Long idHistorial;

        @Column(name = "id_consulta")
        private Long idConsulta;

        public HistorialEstadoId() {}
        public HistorialEstadoId(Long idHistorial, Long idConsulta) {
            this.idHistorial = idHistorial;
            this.idConsulta = idConsulta;
        }
        public Long getIdHistorial() { return idHistorial; }
        public void setIdHistorial(Long idHistorial) { this.idHistorial = idHistorial; }
        public Long getIdConsulta() { return idConsulta; }
        public void setIdConsulta(Long idConsulta) { this.idConsulta = idConsulta; }
        @Override
        public int hashCode() {
            int result = 1;
            result = 31 * result + (idHistorial != null ? idHistorial.hashCode() : 0);
            result = 31 * result + (idConsulta != null ? idConsulta.hashCode() : 0);
            return result;
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            HistorialEstadoId other = (HistorialEstadoId) obj;
            return (idHistorial != null && idHistorial.equals(other.idHistorial)) &&
                   (idConsulta != null && idConsulta.equals(other.idConsulta));
        }
        @Override
        public String toString() {
            return "HistorialEstadoId{" +
                    "idHistorial=" + idHistorial +
                    ", idConsulta=" + idConsulta +
                    '}';
        }
    }
} 