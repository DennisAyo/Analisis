package com.banquito.originacion.analisis.model;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "consultas_buro")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ConsultaBuro {

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

    @Column(name = "version", precision = 9, scale = 0, nullable = false)
    private BigDecimal version;

    public ConsultaBuro(Long idConsulta) {
        this.idConsulta = idConsulta;
    }

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
    
} 