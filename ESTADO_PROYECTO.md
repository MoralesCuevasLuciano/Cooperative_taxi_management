# Estado del Proyecto - Cooperative Taxi Management

**Última actualización:** 21 de Noviembre, 2024

---

## 📋 Resumen de Entidades Implementadas

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

#### 4. **Daily Fuel (Combustible Diario)**
- ✅ Entidad `DailyFuelEntity` con campos: `id`, `driver` (ManyToOne), `vehicle` (ManyToOne), `rendicionId` (Long nullable), `ticketIssueDate`, `submissionDate`, `amount`, `fuelType` (Enum)
- ✅ Enum `FuelType` con valores `GNC` y `NAFTA`
- ✅ DTO `DailyFuelDTO`
- ✅ Repository `DailyFuelRepository` con métodos de búsqueda
- ✅ Validator `DailyFuelValidator` (validaciones: submissionDate >= ticketIssueDate, amount positivo, etc.)
- ✅ Service `DailyFuelService` (CRUD completo)
- ✅ Controller `DailyFuelController` con endpoints explícitos

**Endpoints:**
- `POST /daily-fuel/create`
- `GET /daily-fuel/list`
- `GET /daily-fuel/get/{id}`
- `PUT /daily-fuel/update/{id}`
- `DELETE /daily-fuel/delete/{id}`
- `GET /daily-fuel/get/by-vehicle/{vehicleId}`
- `GET /daily-fuel/get/by-driver/{driverId}`
- `GET /daily-fuel/get/by-ticket-issue-date-range?startDate=...&endDate=...`
- `GET /daily-fuel/get/by-submission-date-range?startDate=...&endDate=...`
- `GET /daily-fuel/get/by-fuel-type/{fuelType}`
- `GET /daily-fuel/get/by-vehicle/{vehicleId}/ticket-issue-date-range?startDate=...&endDate=...`
- `GET /daily-fuel/get/by-driver/{driverId}/ticket-issue-date-range?startDate=...&endDate=...`
- `GET /daily-fuel/get/by-vehicle/{vehicleId}/fuel-type/{fuelType}`
- `GET /daily-fuel/get/by-driver/{driverId}/fuel-type/{fuelType}`

**Nota:** Campo `rendicionId` es Long nullable por ahora, sin relación JPA. Se actualizará cuando se implemente `DriverSettlement`.

#### 5. **Ticket Taxi**
- ✅ Entidad `TicketTaxiEntity` con campos: `id`, `vehicle` (ManyToOne), `rendicionId` (Long obligatorio), `ticketNumber` (opcional), `startDate` (opcional), `cutDate` (opcional), `amount` (obligatorio, >= 0), `freeKilometers` (opcional, >= 0), `occupiedKilometers` (opcional, >= 0), `trips` (opcional, >= 0)
- ✅ DTO `TicketTaxiDTO`
- ✅ Repository `TicketTaxiRepository` con métodos de búsqueda
- ✅ Validator `TicketTaxiValidator` (validaciones: cutDate >= startDate si ambas presentes, amount >= 0, campos opcionales >= 0)
- ✅ Service `TicketTaxiService` (CRUD completo)
- ✅ Controller `TicketTaxiController` con endpoints explícitos

**Endpoints:**
- `POST /ticket-taxi/create`
- `GET /ticket-taxi/list`
- `GET /ticket-taxi/get/{id}`
- `GET /ticket-taxi/get/ticket-number/{ticketNumber}`
- `PUT /ticket-taxi/update/{id}`
- `DELETE /ticket-taxi/delete/{id}`
- `GET /ticket-taxi/get/by-vehicle/{vehicleId}`
- `GET /ticket-taxi/get/by-rendicion/{rendicionId}`
- `GET /ticket-taxi/get/by-start-date-range?startDate=...&endDate=...`
- `GET /ticket-taxi/get/by-cut-date-range?startDate=...&endDate=...`
- `GET /ticket-taxi/get/by-vehicle/{vehicleId}/start-date-range?startDate=...&endDate=...`
- `GET /ticket-taxi/get/by-vehicle/{vehicleId}/cut-date-range?startDate=...&endDate=...`
- `GET /ticket-taxi/get/by-rendicion/{rendicionId}/start-date-range?startDate=...&endDate=...`
- `GET /ticket-taxi/get/by-rendicion/{rendicionId}/cut-date-range?startDate=...&endDate=...`

**Nota:** Campo `rendicionId` es Long obligatorio por ahora, sin relación JPA. Se actualizará cuando se implemente `DriverSettlement`.

### ✅ Mejoras Implementadas

1. **Principios SOLID:**
   - ✅ Cada servicio solo accede a su propio repository
   - ✅ Servicios usan otros servicios (no repositories directamente) para relaciones
   - ✅ Validators con responsabilidad única
   - ✅ Métodos package-private para obtener entidades (ej: `getBrandEntityById()`, `getVehicleEntityById()`, `getDriverEntityById()`)

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

5. **Consistencia de Nombres:**
   - ✅ Todas las columnas de base de datos en inglés
   - ✅ Corrección de nombres en español: `id_chofer` → `id_driver`, `id_vehiculo` → `id_vehicle`, `id_rendicion` → `id_settlement`

---

## 🚧 Próxima Tarea: Driver Settlement (Rendicion_chofer)

### 📝 Contexto

Se necesita implementar la entidad `Rendicion_chofer` (DriverSettlement) basada en el diagrama UML proporcionado. Esta entidad representa la rendición de un chofer que agrupa tickets de taxi y registros de combustible diario.

### 🎯 Requisitos Identificados del UML

#### **Entidad: DriverSettlementEntity**

**Relaciones según UML:**
1. **Con Driver:** 1 chofer → 0..* rendiciones (ManyToOne)
2. **Con TicketTaxi:** 1 rendición → 0..* tickets (Composición - ManyToOne desde TicketTaxi)
3. **Con DailyFuel:** Many-to-Many (0..* → 0..*) - **PREGUNTA PENDIENTE**

**Campos según UML:**
- `id` (PK)
- `driver` (ManyToOne → DriverEntity) - **FK: id_driver**
- `ticketAmount` (Double) - `monto_tickets` (>= 0)
- `voucherAmount` (Double) - `monto_vauchers` (>= 0) - **PREGUNTA: ¿Es "vouchers"?**
- `voucherDifference` (Double) - `diferencia_vauchers` (puede ser negativo)
- `finalBalance` (Double) - `saldo_final` (puede ser negativo)

**Métodos según UML:**
- `calculateTotalTickets(): double` - `calcular_total_tickets()`
- `calculateFinalBalance(): double` - `calcular_saldo_final()`

### ❓ Preguntas Pendientes de Resolver

1. **Nombres en inglés:**
   - ¿"vauchers" es "vouchers" (vouchers/vales) o es otra cosa?
   - Nombres propuestos:
     - `DriverSettlementEntity` / `DriverSettlementDTO`
     - `ticketAmount` (monto_tickets)
     - `voucherAmount` (monto_vauchers)
     - `voucherDifference` (diferencia_vauchers)
     - `finalBalance` (saldo_final)
     - `calculateTotalTickets()` (calcular_total_tickets)
     - `calculateFinalBalance()` (calcular_saldo_final)

2. **Relación con TicketTaxi:**
   - El UML muestra composición (diamante relleno) de `Rendicion_chofer` hacia `Ticket_taxi`
   - ¿Implementamos como ManyToOne desde `TicketTaxi` hacia `DriverSettlement`?
   - ¿O prefieres otra estructura?

3. **Relación con DailyFuel:**
   - El UML muestra Many-to-Many entre `Rendicion_chofer` y `Combustible_diario`
   - ¿La implementamos ahora o dejamos el campo `rendicionId` y la agregamos después?
   - Actualmente `DailyFuel` tiene `rendicionId` (Long nullable) y `TicketTaxi` tiene `rendicionId` (Long obligatorio)

4. **Métodos de cálculo:**
   - Los métodos `calculateTotalTickets()` y `calculateFinalBalance()`:
     - ¿Los implementamos en la entidad como métodos de negocio?
     - ¿O en el servicio?
     - ¿O los calculamos automáticamente al guardar/actualizar?

5. **Validaciones:**
   - ¿`ticketAmount` y `voucherAmount` deben ser >= 0? (Confirmado: SÍ)
   - ¿`voucherDifference` y `finalBalance` pueden ser negativos? (Confirmado: SÍ)
   - ¿Alguno de estos campos puede ser null o todos son obligatorios?

6. **Endpoints:**
   - ¿Qué filtros necesitas?
     - Por chofer
     - Por rango de fechas (¿qué fecha usarías? ¿fecha de creación de la rendición?)
     - Otros

### 📋 Tareas Pendientes

1. ⏳ Actualizar `TicketTaxiEntity` y `DailyFuelEntity` para reemplazar `rendicionId` (Long) por relación ManyToOne con `DriverSettlementEntity`
2. ⏳ Crear Enum si es necesario (no parece necesario según UML)
3. ⏳ Crear `DriverSettlementEntity` con todas las relaciones y campos
4. ⏳ Crear `DriverSettlementDTO`
5. ⏳ Crear `DriverSettlementRepository` con métodos de búsqueda
6. ⏳ Crear `DriverSettlementValidator` con validaciones
7. ⏳ Crear `DriverSettlementService` con CRUD completo y métodos de cálculo
8. ⏳ Crear `DriverSettlementController` con endpoints explícitos
9. ⏳ Implementar relación Many-to-Many con `DailyFuel` (si se decide implementarla ahora)

### ⚠️ Notas Importantes

- **ACTUALIZAR:** `TicketTaxi` y `DailyFuel` tienen `rendicionId` como Long. Deben cambiarse a relación ManyToOne con `DriverSettlement`
- Mantener principios SOLID (servicios no acceden a repositories de otras entidades)
- Todo el código en inglés excepto documentación Swagger
- Todas las columnas de base de datos en inglés

---

## 🔧 Problemas Resueltos

1. ✅ Error "Field 'brand' doesn't have a default value" - Solucionado recreando tabla `models`
2. ✅ Violación SOLID: Servicios accediendo a repositories de otras entidades - Solucionado usando servicios intermedios
3. ✅ Deprecación MySQL dialect - Solucionado cambiando a `MySQLDialect`
4. ✅ Endpoints con misma URL en Swagger - Solucionado con tags explícitos y OpenApiConfig
5. ✅ Inconsistencia de nombres de columnas (español/inglés) - Solucionado cambiando todas las columnas a inglés

---

## 📚 Estructura del Proyecto Actual

```
backend/src/main/java/com/pepotec/cooperative_taxi_managment/
├── config/
│   └── OpenApiConfig.java
├── controllers/
│   ├── BrandController.java
│   ├── DailyFuelController.java
│   ├── DriverController.java
│   ├── MemberController.java
│   ├── ModelController.java
│   ├── SubscriberController.java
│   ├── TicketTaxiController.java
│   └── VehicleController.java
├── models/
│   ├── dto/
│   │   ├── BrandDTO.java
│   │   ├── DailyFuelDTO.java
│   │   ├── DriverDTO.java
│   │   ├── MemberDTO.java
│   │   ├── ModelDTO.java
│   │   ├── PersonDTO.java
│   │   ├── SubscriberDTO.java
│   │   ├── TicketTaxiDTO.java
│   │   └── VehicleDTO.java
│   ├── entities/
│   │   ├── AddressEntity.java
│   │   ├── BrandEntity.java
│   │   ├── DailyFuelEntity.java
│   │   ├── DriverEntity.java
│   │   ├── MemberEntity.java
│   │   ├── ModelEntity.java
│   │   ├── PersonEntity.java
│   │   ├── SubscriberEntity.java
│   │   ├── TicketTaxiEntity.java
│   │   └── VehicleEntity.java
│   └── enums/
│       ├── FuelType.java
│       └── MemberRole.java
├── repositories/
│   ├── BrandRepository.java
│   ├── DailyFuelRepository.java
│   ├── DriverRepository.java
│   ├── ModelRepository.java
│   ├── TicketTaxiRepository.java
│   └── VehicleRepository.java
├── services/
│   ├── AddressService.java
│   ├── BrandService.java
│   ├── DailyFuelService.java
│   ├── DriverService.java
│   ├── ModelService.java
│   ├── TicketTaxiService.java
│   └── VehicleService.java
└── validators/
    ├── AddressValidator.java
    ├── BrandValidator.java
    ├── DailyFuelValidator.java
    ├── DriverValidator.java
    ├── MemberValidator.java
    ├── ModelValidator.java
    ├── PersonValidator.java
    ├── TicketTaxiValidator.java
    └── VehicleValidator.java
```

---

## 🎯 Estado Actual

**✅ Completado:**
- Brand, Model, Vehicle con CRUD completo
- DailyFuel con CRUD completo y filtros avanzados
- TicketTaxi con CRUD completo y filtros avanzados
- Validaciones implementadas
- Principios SOLID aplicados
- Endpoints explícitos
- Documentación Swagger
- Consistencia de nombres de columnas en inglés

**⏳ Pendiente:**
- Implementar `DriverSettlementEntity` (Rendicion_chofer)
- Actualizar `TicketTaxiEntity` y `DailyFuelEntity` para usar relación ManyToOne con `DriverSettlement` en lugar de `rendicionId` (Long)
- Resolver preguntas pendientes sobre `DriverSettlement`
- Implementar relación Many-to-Many entre `DriverSettlement` y `DailyFuel` (si se decide hacerlo ahora)

---

**¡Listo para continuar con DriverSettlement cuando se resuelvan las preguntas! 🚀**
