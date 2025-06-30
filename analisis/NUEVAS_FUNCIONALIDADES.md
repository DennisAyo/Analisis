# Nuevas Funcionalidades - Microservicio de Análisis

## Resumen de Implementación

Se han implementado las siguientes entidades según el modelo de datos actualizado:

1. **ConsultaBuro**: Gestión de consultas de buró crediticio
2. **EvaluacionCrediticia**: Evaluaciones crediticias con scoring automático
3. **ObservacionAnalista**: Observaciones y comentarios de analistas
4. **InformeBuro**: Informes detallados de buró con análisis financiero

## Nuevos Endpoints

### 1. Consultas de Buró (`/v1/consultas-buro`)

#### Crear nueva consulta de buró
```http
POST /v1/consultas-buro
Content-Type: application/json

{
  "idClienteProspecto": 12345,
  "scoreObtenido": 750,
  "fechaConsulta": "2024-01-15T10:30:00",
  "detalleDeudas": "Tarjeta de crédito: $5000, Préstamo personal: $15000",
  "estadoConsulta": "EXITOSA",
  "version": 1
}
```

#### Obtener consultas por cliente
```http
GET /v1/consultas-buro/cliente/12345
```

#### Obtener mejor score de un cliente
```http
GET /v1/consultas-buro/cliente/12345/score-mejor
```

#### Actualizar estado de consulta
```http
PATCH /v1/consultas-buro/1/estado?estado=PROCESADA
```

### 2. Evaluaciones Crediticias (`/v1/evaluaciones-crediticias`)

#### Crear evaluación automática
```http
POST /v1/evaluaciones-crediticias/automatica?idSolicitud=12345&idInformeBuro=10
```

#### Crear evaluación manual
```http
POST /v1/evaluaciones-crediticias
Content-Type: application/json

{
  "idSolicitud": 12345,
  "idInformeBuro": 10,
  "scoreInterno": 680,
  "categoriaRiesgo": "MEDIO",
  "esAutomatico": false,
  "fechaEvaluacion": "2024-01-15T14:30:00",
  "version": 1
}
```

#### Obtener evaluaciones por informe de buró
```http
GET /v1/evaluaciones-crediticias/informe-buro/1
```

#### Obtener evaluación por solicitud
```http
GET /v1/evaluaciones-crediticias/solicitud/12345
```

#### Actualizar categoría de riesgo
```http
PATCH /v1/evaluaciones-crediticias/1/categoria-riesgo?categoria=ALTO
```

### 3. Observaciones de Analistas (`/v1/observaciones-analistas`)

#### Crear nueva observación
```http
POST /v1/observaciones-analistas
Content-Type: application/json

{
  "idSolicitud": 12345,
  "usuario": "analista.juan",
  "razonIntervencion": "Cliente presenta ingresos inconsistentes con declaración inicial",
  "observacion": "Se requiere verificación adicional de ingresos mediante estados de cuenta",
  "fechaHora": "2024-01-15T16:30:00",
  "version": 1
}
```

#### Obtener observaciones por solicitud
```http
GET /v1/observaciones-analistas/solicitud/12345
```

#### Obtener observaciones por usuario
```http
GET /v1/observaciones-analistas/usuario/analista.juan
```

#### Obtener observaciones recientes
```http
GET /v1/observaciones-analistas/recientes?dias=7
```

### 4. Informes de Buró (`/v1/informes-buro`)

#### Crear nuevo informe detallado
```http
POST /v1/informes-buro
Content-Type: application/json

{
  "idConsultaBuro": 1,
  "score": 720,
  "numeroDeudaImpagas": 2,
  "montoTotalAdeudado": 25000.50,
  "capacidadPagoReportada": 8000.00,
  "jsonRespuestaCompleta": "{\"detalle_creditos\":[{\"entidad\":\"Banco XYZ\",\"saldo\":15000}]}",
  "version": 1
}
```

#### Obtener informe por consulta de buró
```http
GET /v1/informes-buro/consulta-buro/1
```

#### Buscar informes por rango de score
```http
GET /v1/informes-buro/score/rango?scoreMin=650&scoreMax=750
```

#### Buscar informes por score mínimo
```http
GET /v1/informes-buro/score/minimo?scoreMin=700
```

#### Buscar por número de deudas impagas
```http
GET /v1/informes-buro/deudas/2
```

#### Calcular ratio deuda/capacidad de pago
```http
GET /v1/informes-buro/1/ratio-deuda-capacidad
```
**Respuesta:**
```json
{
  "idInforme": 1,
  "ratioDeudaCapacidad": 3.125,
  "montoTotalAdeudado": 25000.50,
  "capacidadPagoReportada": 8000.00,
  "interpretacion": "MALO - Capacidad de pago insuficiente"
}
```

#### Actualizar JSON de respuesta
```http
PATCH /v1/informes-buro/1/json-respuesta
Content-Type: application/json

"{\"actualizacion\":\"datos_adicionales\",\"fecha\":\"2024-01-16\"}"
```

## Flujo de Trabajo Típico

### 1. Consulta de Buró
1. **Crear consulta de buró** para un cliente prospecto
2. **Obtener score** y detalles de deudas del buró
3. **Crear informe detallado** con análisis financiero específico
4. **Actualizar estado** según resultado de procesamiento

### 2. Evaluación Crediticia
1. **Crear evaluación automática** basada en informe de buró
2. **Sistema calcula automáticamente** la categoría de riesgo
3. **Analista puede crear observaciones** para casos especiales
4. **Evaluaciones manuales** para casos que requieren intervención humana

### 3. Análisis de Riesgo
1. **Consultar ratio deuda/capacidad** de pago del informe
2. **Revisar observaciones** de analistas previos
3. **Evaluar categoría de riesgo** calculada automáticamente
4. **Tomar decisiones** basadas en análisis integral

## Categorías de Riesgo

El sistema calcula automáticamente la categoría de riesgo basada en el score:

- **BAJO**: Score >= 750 (Excelente perfil crediticio)
- **MEDIO**: Score >= 650 y < 750 (Buen perfil crediticio)
- **ALTO**: Score >= 550 y < 650 (Perfil crediticio con precauciones)
- **MUY_ALTO**: Score < 550 (Perfil crediticio de alto riesgo)
- **SIN_SCORE**: Cuando no hay score disponible

## Estados de Consulta de Buró

- **PENDIENTE**: Consulta iniciada pero no procesada
- **EXITOSA**: Consulta realizada correctamente
- **FALLIDA**: Error en la consulta al buró
- **PROCESADA**: Consulta procesada y utilizada en evaluación

## Interpretación de Ratio Deuda/Capacidad de Pago

- **EXCELENTE** (≤ 0.3): Capacidad de pago muy buena
- **BUENO** (≤ 0.6): Capacidad de pago adecuada  
- **REGULAR** (≤ 1.0): Capacidad de pago justa
- **MALO** (> 1.0): Capacidad de pago insuficiente
- **CRÍTICO** (999.99): Sin capacidad de pago reportada

## Funcionalidades Específicas por Entidad

### ConsultaBuro
- ✅ **Create, Read, Update parcial** (solo estado)
- ✅ **Búsqueda por cliente, estado, fecha**
- ✅ **Obtener mejor score de cliente**
- ✅ **Verificar consultas exitosas**
- ❌ **No Delete** (preservar historial)

### EvaluacionCrediticia  
- ✅ **Create (manual y automática), Read, Update parcial** (solo categoría)
- ✅ **Cálculo automático de categoría de riesgo**
- ✅ **Búsqueda por solicitud, categoría, tipo, informe de buró**
- ✅ **Evaluaciones basadas en informe de buró**
- ❌ **No Delete** (preservar historial)

### ObservacionAnalista
- ✅ **Create, Read, Update, Delete**
- ✅ **Búsqueda por solicitud, usuario, fecha**
- ✅ **Filtros por período de tiempo**
- ✅ **Estadísticas por analista**

### InformeBuro
- ✅ **Create, Read, Update parcial** (solo JSON de respuesta)
- ✅ **Análisis financiero avanzado**
- ✅ **Cálculo de ratios financieros**
- ✅ **Búsqueda por score, deudas, capacidad de pago**
- ✅ **Interpretación automática de ratios**
- ❌ **No Delete** (preservar información crediticia)

## Consideraciones Técnicas

- **Mappers manuales estáticos**: Eliminado MapStruct, implementación manual para mayor control
- **BigDecimal**: Para todos los valores monetarios y scores
- **Jakarta Validation**: Validación exhaustiva de datos de entrada
- **Swagger/OpenAPI**: Documentación completa de APIs
- **JPA sin @Query**: Solo métodos nativos de repositorio
- **Excepciones personalizadas**: RuntimeException para manejo de errores
- **Versionado de APIs**: Todos los endpoints bajo `/v1/`
- **PATCH**: Para actualizaciones parciales específicas

## APIs sin GetAll

Siguiendo las buenas prácticas, **no se implementaron métodos GetAll** para evitar problemas de rendimiento. En su lugar se proporcionan:

- **Búsquedas específicas** por criterios relevantes
- **Filtros** por cliente, fecha, estado, etc.
- **Paginación implícita** en las consultas de repositorio
- **Endpoints especializados** para casos de uso específicos

## ⚠️ RELACIONES CORREGIDAS

**Última actualización**: Se corrigieron las relaciones según el diagrama de base de datos:

- ✅ **EvaluacionCrediticia** ahora tiene relación con **InformeBuro** (id_informe_buro)
- ✅ **InformeBuro** mantiene relación con **ConsultaBuro** (id_consulta_buro) 
- ✅ Flujo corregido: `ConsultaBuro` → `InformeBuro` → `EvaluacionCrediticia`
- ✅ Todos los DTOs, mappers, servicios y controladores actualizados
- ✅ Compilación exitosa verificada 