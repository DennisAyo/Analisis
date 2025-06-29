# 📋 Reglas de Negocio - Microservicio de Análisis

## 🎯 **Reglas de Transición de Estados**

### **Estados Válidos:**
- `INGRESADA` - Solicitud ingresada al sistema
- `EN_REVISION` - En proceso de revisión inicial
- `DOC_PEND` - Documentos pendientes de presentar
- `EN_ANALISIS` - En análisis crediticio
- `APROBADA` - Solicitud aprobada
- `RECHAZADA` - Solicitud rechazada
- `SUSPENDIDA` - Solicitud suspendida temporalmente
- `CANCELADA` - Solicitud cancelada por el cliente
- `DESEMBOL` - Crédito desembolsado (estado final)

### **Matriz de Transiciones Válidas:**

| Estado Actual | Estados Válidos Siguientes |
|---------------|----------------------------|
| INGRESADA | EN_REVISION, CANCELADA |
| EN_REVISION | DOC_PEND, EN_ANALISIS, RECHAZADA, SUSPENDIDA |
| DOC_PEND | EN_REVISION, EN_ANALISIS, CANCELADA |
| EN_ANALISIS | APROBADA, RECHAZADA, DOC_PEND, SUSPENDIDA |
| APROBADA | DESEMBOL, SUSPENDIDA |
| RECHAZADA | EN_REVISION (solo con nueva información) |
| SUSPENDIDA | EN_REVISION, CANCELADA |
| CANCELADA | *Estado final - sin transiciones* |
| DESEMBOL | *Estado final - sin transiciones* |

## 🚫 **Reglas de Validación Específicas**

### **1. Reglas para RECHAZO:**
- ✅ **Debe existir al menos una observación del analista**
- ✅ **La observación debe contener justificación del rechazo**
- ✅ **La solicitud debe haber sido revisada previamente**

### **2. Reglas para APROBACIÓN:**
- ✅ **La solicitud DEBE haber pasado por estado EN_ANALISIS**
- ✅ **No se puede aprobar directamente sin análisis crediticio**
- ✅ **Debe tener historial completo de revisión**

### **3. Reglas para SUSPENSIÓN:**
- ✅ **Debe existir una observación que justifique la suspensión**
- ✅ **La observación debe contener el motivo específico**
- ✅ **Puede suspenderse desde cualquier estado activo**

### **4. Reglas para ANÁLISIS:**
- ✅ **Una solicitud NO puede reingresar a análisis más de 2 veces**
- ✅ **Previene análisis infinitos**
- ✅ **Optimiza recursos del equipo de análisis**

## ⏰ **Reglas de SLA (Service Level Agreement)**

### **SLA de Análisis Crediticio:**
- 📅 **Tiempo máximo en análisis: 5 días**
- ⚠️ **Se genera alerta automática si se excede**
- 🔄 **Validación automática en cada transición**

## 🔄 **Reglas de Flujo de Proceso**

### **Flujo Estándar Esperado:**
```
INGRESADA → EN_REVISION → EN_ANALISIS → APROBADA → DESEMBOL
```

### **Flujos Alternativos:**
```
INGRESADA → EN_REVISION → DOC_PEND → EN_REVISION → EN_ANALISIS → APROBADA → DESEMBOL

INGRESADA → EN_REVISION → EN_ANALISIS → RECHAZADA

INGRESADA → EN_REVISION → SUSPENDIDA → EN_REVISION → ...

Cualquier estado → CANCELADA (por cliente)
```

## 📊 **Nuevos Endpoints con Reglas de Negocio**

### **Historial Estados (mejorado):**
### **1. POST /v1/historiales-estados/cambiar-estado-validado**
- Aplica todas las validaciones de reglas de negocio
- Valida transiciones permitidas
- Verifica requisitos específicos por estado

### **2. GET /v1/historiales-estados/estados-validos/{idSolicitud}**
- Retorna solo los estados válidos para la transición
- Considera el estado actual de la solicitud
- Incluye información contextual

### **3. GET /v1/historiales-estados/validar-sla/{idSolicitud}**
- Valida cumplimiento de SLA de análisis
- Retorna días transcurridos
- Genera alertas automáticas

### **4. GET /v1/historiales-estados/resumen-solicitud/{idSolicitud}**
- Resumen completo del proceso
- Estadísticas de tiempo y transiciones
- Historial completo de estados

### **5. GET /v1/historiales-estados/estadisticas-por-estado**
- Estadísticas agregadas por estado
- Filtros por usuario y fecha
- Dashboard de gestión

### **Observaciones Analistas (mejorado):**
### **6. GET /v1/observaciones-analistas/estadisticas-por-usuario/{usuario}**
- Estadísticas detalladas por analista
- Análisis de palabras clave en observaciones
- Métricas de productividad

### **7. GET /v1/observaciones-analistas/observaciones-recientes**
- Últimas observaciones del sistema
- Filtro por cantidad (máximo 50)
- Ordenadas por fecha descendente

## 🔒 **Validaciones de Integridad**

### **Reglas de Datos:**
- ✅ **Estado inicial DEBE ser INGRESADA**
- ✅ **No se permiten transiciones inválidas**
- ✅ **Fechas automáticas para trazabilidad**
- ✅ **Versiones automáticas para concurrencia**
- ✅ **Usuario obligatorio en cada transición**

### **Reglas de Observaciones:**
- ✅ **Observaciones obligatorias para RECHAZO y SUSPENSIÓN**
- ✅ **Longitud máxima: 600 caracteres**
- ✅ **Usuario y fecha automáticos**

## 📈 **Beneficios de Implementación**

### **Para el Negocio:**
- 🎯 **Proceso estandarizado y controlado**
- 📊 **Trazabilidad completa de decisiones**
- ⏱️ **Control de SLA y tiempos**
- 🔍 **Auditoría completa del proceso**

### **Para Desarrolladores:**
- 🛡️ **Validaciones automáticas**
- 🚨 **Manejo de errores específicos**
- 📝 **Documentación automática con Swagger**
- 🔧 **APIs RESTful bien estructuradas**

### **Para Analistas:**
- 📋 **Flujo de trabajo claro**
- ⚡ **Transiciones válidas automáticas**
- 📊 **Estadísticas de productividad**
- 🔔 **Alertas de SLA**

## ⚡ **Ejemplos de Uso**

### **Cambiar Estado con Validación:**
```bash
POST /v1/historiales-estados/cambiar-estado-validado
{
  "idSolicitud": 12345,
  "estado": "EN_ANALISIS",
  "usuario": "analista1",
  "motivo": "Iniciando análisis crediticio"
}
```

### **Consultar Estados Válidos:**
```bash
GET /v1/historiales-estados/estados-validos/12345

Response:
{
  "estadoActual": "EN_REVISION",
  "estadosValidos": ["DOC_PEND", "EN_ANALISIS", "RECHAZADA", "SUSPENDIDA"],
  "fechaUltimaTransicion": "2024-01-15T10:30:00"
}
```

### **Validar SLA:**
```bash
GET /v1/historiales-estados/validar-sla/12345

Response:
{
  "cumpleSLA": false,
  "reglaViolada": "SLA_ANALYSIS_EXCEEDED",
  "detalle": "La solicitud lleva 7 días en análisis. SLA máximo: 5 días"
}
```

### **Estadísticas de Usuario:**
```bash
GET /v1/observaciones-analistas/estadisticas-por-usuario/analista1

Response:
{
  "usuario": "analista1",
  "totalObservaciones": 45,
  "observacionesPorMes": {"2024-1": 15, "2024-2": 30},
  "palabrasComunes": {"documentos": 12, "ingresos": 8, "verificar": 7}
}
```

## 🔄 **Extensibilidad**

Las reglas de negocio están diseñadas para ser:
- **Configurables** - Fácil modificación de SLA y validaciones
- **Extensibles** - Nuevas reglas pueden agregarse sin afectar existentes
- **Testeable** - Cada regla es verificable independientemente
- **Auditable** - Todas las validaciones quedan registradas

---
*Documentación generada para el Microservicio de Análisis v1.0.0* 