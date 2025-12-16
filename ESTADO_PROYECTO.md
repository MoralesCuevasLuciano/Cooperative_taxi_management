# Estado del Proyecto - Cooperative Taxi Management

**Última actualización:** 15 de Diciembre, 2024

**Actualización reciente:** Sistema de porcentajes de combustible y FuelReimbursement implementado completamente.

---

## 📋 Resumen de Entidades Implementadas

### ✅ Entidades Completadas

#### 1. **Brand (Marca)**
- ✅ Entidad `BrandEntity` con campos: `id`, `name`, `active`
- ✅ DTO `BrandDTO` ubicado en `models.dto.brand.BrandDTO`
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
- ✅ DTO `ModelDTO` ubicado en `models.dto.model.ModelDTO`
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
- ✅ DTO `VehicleDTO` y `VehicleCreateDTO` ubicados en `models.dto.vehicle.*`
- ✅ Repository `VehicleRepository`
- ✅ Validator `VehicleValidator` (validaciones de campos y unicidad de patente, licencia, motor, chasis)
- ✅ Service `VehicleService` (CRUD completo + soft delete + creación automática de cuenta)
- ✅ Controller `VehicleController` con endpoints explícitos

**Endpoints:**
- `POST /vehicles/create` (acepta `VehicleCreateDTO` con `modelId` en lugar de objeto completo)
- `GET /vehicles/list`
- `GET /vehicles/get/{id}`
- `GET /vehicles/get/license-plate/{licensePlate}`
- `GET /vehicles/get/active`
- `GET /vehicles/get/by-model/{modelId}`
- `PUT /vehicles/update/{id}`
- `DELETE /vehicles/delete/{id}`
- `DELETE /vehicles/delete/{id}/leave-date/{leaveDate}` (formato fecha: YYYY-MM-DD)

**Nota:** Al crear un vehículo, se crea automáticamente una `VehicleAccount` con balance 0.

#### 4. **Person (Persona) - Clase Abstracta**
- ✅ Entidad abstracta `PersonEntity` con campos comunes: `id`, `firstName`, `secondName`, `fatherSurname`, `motherSurname`, `dni`, `cuit`, `phone`, `email`, `birthDate`, `active`
- ✅ DTO abstracto `PersonDTO` ubicado en `models.dto.person.PersonDTO`
- ✅ Validator `PersonValidator` con validaciones comunes

#### 5. **Member (Socio)**
- ✅ Entidad `MemberEntity` que extiende `PersonEntity` con campos adicionales: `joinDate`, `leaveDate`, `role` (Enum), `address` (OneToOne)
- ✅ DTO `MemberDTO` ubicado en `models.dto.person.member.MemberDTO`
- ✅ Repository `MemberRepository`
- ✅ Validator `MemberValidator` (validaciones específicas de Member)
- ✅ Service `MemberService` (CRUD completo + soft delete + creación automática de cuenta)
- ✅ Controller `MemberController` con endpoints explícitos

**Endpoints:**
- `POST /members/create`
- `GET /members/list`
- `GET /members/get/{id}`
- `GET /members/get/dni/{dni}`
- `GET /members/get/active`
- `PUT /members/update/{id}`
- `DELETE /members/delete/{id}`

**Nota:** Al crear un socio, se crea automáticamente una `MemberAccount` con balance 0.

#### 6. **Subscriber (Abonado)**
- ✅ Entidad `SubscriberEntity` que extiende `PersonEntity` con campo adicional: `licenceNumbers` (List<String>)
- ✅ DTO `SubscriberDTO` ubicado en `models.dto.person.subscriber.SubscriberDTO`
- ✅ Repository `SubscriberRepository`
- ✅ Validator `SubscriberValidator` (validaciones específicas de Subscriber)
- ✅ Service `SubscriberService` (CRUD completo + creación automática de cuenta)
- ✅ Controller `SubscriberController` con endpoints explícitos

**Endpoints:**
- `POST /subscribers/create`
- `GET /subscribers/list`
- `GET /subscribers/get/{id}`
- `GET /subscribers/get/dni/{dni}`
- `GET /subscribers/get/active`
- `PUT /subscribers/update/{id}`
- `DELETE /subscribers/delete/{id}`

**Nota:** Al crear un abonado, se crea automáticamente una `SubscriberAccount` con balance 0.

#### 7. **Driver (Chofer)**
- ✅ Entidad `DriverEntity` que extiende `MemberEntity` con campo adicional: `expirationRegistrationDate`
- ✅ DTO `DriverDTO` ubicado en `models.dto.person.member.driver.DriverDTO`
- ✅ Repository `DriverRepository`
- ✅ Validator `DriverValidator` (validaciones específicas de Driver)
- ✅ Service `DriverService` (CRUD completo + soft delete)
- ✅ Controller `DriverController` con endpoints explícitos

**Endpoints:**
- `POST /drivers/create`
- `GET /drivers/list`
- `GET /drivers/get/{id}`
- `GET /drivers/get/dni/{dni}`
- `GET /drivers/get/active`
- `PUT /drivers/update/{id}`
- `DELETE /drivers/delete/{id}`

**Nota:** Al crear un chofer, se crea automáticamente una `MemberAccount` con balance 0.

#### 8. **Account Entities (Cuentas)**
- ✅ Clase abstracta `AbstractAccountEntity` con `@MappedSuperclass` que contiene: `id`, `balance` (puede ser negativo), `lastModified` (nullable), `active` (soft delete)
- ✅ Entidad `MemberAccountEntity` que extiende `AbstractAccountEntity` con relación OneToOne a `MemberEntity`
- ✅ Entidad `SubscriberAccountEntity` que extiende `AbstractAccountEntity` con relación OneToOne a `SubscriberEntity`
- ✅ Entidad `VehicleAccountEntity` que extiende `AbstractAccountEntity` con relación OneToOne a `VehicleEntity`
- ✅ DTOs `MemberAccountDTO`, `SubscriberAccountDTO`, `VehicleAccountDTO` ubicados en sus respectivas carpetas
- ✅ DTOs de creación `MemberAccountCreateDTO`, `SubscriberAccountCreateDTO`, `VehicleAccountCreateDTO`
- ✅ Repositories con métodos de filtrado: `MemberAccountRepository`, `SubscriberAccountRepository`, `VehicleAccountRepository`
- ✅ Validators: `MemberAccountValidator`, `SubscriberAccountValidator`, `VehicleAccountValidator`
- ✅ Services: `MemberAccountService`, `SubscriberAccountService`, `VehicleAccountService` (CRUD completo + soft delete)
- ✅ Controllers: `MemberAccountController`, `SubscriberAccountController`, `VehicleAccountController`

**Endpoints de Cuentas:**
- `POST /member-accounts/members/{memberId}` - Crear cuenta de socio
- `GET /member-accounts/{id}` - Obtener cuenta por ID
- `GET /member-accounts/members/{memberId}` - Obtener cuenta por ID de socio
- `GET /member-accounts/list` - Listar todas las cuentas
- `GET /member-accounts/active` - Listar cuentas activas
- `PUT /member-accounts/update/{id}` - Actualizar cuenta
- `DELETE /member-accounts/delete/{id}` - Soft delete (marca como inactiva)

(Endpoints similares para `subscriber-accounts` y `vehicle-accounts`)

**Nota:** Las cuentas usan soft delete (campo `active`). El balance puede ser negativo.

#### 9. **Driver Settlement (Rendición de Chofer)**
- ✅ Entidad `DriverSettlementEntity` con campos: `id`, `driver` (ManyToOne → DriverEntity), `ticketAmount` (>= 0), `voucherAmount` (>= 0), `voucherDifference` (puede ser negativo), `finalBalance` (puede ser negativo), `submissionDate` (fecha de entrega)
- ✅ DTOs `DriverSettlementDTO` y `DriverSettlementCreateDTO` ubicados en `models.dto.driversettlement.*`
- ✅ Repository `DriverSettlementRepository` con métodos de búsqueda por chofer y fecha
- ✅ Validator `DriverSettlementValidator` (validaciones: todos los campos obligatorios, montos >= 0 donde corresponde)
- ✅ Service `DriverSettlementService` (CRUD completo + métodos de cálculo)
- ✅ Controller `DriverSettlementController` con endpoints explícitos

**Endpoints:**
- `POST /drivers/{driverId}/settlements` - Crear rendición (driverId en path, `DriverSettlementCreateDTO` en body)
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

#### 10. **Ticket Taxi**
- ✅ Entidad `TicketTaxiEntity` con campos: `id`, `vehicle` (ManyToOne), `settlement` (ManyToOne → DriverSettlementEntity, obligatorio), `ticketNumber` (opcional), `startDate` (opcional), `cutDate` (opcional), `amount` (obligatorio, >= 0), `freeKilometers` (opcional, >= 0), `occupiedKilometers` (opcional, >= 0), `trips` (opcional, >= 0)
- ✅ DTOs `TicketTaxiDTO` y `TicketTaxiCreateDTO` ubicados en `models.dto.tickettaxi.*`
- ✅ Repository `TicketTaxiRepository` con métodos de búsqueda
- ✅ Validator `TicketTaxiValidator` (validaciones: cutDate >= startDate si ambas presentes, amount >= 0, campos opcionales >= 0)
- ✅ Service `TicketTaxiService` (CRUD completo)
- ✅ Controller `TicketTaxiController` con endpoints explícitos

**Endpoints:**
- `POST /settlements/{settlementId}/vehicles/{vehicleId}` - Crear ticket (settlementId y vehicleId en path, `TicketTaxiCreateDTO` en body)
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

#### 11. **Daily Fuel (Combustible Diario)**
- ✅ Entidad `DailyFuelEntity` con campos: `id`, `driver` (ManyToOne), `vehicle` (ManyToOne), `settlement` (ManyToOne → DriverSettlementEntity, obligatorio), `ticketIssueDate`, `submissionDate`, `amount`, `fuelType` (Enum)
- ✅ Enum `FuelType` con valores `GNC` y `NAFTA`
- ✅ DTOs `DailyFuelDTO` y `DailyFuelCreateDTO` ubicados en `models.dto.dailyfuel.*`
- ✅ Repository `DailyFuelRepository` con métodos de búsqueda
- ✅ Validator `DailyFuelValidator` (validaciones: submissionDate >= ticketIssueDate, amount positivo, etc.)
- ✅ Service `DailyFuelService` (CRUD completo)
- ✅ Controller `DailyFuelController` con endpoints explícitos

**Endpoints:**
- `POST /drivers/{driverId}/vehicles/{vehicleId}?settlementId={settlementId}` - Crear combustible (driverId y vehicleId en path, settlementId como query param obligatorio, `DailyFuelCreateDTO` en body)
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

**Nota:** Campo `settlement` es ManyToOne obligatorio hacia `DriverSettlementEntity`.

---

## 🎯 Trabajo Realizado Hoy (26 de Noviembre, 2024)

### 1. **Refactorización Completa de Estructura de DTOs**
- ✅ Reorganización de todos los DTOs en estructura de carpetas por entidad
- ✅ Estructura implementada:
  - `models.dto.person/` - Contiene `PersonDTO` y subcarpetas:
    - `member/` - Contiene `MemberDTO` y subcarpetas:
      - `driver/` - Contiene `DriverDTO`
      - `account/` - Contiene `MemberAccountDTO` y `MemberAccountCreateDTO`
    - `subscriber/` - Contiene `SubscriberDTO` y subcarpeta:
      - `account/` - Contiene `SubscriberAccountDTO` y `SubscriberAccountCreateDTO`
  - `models.dto.vehicle/` - Contiene `VehicleDTO`, `VehicleCreateDTO` y subcarpeta:
    - `account/` - Contiene `VehicleAccountDTO` y `VehicleAccountCreateDTO`
  - `models.dto.driversettlement/` - Contiene `DriverSettlementDTO` y `DriverSettlementCreateDTO`
  - `models.dto.dailyfuel/` - Contiene `DailyFuelDTO` y `DailyFuelCreateDTO`
  - `models.dto.tickettaxi/` - Contiene `TicketTaxiDTO` y `TicketTaxiCreateDTO`
  - `models.dto.address/` - Contiene `AddressDTO`
  - `models.dto.brand/` - Contiene `BrandDTO`
  - `models.dto.model/` - Contiene `ModelDTO`
- ✅ Actualización de todos los imports en:
  - 12 Services
  - 12 Controllers
  - 14 Validators
  - Referencias entre DTOs
- ✅ Eliminación de DTOs duplicados de la raíz (21 archivos eliminados)

### 2. **Implementación de Sistema de Cuentas**
- ✅ Creación de entidades de cuentas con herencia usando `@MappedSuperclass`:
  - `AbstractAccountEntity` (clase abstracta con campos comunes)
  - `MemberAccountEntity` (OneToOne con MemberEntity)
  - `SubscriberAccountEntity` (OneToOne con SubscriberEntity)
  - `VehicleAccountEntity` (OneToOne con VehicleEntity)
- ✅ Implementación completa de CRUD para las tres entidades de cuentas:
  - Repositories con métodos de filtrado (por ID de dueño, nombre, DNI, patente, etc.)
  - Services con soft delete (campo `active`)
  - Controllers con endpoints RESTful
  - Validators con validaciones específicas
- ✅ Creación automática de cuentas:
  - Al crear un `Member` → se crea automáticamente `MemberAccount` con balance 0
  - Al crear un `Subscriber` → se crea automáticamente `SubscriberAccount` con balance 0
  - Al crear un `Vehicle` → se crea automáticamente `VehicleAccount` con balance 0
  - Al crear un `Driver` → se crea automáticamente `MemberAccount` con balance 0 (ya que Driver extiende Member)

### 3. **Mejoras en Endpoints de Creación**
- ✅ Refactorización de endpoints de creación para usar DTOs específicos (`*CreateDTO`) y path variables:
  - `POST /drivers/{driverId}/settlements` - DriverSettlement con driverId en path
  - `POST /settlements/{settlementId}/vehicles/{vehicleId}` - TicketTaxi con IDs en path
  - `POST /drivers/{driverId}/vehicles/{vehicleId}?settlementId={settlementId}` - DailyFuel con IDs en path/query
  - `POST /vehicles/create` - Vehicle con `VehicleCreateDTO` que acepta `modelId` en lugar de objeto completo

### 4. **Mejoras en Validaciones**
- ✅ Validación de balance permitiendo valores negativos (las cuentas pueden tener deuda)
- ✅ Validación de `lastModified` como nullable (cuando la cuenta está recién creada)
- ✅ Implementación de soft delete en cuentas (campo `active`)

---

## 🚧 Tareas Pendientes

_No hay tareas pendientes críticas en este momento._

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
│   ├── MemberAccountController.java
│   ├── MemberController.java
│   ├── ModelController.java
│   ├── SubscriberAccountController.java
│   ├── SubscriberController.java
│   ├── TicketTaxiController.java
│   ├── VehicleAccountController.java
│   └── VehicleController.java
├── models/
│   ├── dto/
│   │   ├── address/
│   │   │   └── AddressDTO.java
│   │   ├── brand/
│   │   │   └── BrandDTO.java
│   │   ├── dailyfuel/
│   │   │   ├── DailyFuelCreateDTO.java
│   │   │   └── DailyFuelDTO.java
│   │   ├── driversettlement/
│   │   │   ├── DriverSettlementCreateDTO.java
│   │   │   └── DriverSettlementDTO.java
│   │   ├── model/
│   │   │   └── ModelDTO.java
│   │   ├── person/
│   │   │   ├── PersonDTO.java
│   │   │   ├── member/
│   │   │   │   ├── MemberDTO.java
│   │   │   │   ├── account/
│   │   │   │   │   ├── MemberAccountCreateDTO.java
│   │   │   │   │   └── MemberAccountDTO.java
│   │   │   │   └── driver/
│   │   │   │       └── DriverDTO.java
│   │   │   └── subscriber/
│   │   │       ├── SubscriberDTO.java
│   │   │       └── account/
│   │   │           ├── SubscriberAccountCreateDTO.java
│   │   │           └── SubscriberAccountDTO.java
│   │   ├── tickettaxi/
│   │   │   ├── TicketTaxiCreateDTO.java
│   │   │   └── TicketTaxiDTO.java
│   │   └── vehicle/
│   │       ├── VehicleCreateDTO.java
│   │       ├── VehicleDTO.java
│   │       └── account/
│   │           ├── VehicleAccountCreateDTO.java
│   │           └── VehicleAccountDTO.java
│   ├── entities/
│   │   ├── AbstractAccountEntity.java
│   │   ├── AddressEntity.java
│   │   ├── BrandEntity.java
│   │   ├── DailyFuelEntity.java
│   │   ├── DriverEntity.java
│   │   ├── DriverSettlementEntity.java
│   │   ├── MemberAccountEntity.java
│   │   ├── MemberEntity.java
│   │   ├── ModelEntity.java
│   │   ├── PersonEntity.java
│   │   ├── SubscriberAccountEntity.java
│   │   ├── SubscriberEntity.java
│   │   ├── TicketTaxiEntity.java
│   │   └── VehicleAccountEntity.java
│   │   └── VehicleEntity.java
│   └── enums/
│       ├── FuelType.java
│       └── MemberRole.java
├── repositories/
│   ├── AddressRepository.java
│   ├── BrandRepository.java
│   ├── DailyFuelRepository.java
│   ├── DriverRepository.java
│   ├── DriverSettlementRepository.java
│   ├── MemberAccountRepository.java
│   ├── MemberRepository.java
│   ├── ModelRepository.java
│   ├── SubscriberAccountRepository.java
│   ├── SubscriberRepository.java
│   ├── TicketTaxiRepository.java
│   ├── VehicleAccountRepository.java
│   └── VehicleRepository.java
├── services/
│   ├── AddressService.java
│   ├── BrandService.java
│   ├── DailyFuelService.java
│   ├── DriverService.java
│   ├── DriverSettlementService.java
│   ├── MemberAccountService.java
│   ├── MemberService.java
│   ├── ModelService.java
│   ├── SubscriberAccountService.java
│   ├── SubscriberService.java
│   ├── TicketTaxiService.java
│   ├── VehicleAccountService.java
│   └── VehicleService.java
└── validators/
    ├── AddressValidator.java
    ├── BrandValidator.java
    ├── DailyFuelValidator.java
    ├── DriverSettlementValidator.java
    ├── DriverValidator.java
    ├── MemberAccountValidator.java
    ├── MemberValidator.java
    ├── ModelValidator.java
    ├── PersonValidator.java
    ├── SubscriberAccountValidator.java
    ├── SubscriberValidator.java
    ├── TicketTaxiValidator.java
    ├── VehicleAccountValidator.java
    └── VehicleValidator.java
```

---

## 🔧 Problemas Resueltos

1. ✅ Error "Field 'brand' doesn't have a default value" - Solucionado recreando tabla `models`
2. ✅ Violación SOLID: Servicios accediendo a repositories de otras entidades - Solucionado usando servicios intermedios
3. ✅ Deprecación MySQL dialect - Solucionado cambiando a `MySQLDialect`
4. ✅ Endpoints con misma URL en Swagger - Solucionado con tags explícitos y OpenApiConfig
5. ✅ Inconsistencia de nombres de columnas (español/inglés) - Solucionado cambiando todas las columnas a inglés
6. ✅ Dependencia circular entre `DriverSettlementService` y `TicketTaxiService` - Solucionado usando `@Lazy` en la dependencia
7. ✅ Referencias a `rendicionId` en validators y servicios - Solucionado actualizando a usar relación `settlement`
8. ✅ DTOs desorganizados en la raíz - Solucionado reorganizando en estructura de carpetas por entidad
9. ✅ Endpoints de creación con objetos completos en body - Solucionado usando DTOs específicos y path variables
10. ✅ Falta de sistema de cuentas - Solucionado implementando entidades de cuentas con herencia y CRUD completo
11. ✅ Error "Row was updated or deleted by another transaction" en Swagger - Solucionado usando DTOs específicos de creación que excluyen el campo `id`
12. ✅ Error "The active status cannot be null" al crear vehículos - Solucionado estableciendo explícitamente `active = true` en los métodos `convertCreateDtoToEntity` de todos los servicios de cuentas
13. ✅ Creación automática de cuenta para choferes - Solucionado implementando creación automática de `MemberAccount` al crear un `Driver`, con método sobrecargado en `MemberAccountService` para aceptar `MemberEntity` directamente

---

## 🎯 Estado Actual del Proyecto

**✅ Completado:**
- Brand, Model, Vehicle con CRUD completo
- Person, Member, Subscriber, Driver con CRUD completo y herencia correctamente implementada
- Sistema de cuentas completo (MemberAccount, SubscriberAccount, VehicleAccount) con:
  - Herencia usando `@MappedSuperclass`
  - CRUD completo con soft delete
  - Creación automática al crear Member, Subscriber, Vehicle o Driver
  - Validaciones que permiten balance negativo
  - Campo `active` establecido explícitamente al crear cuentas (evita errores de validación)
- DailyFuel con CRUD completo, filtros avanzados y sistema de porcentajes:
  - Campos `cooperativePercentage` y `driverPercentage`
  - Asignación automática de porcentajes (último del mismo tipo o 50/50 por defecto)
  - Acumulación automática de crédito de combustible
- FuelReimbursement (Reintegro de Combustible) con CRUD completo:
  - Relación OneToOne con MemberAccount
  - Creación automática al acumular crédito del primer DailyFuel
  - Métodos de acumulación y reintegro quincenal
  - Endpoints REST completos
- TicketTaxi con CRUD completo y filtros avanzados
- DriverSettlement con CRUD completo, métodos de cálculo y filtros
- Refactorización completa de estructura de DTOs organizados por entidad
- Endpoints de creación mejorados usando DTOs específicos y path variables
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

## 📝 Notas para IA del Futuro

Este proyecto es un sistema de gestión de taxis cooperativos desarrollado en Spring Boot con JPA/Hibernate. 

**Patrones importantes a seguir:**
1. **DTOs organizados por entidad:** Todos los DTOs están organizados en carpetas según su entidad relacionada. Las entidades con herencia tienen sus DTOs anidados (ej: `person/member/driver/DriverDTO`).
2. **DTOs de creación separados:** Para crear entidades, usar `*CreateDTO` que solo contiene los campos necesarios para la creación, sin IDs ni objetos completos de relaciones.
3. **Path variables para relaciones:** Al crear entidades relacionadas, usar path variables para los IDs de las entidades padre (ej: `/drivers/{driverId}/settlements`).
4. **Creación automática de cuentas:** Al crear Member, Subscriber o Vehicle, siempre crear automáticamente su cuenta asociada con balance 0.
5. **Soft delete:** Las cuentas usan soft delete (campo `active`), no eliminación física.
6. **Balance negativo permitido:** Las cuentas pueden tener balance negativo (deuda).

**Para continuar el trabajo:**
- Revisar la sección "Tareas Pendientes" para ver qué falta implementar
- Seguir los patrones establecidos en el código existente
- Mantener la estructura de carpetas de DTOs
- Asegurarse de actualizar este archivo cuando se completen tareas

---

## 🏗️ Decisiones Arquitectónicas - Sistema de Combustible y Reintegros

**Fecha de decisión:** 15 de Diciembre, 2024

### 📌 Contexto del Negocio

Cuando un chofer rinde (`DriverSettlement`), presenta:
- **Tickets de trabajo** (`TicketTaxi`): ingresos del chofer
- **Tickets de combustible** (`DailyFuel`): gastos de combustible

Del gasto de combustible, un porcentaje (generalmente 50%) se acumula como crédito para el chofer, pero este crédito:
- **NO** es saldo normal de la cuenta (`MemberAccount.balance`)
- Se **reintegra quincenalmente** (manual)
- Al reintegrarse, se suma al balance de la cuenta
- Luego se usa para descontar la **cuota mensual de socio** (a implementar)

### ✅ Decisiones Tomadas

#### 1. **Porcentajes de Combustible en DailyFuel**

**Implementación:**
- Agregar campos `cooperativePercentage` y `driverPercentage` en `DailyFuelEntity`
- **Validación:** La suma de ambos debe ser 100
- **Lógica de valores por defecto:**
  1. Si no se especifican porcentajes al crear un `DailyFuel`:
     - Buscar el último `DailyFuel` del mismo `fuelType` (GNC/NAFTA) para ese `driver`
     - Si existe, usar esos porcentajes
     - Si no existe, usar 50/50 por defecto
  2. El usuario puede modificar los porcentajes manualmente

**Razón:** Permite flexibilidad para casos especiales (ej: auto que solo anda a nafta temporalmente → 70% chofer, 30% cooperativa) manteniendo consistencia con el último uso del mismo tipo de combustible.

#### 2. **Entidad FuelReimbursement (Reintegro de Combustible)**

**Nueva entidad:** `FuelReimbursementEntity`

**Campos:**
- `id` (Long)
- `memberAccount` (OneToOne → MemberAccountEntity) - Relación única con la cuenta del chofer
- `accumulatedAmount` (Double) - Monto acumulado pendiente de reintegro
- `lastReimbursementDate` (LocalDate, nullable) - Última fecha de reintegro quincenal
- `createdDate` (LocalDate) - Fecha de creación
- `active` (Boolean) - Soft delete

**Propósito:**
- Mantener separado el saldo de combustible del balance general de la cuenta
- Acumular el crédito del chofer (porcentaje del combustible) hasta el reintegro
- Facilitar el reintegro quincenal manual
- Preparar para el descuento de cuota mensual

**Flujo:**
1. Al crear un `DailyFuel`:
   - Calcular: `driverCredit = amount * (driverPercentage / 100)`
   - Si no existe `FuelReimbursement` para el chofer, se crea automáticamente
   - Acumular en `FuelReimbursement.accumulatedAmount` del chofer
2. Reintegro quincenal (manual):
   - Sumar `accumulatedAmount` a `MemberAccount.balance`
   - Resetear `accumulatedAmount` a 0
   - Actualizar `lastReimbursementDate`
3. Cuota mensual (a implementar):
   - Usar `accumulatedAmount` (si existe) para descontar de la cuota

**Creación:**
- **Automática:** Se crea automáticamente al crear el primer `DailyFuel` con porcentaje del chofer > 0
- **Manual:** También se puede crear explícitamente mediante endpoint `POST /fuel-reimbursements/member-accounts/{memberAccountId}`

#### 3. **Reintegros y Cuotas**

- **Reintegro quincenal:** Manual (no automático)
- **Cuota de socio:** Mensual (a implementar)
- **Historial de reintegros:** Pendiente para implementación futura (clase `MovimientoDinero` o similar)

### ✅ Implementación Completada (15 de Diciembre, 2024)

1. **✅ DailyFuelEntity modificado:**
   - ✅ Agregados `cooperativePercentage` (Double, nullable)
   - ✅ Agregados `driverPercentage` (Double, nullable)
   - ✅ Validaciones actualizadas en `DailyFuelValidator` (suma debe ser 100)

2. **✅ FuelReimbursementEntity creado:**
   - ✅ Entidad completa con todos los campos definidos
   - ✅ Repository `FuelReimbursementRepository` con métodos de búsqueda
   - ✅ DTOs: `FuelReimbursementDTO` y `FuelReimbursementCreateDTO`
   - ✅ Validator `FuelReimbursementValidator` con validaciones completas
   - ✅ Service `FuelReimbursementService` con métodos:
     - ✅ `createFuelReimbursement()` - Crear registro manualmente
     - ✅ `accumulateFuelCredit()` - Acumular crédito (crea automáticamente si no existe)
     - ✅ `reimburseFuelCredit()` - Reintegrar quincenalmente al balance
     - ✅ CRUD completo
   - ✅ Controller `FuelReimbursementController` con endpoints REST

3. **✅ DailyFuelService modificado:**
   - ✅ Lógica para buscar último `DailyFuel` del mismo `fuelType` para el chofer
   - ✅ Asignación automática de porcentajes por defecto (último del mismo tipo o 50/50)
   - ✅ Acumulación automática de crédito al crear `DailyFuel`
   - ✅ Creación automática de `FuelReimbursement` si no existe

**Endpoints implementados:**
- `POST /fuel-reimbursements/member-accounts/{memberAccountId}` - Crear reintegro
- `GET /fuel-reimbursements/get/{id}` - Obtener por ID
- `GET /fuel-reimbursements/get/by-member-account/{memberAccountId}` - Obtener por cuenta
- `GET /fuel-reimbursements/list` - Listar todos
- `POST /fuel-reimbursements/member-accounts/{memberAccountId}/accumulate?amount={amount}` - Acumular crédito
- `POST /fuel-reimbursements/member-accounts/{memberAccountId}/reimburse` - Reintegrar crédito
- `PUT /fuel-reimbursements/update/{id}` - Actualizar
- `DELETE /fuel-reimbursements/delete/{id}` - Soft delete

### 📋 Tareas Pendientes

4. **Futuro (no implementar ahora):**
   - Sistema de historial de movimientos (`MovimientoDinero`)
   - Sistema de cuotas mensuales de socio

### 🔄 Relaciones Actualizadas

```
DailyFuelEntity
├── cooperativePercentage (nuevo)
├── driverPercentage (nuevo)
└── ... (campos existentes)

FuelReimbursementEntity (implementado)
├── memberAccount (OneToOne → MemberAccountEntity, unique = true)
└── ... (campos definidos)

MemberAccountEntity
└── (relación OneToOne con FuelReimbursementEntity)
```

---

**¡Sistema completo y funcionando correctamente! 🚀**
