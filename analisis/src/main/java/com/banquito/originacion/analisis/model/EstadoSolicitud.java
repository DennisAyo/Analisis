package com.banquito.originacion.analisis.model;

public enum EstadoSolicitud {
    INGRESADA("INGRESADA", "Solicitud ingresada al sistema"),
    EN_REVISION("EN_REVISION", "En proceso de revisión"),
    DOCUMENTOS_PENDIENTES("DOC_PEND", "Documentos pendientes"),
    EN_ANALISIS("EN_ANALISIS", "En análisis crediticio"),
    APROBADA("APROBADA", "Solicitud aprobada"),
    RECHAZADA("RECHAZADA", "Solicitud rechazada"),
    SUSPENDIDA("SUSPENDIDA", "Solicitud suspendida"),
    CANCELADA("CANCELADA", "Solicitud cancelada por el cliente"),
    DESEMBOLSADA("DESEMBOL", "Crédito desembolsado");

    private final String codigo;
    private final String descripcion;

    EstadoSolicitud(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static EstadoSolicitud fromCodigo(String codigo) {
        for (EstadoSolicitud estado : EstadoSolicitud.values()) {
            if (estado.getCodigo().equals(codigo)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Estado no válido: " + codigo);
    }
} 