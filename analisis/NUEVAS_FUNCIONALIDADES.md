# Nuevas Funcionalidades - Microservicio de Análisis

## Resumen de Implementación

Se han agregado las siguientes nuevas entidades según el modelo de datos actualizado:

1. **ConsultaBuro**: Gestión de consultas de buró crediticio
2. **ReglaEvaluacionCrediticia**: Definición y gestión de reglas para evaluación
3. **EvaluacionCrediticia**: Evaluaciones crediticias con scoring automático
4. **ValidacionRegla**: Resultados de aplicar reglas a evaluaciones

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

### 2. Reglas de Evaluación (`/v1/reglas-evaluacion`)

#### Crear nueva regla
```http
POST /v1/reglas-evaluacion
Content-Type: application/json

{
  "nombre": "Verificación de ingresos mínimos",
  "descripcion": "Verifica que los ingresos sean superiores a 3 veces la cuota",
  "condicion": "ingresos >= (cuota * 3)",
  "prioridad": 1,
  "activa": true,
  "version": 1
}
```

#### Obtener reglas activas
```http
GET /v1/reglas-evaluacion/activas
```

#### Buscar reglas por nombre
```http
GET /v1/reglas-evaluacion?nombre=ingresos
```

#### Activar/Desactivar regla
```http
PATCH /v1/reglas-evaluacion/1/toggle-activa
```

#### Actualizar prioridad
```http
PATCH /v1/reglas-evaluacion/1/prioridad?prioridad=5
```

### 3. Evaluaciones Crediticias (`/v1/evaluaciones-crediticias`)

#### Crear evaluación automática
```http
POST /v1/evaluaciones-crediticias/automatica?idSolicitud=12345&idConsultaBuro=10
```

#### Crear evaluación manual
```http
POST /v1/evaluaciones-crediticias
Content-Type: application/json

{
  "idSolicitud": 12345,
  "idConsultaBuro": 10,
  "scoreInterno": 680,
  "categoriaRiesgo": "MEDIO",
  "esAutomatico": false,
  "fechaEvaluacion": "2024-01-15T14:30:00",
  "version": 1
}
```

#### Obtener evaluaciones por solicitud
```http
GET /v1/evaluaciones-crediticias/solicitud/12345
```

#### Obtener última evaluación de una solicitud
```http
GET /v1/evaluaciones-crediticias/solicitud/12345/ultima
```

#### Calcular categoría de riesgo por score
```http
GET /v1/evaluaciones-crediticias/score/680/categoria
```

#### Actualizar categoría de riesgo
```http
PATCH /v1/evaluaciones-crediticias/1/categoria-riesgo?categoria=ALTO
```

### 4. Validaciones de Reglas (`/v1/validaciones-reglas`)

#### Obtener validaciones por evaluación
```http
GET /v1/validaciones-reglas/evaluacion/5
```

#### Obtener validaciones fallidas por solicitud
```http
GET /v1/validaciones-reglas/solicitud/12345/fallidas
```

#### Obtener resumen de validaciones
```http
GET /v1/validaciones-reglas/evaluacion/5/resumen
```

#### Crear validación manual
```http
POST /v1/validaciones-reglas
Content-Type: application/json

{
  "idEvaluacionesCrediticias": 5,
  "idRegla": 3,
  "resultado": true,
  "fecha": "2024-01-15T15:45:00"
}
```

## Flujo de Trabajo Típico

### 1. Consulta de Buró
1. Crear consulta de buró para un cliente
2. Obtener score y detalles de deudas
3. Actualizar estado según resultado

### 2. Evaluación Crediticia
1. Crear evaluación automática basada en consulta de buró
2. Sistema calcula automáticamente la categoría de riesgo
3. Se pueden crear evaluaciones manuales si es necesario

### 3. Aplicación de Reglas
1. Obtener reglas activas ordenadas por prioridad
2. Aplicar cada regla a la evaluación crediticia
3. Registrar resultados en ValidacionRegla

### 4. Análisis de Resultados
1. Consultar resumen de validaciones
2. Identificar reglas que fallaron
3. Tomar decisiones basadas en categoría de riesgo y validaciones

## Categorías de Riesgo

El sistema calcula automáticamente la categoría de riesgo basada en el score:

- **BAJO**: Score >= 750
- **MEDIO**: Score >= 650 y < 750  
- **ALTO**: Score >= 550 y < 650
- **MUY_ALTO**: Score < 550
- **SIN_SCORE**: Cuando no hay score disponible

## Estados de Consulta de Buró

- **PENDIENTE**: Consulta iniciada pero no procesada
- **EXITOSA**: Consulta realizada correctamente
- **FALLIDA**: Error en la consulta
- **PROCESADA**: Consulta procesada y utilizada en evaluación

## Tecnologías Utilizadas

- **MapStruct**: Para mapeo automático entre entidades y DTOs
- **Jakarta Validation**: Para validación de datos de entrada
- **Swagger/OpenAPI**: Para documentación de APIs
- **JPA/Hibernate**: Para persistencia con PostgreSQL
- **Spring Boot**: Framework principal
- **BigDecimal**: Para precisión en cálculos financieros

## Reglas de Negocio Implementadas

1. **Validación de Transiciones**: Las evaluaciones deben tener consulta de buró válida
2. **Cálculo Automático**: La categoría de riesgo se calcula automáticamente
3. **Versionado**: Todas las entidades manejan versionado para concurrencia
4. **Auditoría**: Fechas automáticas en creación y modificación
5. **Integridad**: Validaciones de FK y datos requeridos

## Consideraciones de Seguridad

- Todas las APIs incluyen validación de entrada
- Manejo de excepciones sin exposición de datos sensibles
- Versionado para prevenir actualizaciones concurrentes
- Logging de operaciones críticas 