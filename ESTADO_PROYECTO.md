# Estado del Proyecto - Cooperative Taxi Management

**Última actualización:** 20 de Noviembre, 2024

---

## 📋 Resumen de lo Implementado Hoy

### ✅ Entidades Completadas

#### 1. **Brand (Marca)**
- ✅ Entidad `BrandEntity` con campos: `id`, `name`, `active`
- ✅ DTO `BrandDTO`
- ✅ Repository `BrandRepository`
- ✅ Validator `BrandValidator` (validaciones de campos y unicidad)
- ✅ Service `BrandService` (CRUD completo)
- ✅ Controller `BrandController` con endpoints explícitos

**Endpoints:**
- `POST /brands/create`
- `GET /brands/list`
- `GET /brands/get/{id}`
- `PUT /brands/update/{id}`
- `DELETE /brands/delete/{id}`

#### 2. **Model (Modelo)**
- ✅ Entidad `ModelEntity` con campos: `id`, `name`, `year`, `brand` (ManyToOne)
- ✅ DTO `ModelDTO`
- ✅ Repository `ModelRepository`
- ✅ Validator `ModelValidator` (validaciones de campos, año no futuro, unicidad)
- ✅ Service `ModelService` (CRUD completo)
- ✅ Controller `ModelController` con endpoints explícitos

**Endpoints:**
- `POST /models/create`
- `GET /models/list`
- `GET /models/get/{id}`
- `GET /models/get/by-brand/{brandId}`
- `PUT /models/update/{id}`
- `DELETE /models/delete/{id}`

**Nota:** El campo `year` está en `ModelEntity`, no en `VehicleEntity`. Validación implementada para que el año no sea futuro (pendiente completar en validator).

#### 3. **Vehicle (Vehículo)**
- ✅ Entidad `VehicleEntity` con campos: `id`, `licensePlate`, `licenseNumber`, `engineNumber`, `chassisNumber`, `vtvExpirationDate`, `active`, `leaveDate`, `model` (ManyToOne)
- ✅ DTO `VehicleDTO`
- ✅ Repository `VehicleRepository`
- ✅ Validator `VehicleValidator` (validaciones de campos y unicidad de patente, licencia, motor, chasis)
- ✅ Service `VehicleService` (CRUD completo + soft delete)
- ✅ Controller `VehicleController` con endpoints explícitos

**Endpoints:**
- `POST /vehicles/create`
- `GET /vehicles/list`
- `GET /vehicles/get/{id}`
- `GET /vehicles/get/license-plate/{licensePlate}`
- `GET /vehicles/get/active`
- `GET /vehicles/get/by-model/{modelId}`
- `PUT /vehicles/update/{id}`
- `DELETE /vehicles/delete/{id}`
- `DELETE /vehicles/delete/{id}/leave-date/{leaveDate}` (formato fecha: YYYY-MM-DD)

### ✅ Mejoras Implementadas

1. **Principios SOLID:**
   - ✅ Cada servicio solo accede a su propio repository
   - ✅ Servicios usan otros servicios (no repositories directamente) para relaciones
   - ✅ Validators con responsabilidad única
   - ✅ Métodos package-private para obtener entidades (ej: `getBrandEntityById()`)

2. **Endpoints Explícitos:**
   - ✅ Todos los endpoints tienen paths explícitos (`/create`, `/update/{id}`, `/delete/{id}`, etc.)
   - ✅ Endpoints modificados en: `MemberController`, `DriverController`, `SubscriberController`

3. **Documentación Swagger:**
   - ✅ Configuración OpenAPI personalizada (`OpenApiConfig.java`)
   - ✅ Tags explícitos en cada operación para mejor organización

4. **Configuración:**
   - ✅ Fix de deprecación MySQL dialect (`MySQL8Dialect` → `MySQLDialect`)
   - ✅ `@Transactional` en servicios
   - ✅ `@DateTimeFormat` para fechas en path variables

### 📁 Archivos de Ejemplo Creados

- `brand_create_example.json`
- `model_create_example.json`
- `vehicle_create_example.json`
- `vehicle_create_example_minimal.json`
- `member_create_example.json`
- `member_create_example_minimal.json`

---

## 🚧 Próxima Tarea: Combustible Diario (Daily Fuel)

### 📝 Contexto

Se necesita implementar la entidad `Combustible_diario` (DailyFuel) basada en el diagrama UML proporcionado.

### 🎯 Requisitos Identificados

#### **Entidad: DailyFuelEntity**

**Relaciones:**
1. **Con Vehicle:** 1 vehículo → 0..* registros de combustible diario (ManyToOne)
2. **Con Driver:** 1 chofer → 0..* registros de combustible diario (ManyToOne)
3. **Con Rendicion_chofer:** 1 rendición → 0..* registros (ManyToOne) - **NO IMPLEMENTAR POR AHORA**

**Campos:**
- `id` (PK)
- `driver` (ManyToOne → DriverEntity) - **FK: id_chofer**
- `vehicle` (ManyToOne → VehicleEntity) - **FK: id_vehiculo**
- `rendicionId` (Long, nullable) - **FK: id_rendicion** - **NO IMPLEMENTAR RELACIÓN JPA POR AHORA**
- `fecha` (LocalDate) - Fecha de emisión del ticket
- `fechaEntrega` (LocalDate) - **NUEVO:** Fecha en que el chofer presentó el ticket a la empresa
- `monto` (Double) - Cantidad monetaria
- `fuelType` (Enum) - **NUEVO:** Tipo de combustible (GNC o Nafta)

#### **Enum: FuelType**
- `GNC`
- `NAFTA`

### ❓ Preguntas Pendientes de Resolver

1. **Validaciones:**
   - ¿`fechaEntrega` puede ser anterior a `fecha`? (lógicamente debería ser posterior o igual)
   - ¿`monto` debe ser siempre positivo?
   - ¿`fechaEntrega` puede ser null o es obligatoria?

2. **Relación con Rendicion_chofer:**
   - ¿Dejamos `rendicionId` como `Long` nullable por ahora?
   - ¿O lo omitimos completamente hasta implementar Rendicion_chofer?

3. **Endpoints adicionales:**
   - ¿Filtrar por vehículo?
   - ¿Filtrar por chofer?
   - ¿Filtrar por rango de fechas?
   - ¿Filtrar por tipo de combustible?

### 📋 Plan de Implementación (Pendiente de Autorización)

1. ✅ Crear Enum `FuelType` con valores `GNC` y `NAFTA`
2. ✅ Crear `DailyFuelEntity` con todas las relaciones y campos
3. ✅ Crear `DailyFuelDTO`
4. ✅ Crear `DailyFuelRepository` con métodos de búsqueda
5. ✅ Crear `DailyFuelValidator` con validaciones
6. ✅ Crear `DailyFuelService` con CRUD completo
7. ✅ Crear `DailyFuelController` con endpoints explícitos

### ⚠️ Notas Importantes

- **NO implementar** `Rendicion_chofer` ni `Ticket_taxi` todavía
- **Solo** trabajar en `Combustible_diario` por ahora
- Mantener principios SOLID (servicios no acceden a repositories de otras entidades)
- Todo el código en inglés excepto documentación Swagger
- Validar que `year` en Model no sea futuro (pendiente completar en ModelValidator)

---

## 🔧 Problemas Resueltos Hoy

1. ✅ Error "Field 'brand' doesn't have a default value" - Solucionado recreando tabla `models`
2. ✅ Violación SOLID: Servicios accediendo a repositories de otras entidades - Solucionado usando servicios intermedios
3. ✅ Deprecación MySQL dialect - Solucionado cambiando a `MySQLDialect`
4. ✅ Endpoints con misma URL en Swagger - Solucionado con tags explícitos y OpenApiConfig

---

## 📚 Estructura del Proyecto

```
backend/src/main/java/com/pepotec/cooperative_taxi_managment/
├── config/
│   └── OpenApiConfig.java
├── controllers/
│   ├── BrandController.java
│   ├── DriverController.java
│   ├── MemberController.java
│   ├── ModelController.java
│   ├── SubscriberController.java
│   └── VehicleController.java
├── models/
│   ├── dto/
│   │   ├── BrandDTO.java
│   │   ├── ModelDTO.java
│   │   └── VehicleDTO.java
│   └── entities/
│       ├── BrandEntity.java
│       ├── ModelEntity.java
│       └── VehicleEntity.java
├── repositories/
│   ├── BrandRepository.java
│   ├── ModelRepository.java
│   └── VehicleRepository.java
├── services/
│   ├── BrandService.java
│   ├── ModelService.java
│   └── VehicleService.java
└── validators/
    ├── BrandValidator.java
    ├── ModelValidator.java
    └── VehicleValidator.java
```

---

## 🎯 Estado Actual

**✅ Completado:**
- Brand, Model, Vehicle con CRUD completo
- Validaciones implementadas
- Principios SOLID aplicados
- Endpoints explícitos
- Documentación Swagger

**⏳ Pendiente:**
- Implementar `DailyFuelEntity` (Combustible Diario)
- Completar validación de año no futuro en ModelValidator
- Implementar `Rendicion_chofer` (futuro)
- Implementar `Ticket_taxi` (futuro)

---

**¡Listo para continuar cuando vuelvas! 🚀**

