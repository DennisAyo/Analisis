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
@Table(name = "observacion_analistas")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ObservacionAnalista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_observacion_analista")
    private Long idObservacionAnalista;

    @Column(name = "id_solicitud", nullable = false)
    private Integer idSolicitud;

    @Column(name = "usuario", length = 50, nullable = false)
    private String usuario;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "razon_intervencion", length = 600, nullable = false)
    private String razonIntervencion;

    @Column(name = "version", precision = 9, scale = 0, nullable = false)
    private BigDecimal version;

    public ObservacionAnalista(Long idObservacionAnalista) {
        this.idObservacionAnalista = idObservacionAnalista;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idObservacionAnalista == null) ? 0 : idObservacionAnalista.hashCode());
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
        ObservacionAnalista other = (ObservacionAnalista) obj;
        if (idObservacionAnalista == null) {
            if (other.idObservacionAnalista != null)
                return false;
        } else if (!idObservacionAnalista.equals(other.idObservacionAnalista))
            return false;
        return true;
    }
} 