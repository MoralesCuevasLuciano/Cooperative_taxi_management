# Estado del Proyecto - Cooperative Taxi Management

**Última actualización:** 26 de Noviembre, 2024

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
- ✅ Entidad `DailyFuelEntity` con campos: `id`, `driver` (ManyToOne), `vehicle` (ManyToOne), `settlement` (ManyToOne → DriverSettlementEntity, nullable), `ticketIssueDate`, `submissionDate`, `amount`, `fuelType` (Enum)
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

**Nota:** Campo `settlement` es ManyToOne nullable hacia `DriverSettlementEntity`.

#### 5. **Ticket Taxi**
- ✅ Entidad `TicketTaxiEntity` con campos: `id`, `vehicle` (ManyToOne), `settlement` (ManyToOne → DriverSettlementEntity, obligatorio), `ticketNumber` (opcional), `startDate` (opcional), `cutDate` (opcional), `amount` (obligatorio, >= 0), `freeKilometers` (opcional, >= 0), `occupiedKilometers` (opcional, >= 0), `trips` (opcional, >= 0)
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
- `GET /ticket-taxi/get/by-settlement/{settlementId}`
- `GET /ticket-taxi/get/by-start-date-range?startDate=...&endDate=...`
- `GET /ticket-taxi/get/by-cut-date-range?startDate=...&endDate=...`
- `GET /ticket-taxi/get/by-vehicle/{vehicleId}/start-date-range?startDate=...&endDate=...`
- `GET /ticket-taxi/get/by-vehicle/{vehicleId}/cut-date-range?startDate=...&endDate=...`
- `GET /ticket-taxi/get/by-settlement/{settlementId}/start-date-range?startDate=...&endDate=...`
- `GET /ticket-taxi/get/by-settlement/{settlementId}/cut-date-range?startDate=...&endDate=...`

**Nota:** Campo `settlement` es ManyToOne obligatorio hacia `DriverSettlementEntity`.

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

#### 6. **Driver Settlement (Rendición de Chofer)**
- ✅ Entidad `DriverSettlementEntity` con campos: `id`, `driver` (ManyToOne → DriverEntity), `ticketAmount` (>= 0), `voucherAmount` (>= 0), `voucherDifference` (puede ser negativo), `finalBalance` (puede ser negativo), `submissionDate` (fecha de entrega)
- ✅ DTO `DriverSettlementDTO`
- ✅ Repository `DriverSettlementRepository` con métodos de búsqueda por chofer y fecha
- ✅ Validator `DriverSettlementValidator` (validaciones: todos los campos obligatorios, montos >= 0 donde corresponde)
- ✅ Service `DriverSettlementService` (CRUD completo + métodos de cálculo)
- ✅ Controller `DriverSettlementController` con endpoints explícitos

**Endpoints:**
- `POST /driver-settlements/create`
- `GET /driver-settlements/list`
- `GET /driver-settlements/get/{id}`
- `GET /driver-settlements/get/by-driver/{driverId}`
- `GET /driver-settlements/get/by-submission-date/{submissionDate}`
- `GET /driver-settlements/get/by-submission-date-range?startDate=...&endDate=...`
- `GET /driver-settlements/get/by-driver/{driverId}/submission-date-range?startDate=...&endDate=...`
- `GET /driver-settlements/calculate/total-tickets/{settlementId}`
- `POST /driver-settlements/calculate/final-balance`
- `PUT /driver-settlements/update/{id}`
- `DELETE /driver-settlements/delete/{id}`

**Métodos de cálculo:**
- `calculateTotalTickets(Long settlementId)`: Suma todos los montos de tickets asociados a la rendición
- `calculateFinalBalance(DriverSettlementDTO)`: Calcula saldo final (ticketAmount - voucherAmount + voucherDifference)

**Relaciones:**
- 1 Driver → 0..* DriverSettlements (ManyToOne desde DriverSettlement)
- 1 DriverSettlement → 0..* TicketTaxis (ManyToOne desde TicketTaxi, obligatorio)
- 1 DriverSettlement → 0..* DailyFuels (ManyToOne desde DailyFuel, nullable)

---

## 🚧 Próximas Tareas

### 📝 Tareas Futuras

- Implementar nuevas funcionalidades según requerimientos
- Optimizaciones de rendimiento si es necesario
- Mejoras en validaciones y manejo de errores

---

## 🔧 Problemas Resueltos

1. ✅ Error "Field 'brand' doesn't have a default value" - Solucionado recreando tabla `models`
2. ✅ Violación SOLID: Servicios accediendo a repositories de otras entidades - Solucionado usando servicios intermedios
3. ✅ Deprecación MySQL dialect - Solucionado cambiando a `MySQLDialect`
4. ✅ Endpoints con misma URL en Swagger - Solucionado con tags explícitos y OpenApiConfig
5. ✅ Inconsistencia de nombres de columnas (español/inglés) - Solucionado cambiando todas las columnas a inglés
6. ✅ Dependencia circular entre `DriverSettlementService` y `TicketTaxiService` - Solucionado usando `@Lazy` en la dependencia
7. ✅ Referencias a `rendicionId` en validators y servicios - Solucionado actualizando a usar relación `settlement`

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
│   ├── DriverSettlementController.java
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
│   │   ├── DriverSettlementDTO.java
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
│   │   ├── DriverSettlementEntity.java
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
│   ├── DriverSettlementRepository.java
│   ├── ModelRepository.java
│   ├── TicketTaxiRepository.java
│   └── VehicleRepository.java
├── services/
│   ├── AddressService.java
│   ├── BrandService.java
│   ├── DailyFuelService.java
│   ├── DriverService.java
│   ├── DriverSettlementService.java
│   ├── ModelService.java
│   ├── TicketTaxiService.java
│   └── VehicleService.java
└── validators/
    ├── AddressValidator.java
    ├── BrandValidator.java
    ├── DailyFuelValidator.java
    ├── DriverSettlementValidator.java
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
- DailyFuel con CRUD completo y filtros avanzados (actualizado con relación a DriverSettlement)
- TicketTaxi con CRUD completo y filtros avanzados (actualizado con relación a DriverSettlement)
- DriverSettlement con CRUD completo, métodos de cálculo y filtros
- Validaciones implementadas
- Principios SOLID aplicados (con `@Lazy` para evitar dependencias circulares)
- Endpoints explícitos
- Documentación Swagger
- Consistencia de nombres de columnas en inglés
- Relaciones JPA correctamente implementadas entre todas las entidades

**⏳ Pendiente:**
- Implementar nuevas funcionalidades según requerimientos futuros
- Optimizaciones y mejoras continuas

---

**¡Listo para continuar con DriverSettlement cuando se resuelvan las preguntas! 🚀**
