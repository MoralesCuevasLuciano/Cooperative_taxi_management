# Boceto: Sistema de Movimientos de Cuenta (Account Movements)

## 📋 Estructura Propuesta

### 1. **AbstractAccountMovementEntity** (Clase Abstracta - @MappedSuperclass)
**Propósito:** Clase base para movimientos que NO afectan el saldo hasta que se "agregan"

**Estrategia:** JOINED (heredada por AccountIncomeEntity y AbstractAccountExpenseEntity)

**Atributos:**
- `id` (Long) - PK
- `memberAccount` (ManyToOne → MemberAccountEntity, nullable)
- `subscriberAccount` (ManyToOne → SubscriberAccountEntity, nullable)
- `vehicleAccount` (ManyToOne → VehicleAccountEntity, nullable)
- `amount` (Double, siempre positivo) - Monto del movimiento (el signo lo define Income vs Expense)
- `yearMonth` (String, formato YYYY-MM) - Período devengado del movimiento (mes al que corresponde, aunque se pague después)
- `added` (Boolean, default false) - Si ya fue agregado al saldo general (se actualiza automática y manualmente)
- `addedDate` (LocalDate, nullable) - Fecha en que se agregó al saldo
- `note` (String, nullable) - Nota/descripción informativa
- `currentInstallment` (Integer, nullable) - Número de cuota actual (si es en cuotas)
- `finalInstallment` (Integer, nullable) - Número de cuota final (si es en cuotas)
- `active` (Boolean, default true) - Soft delete

**Validaciones:**
- Solo una cuenta puede estar presente (MemberAccount, SubscriberAccount o VehicleAccount)
- Si `currentInstallment` no es null, `finalInstallment` tampoco puede ser null
- `currentInstallment` <= `finalInstallment` si ambos están presentes
- `yearMonth` debe seguir formato YYYY-MM (Pattern: `^\\d{4}-(0[1-9]|1[0-2])$`)
- `amount` > 0 (siempre positivo)

**Índices:**
- `(id_member_account, period, added)` - Para búsquedas eficientes por cuenta, período y estado
- `(id_subscriber_account, period, added)` - Similar para subscriber
- `(id_vehicle_account, period, added)` - Similar para vehicle

**Notas:**
- El campo `added` indica que el movimiento fue agregado al saldo de la cuenta (como ingreso o egreso según la clase)
- **`added` puede ser `true` sin estar completamente saldado:** El campo `added` solo indica que se registró el movimiento en el saldo de la cuenta, independientemente de si está completamente pagado o no
- `added` se puede actualizar manualmente o automáticamente cuando se completa el saldo mediante SettlementAllocation
- Cuando `added` pasa a `true`, se debe establecer `addedDate` con la fecha actual
- El campo `amount` siempre es positivo; el signo (ingreso/egreso) lo determina si es AccountIncome (positivo) o AccountExpense (negativo)
- El campo `yearMonth` representa el mes devengado (ej: cuota de Diciembre), aunque se pague en Enero

---

### 2. **AccountIncomeEntity** (IngresoCuenta)
**Extiende:** AbstractAccountMovementEntity
**Estrategia:** JOINED (tabla `account_incomes`)

**Atributos adicionales:**
- `id` (Long) - PK (heredado, FK a tabla padre)
- `incomeType` (ManyToOne → IncomeTypeEntity, nullable) - Tipo de ingreso

**Unicidad:**
- `(account + yearMonth + incomeType)` - **Solo aplica cuando `incomeType.monthlyRecurrence = true`**
- Si `monthlyRecurrence = false`, pueden existir múltiples movimientos del mismo tipo para la misma cuenta en el mismo período

**Validaciones:**
- Si `incomeType` tiene `monthlyRecurrence = true`, el sistema automáticamente creará nuevos movimientos mes a mes
- La unicidad `(account + yearMonth + incomeType)` solo se valida cuando `monthlyRecurrence = true`

**Relaciones:**
- ManyToOne con IncomeTypeEntity
- OneToMany con SettlementAllocationEntity (mappedBy = "accountMovement")

**Notas:**
- Relación con IncomeType para identificar tipos de ingresos mensuales recurrentes
- La automatización mensual verificará si `currentInstallment != finalInstallment` antes de crear la siguiente cuota
- El `amount` es positivo (representa un ingreso)
- **Cuotas:** Si un movimiento tiene cuotas (`currentInstallment` y `finalInstallment`), NO puede haber múltiples movimientos del mismo tipo para la misma cuenta en el mismo período (es mes a mes)
- Si `monthlyRecurrence = false`, pueden existir múltiples movimientos del mismo tipo en el mismo período, pero sin cuotas

---

### 3. **AbstractAccountExpenseEntity** (GastoCuenta)
**Extiende:** AbstractAccountMovementEntity
**Estrategia:** JOINED (tabla `account_expenses` - abstracta, no se instancia)
**Tipo:** Clase abstracta

**Atributos adicionales:**
- `id` (Long) - PK (heredado, FK a tabla padre)
- (Sin atributos adicionales, solo estructura para herencia)

**Notas:**
- Clase intermedia para agrupar MonthlyExpense y WorkshopRepair
- El `amount` es positivo pero representa un egreso (se resta del saldo cuando se agrega)

---

### 4. **MonthlyExpenseEntity** (GastoMensual)
**Extiende:** AbstractAccountExpenseEntity
**Estrategia:** JOINED (tabla `monthly_expenses`)

**Atributos adicionales:**
- `id` (Long) - PK (heredado, FK a tabla padre)
- `expenseType` (ManyToOne → ExpenseTypeEntity, nullable) - Tipo de gasto mensual

**Unicidad:**
- `(account + yearMonth + expenseType)` - **Solo aplica cuando `expenseType.monthlyRecurrence = true`**
- Si `monthlyRecurrence = false`, pueden existir múltiples movimientos del mismo tipo para la misma cuenta en el mismo período

**Validaciones:**
- Si `expenseType` tiene `monthlyRecurrence = true`, el sistema automáticamente creará nuevos movimientos mes a mes
- La unicidad `(account + yearMonth + expenseType)` solo se valida cuando `monthlyRecurrence = true`

**Relaciones:**
- ManyToOne con ExpenseTypeEntity
- OneToMany con SettlementAllocationEntity (mappedBy = "accountMovement")

**Notas:**
- Relación con ExpenseType para identificar tipos de gastos mensuales recurrentes
- La automatización mensual verificará si `currentInstallment != finalInstallment` antes de crear la siguiente cuota
- El `amount` es positivo pero representa un egreso
- **Cuotas:** Si un movimiento tiene cuotas (`currentInstallment` y `finalInstallment`), NO puede haber múltiples movimientos del mismo tipo para la misma cuenta en el mismo período (es mes a mes)
- Si `monthlyRecurrence = false`, pueden existir múltiples movimientos del mismo tipo en el mismo período, pero sin cuotas

---

### 5. **WorkshopRepairEntity** (ArregloTaller)
**Extiende:** AbstractAccountExpenseEntity
**Estrategia:** JOINED (tabla `workshop_repairs`)

**Atributos adicionales:**
- `id` (Long) - PK (heredado, FK a tabla padre)
- `repairType` (Enum RepairType, nullable) - Tipo de arreglo
- `remainingBalance` (Double, nullable) - Saldo restante del arreglo

**Enum RepairType:**
- `WORKSHOP_REPAIR` - Gasto de taller
- `LUBRICATION_CENTER` - Lubricentro

**Validaciones:**
- `remainingBalance` >= 0 si no es null
- `remainingBalance` se actualiza automáticamente cuando se hacen pagos parciales, pero también puede actualizarse manualmente
- **NO puede tener cuotas:** `currentInstallment` y `finalInstallment` deben ser null (solo AccountIncome y MonthlyExpense pueden tener cuotas)

**Relaciones:**
- OneToMany con SettlementAllocationEntity (mappedBy = "accountMovement")

**Notas:**
- El `remainingBalance` representa lo que falta pagar de ESE arreglo específico
- Se actualiza cuando se asocia un pago mediante SettlementAllocation
- **remainingBalance derivado vs editable:**
  - **Derivado:** Se calcula automáticamente como `amount - suma_de_pagos` (no se guarda, se calcula al vuelo)
  - **Editable:** Se guarda en BD y puede modificarse manualmente, pero también se actualiza automáticamente
  - **Decisión:** Editable (se persiste) para permitir ajustes manuales y tener un valor fijo en BD

---

### 6. **SettlementAllocationEntity** (Tabla Puente - Asignación de Saldos)
**Propósito:** Manejar pagos parciales de movimientos de cuenta mediante diferentes métodos de pago

**Razón de Ser:**
Esta entidad es fundamental para el sistema porque permite:

1. **Pagos Parciales:** Un movimiento puede saldarse en múltiples partes (ej: un `WorkshopRepair` de $10,000 puede pagarse $3,000 con recibo, $4,000 con liquidación, $3,000 con movimiento de dinero)

2. **Múltiples Métodos de Pago:** Un mismo movimiento puede saldarse con diferentes métodos:
   - Recibo físico (`ReceiptEntity`)
   - Liquidación de sueldo (`PayrollSettlementEntity`)
   - Movimiento de dinero (`CashMovement` o `NonCashMovement`)

3. **Un Recibo Puede Saldar Múltiples Movimientos:** Un recibo puede cubrir varios movimientos (ej: un recibo de $15,000 puede saldar un `AccountIncome` de $5,000, un `MonthlyExpense` de $3,000 y un `WorkshopRepair` de $7,000)

4. **Control de Saldo Pendiente:** Calcula el saldo pendiente (`amount - SUM(allocatedAmount)`) y marca `added = true` cuando se completa el pago

5. **Actualización Automática:** Actualiza `remainingBalance` en `WorkshopRepair` cuando se crean o eliminan asignaciones

**Ejemplo Práctico:**
```
Movimiento: WorkshopRepair de $10,000 para vehículo ABC123

SettlementAllocation 1:
  - accountMovement: WorkshopRepair ($10,000)
  - receipt: Recibo #123
  - allocatedAmount: $3,000
  - allocationDate: 2024-01-15

SettlementAllocation 2:
  - accountMovement: WorkshopRepair ($10,000) [el mismo]
  - payrollSettlement: Liquidación Enero 2024
  - allocatedAmount: $4,000
  - allocationDate: 2024-01-20

SettlementAllocation 3:
  - accountMovement: WorkshopRepair ($10,000) [el mismo]
  - movementId: 456 (CashMovement)
  - allocatedAmount: $3,000
  - allocationDate: 2024-01-25

Resultado:
- Saldo pendiente: $10,000 - ($3,000 + $4,000 + $3,000) = $0
- added = true
- addedDate = 2024-01-25
- remainingBalance = $0
```

**Atributos:**
- `id` (Long) - PK
- `accountMovement` (ManyToOne → AbstractAccountMovementEntity, nullable) - Movimiento que se está saldando
- `receipt` (ManyToOne → ReceiptEntity, nullable) - Recibo que salda (parcial o total)
- `payrollSettlement` (ManyToOne → PayrollSettlementEntity, nullable) - Liquidación que salda (parcial o total)
- `movementId` (Long, nullable) - ID del CashMovement o NonCashMovement que salda (parcial o total)
- `allocatedAmount` (Double) - Monto asignado de este pago específico
- `allocationDate` (LocalDate) - Fecha de asignación
- `note` (String, nullable) - Nota sobre esta asignación
- `active` (Boolean, default true) - Soft delete

**Validaciones:**
- **XOR (Exclusivo):** Solo uno de los tres métodos de pago puede estar presente (receipt, payrollSettlement o movementId) - exactamente uno, no pueden estar todos null ni todos presentes
- `allocatedAmount` > 0
- `allocatedAmount` <= `accountMovement.amount - suma_de_otras_asignaciones`
- **Validación de movementId:** Cuando se carga un movimiento de dinero (`CashMovement` o `NonCashMovement`), se dará la opción de marcarlo como para pagar un movimiento de cuenta, asegurando que sea válido
- **Nota sobre cuentas:** Si un movimiento de dinero tiene una cuenta asociada, NO tendrá un `SettlementAllocation` (son casos diferentes - el movimiento afecta directamente el saldo de la cuenta)

**Relaciones:**
- ManyToOne con AbstractAccountMovementEntity
- ManyToOne con ReceiptEntity (0..1 por SettlementAllocation, pero un Receipt puede tener 0..* SettlementAllocation)
  - **Sin cascade:** La relación NO debe tener cascade porque el SettlementAllocation debe ser inmutable cuando tiene recibo
- ManyToOne con PayrollSettlementEntity (0..1 por SettlementAllocation, pero un PayrollSettlement puede tener 0..* SettlementAllocation)
  - **Sin cascade:** La relación NO debe tener cascade porque el SettlementAllocation debe ser inmutable cuando tiene liquidación
- **Relación con Movement:** Campo `movementId` (Long) - referencia a CashMovement o NonCashMovement (0..1 por SettlementAllocation, pero un Movement puede tener 0..* SettlementAllocation)
  - **Cascade DELETE:** Si se elimina el movimiento de dinero, el SettlementAllocation asociado también debe eliminarse (soft delete)

**Índices:**
- `(id_account_movement)` - Para búsquedas por movimiento
- `(id_receipt)` - Para búsquedas por recibo
- `(id_payroll_settlement)` - Para búsquedas por liquidación
- `(movement_id)` - Para búsquedas por movimiento de dinero

**Reglas de Inmutabilidad y Eliminación:**
- **Si tiene Recibo o Liquidación asignada:**
  - El `SettlementAllocation` es **INMUTABLE** (no se puede modificar ni eliminar)
  - Especialmente el campo `allocatedAmount` NO puede cambiarse
  - No se puede hacer soft delete porque el recibo/liquidación ya tiene en cuenta ese movimiento para su monto
  - **Razón:** Los recibos y liquidaciones son documentos oficiales que no pueden modificarse retroactivamente
  
- **Si tiene Movimiento de Dinero asignado (`movementId`):**
  - El `SettlementAllocation` puede modificarse y eliminarse
  - **Cascade DELETE:** Si se elimina el movimiento de dinero (soft delete), el `SettlementAllocation` asociado también se elimina automáticamente (soft delete)
  - **Razón:** Los movimientos de dinero son más "volátiles" y pueden corregirse, por lo que la asignación también puede corregirse

**Notas:**
- Permite que un movimiento se salde con múltiples métodos de pago (ej: mitad con recibo, mitad con cash movement)
- Cuando se crea una asignación, se actualiza `remainingBalance` en WorkshopRepair si aplica
- Cuando la suma de `allocatedAmount` alcanza el `amount` del movimiento, `added` pasa a `true` y `addedDate` se establece
- Un recibo puede tener múltiples SettlementAllocation (puede saldar varios movimientos)

---

### 7. **IncomeTypeEntity** (TipoIngreso)
**Tipo:** Entidad independiente

**Atributos:**
- `id` (Long) - PK
- `name` (String) - Nombre del tipo de ingreso (único)
- `monthlyRecurrence` (Boolean, default false) - Si es recurrente mensualmente
- `active` (Boolean, default true) - Soft delete

**Validaciones:**
- `name` único (constraint de unicidad)
- `name` no vacío (NotBlank)
- `name` entre 3 y 100 caracteres

**Relaciones:**
- OneToMany con AccountIncomeEntity (mappedBy = "incomeType")

**Notas:**
- El soft delete (`active = false`) solo afecta que el tipo no sea más elegible para nuevos movimientos
- Los movimientos ya creados conservan su relación con el tipo, incluso si se marca como inactivo
- No se puede eliminar físicamente si tiene movimientos asociados (validación en servicio)

---

### 8. **ExpenseTypeEntity** (TipoGasto)
**Tipo:** Entidad independiente

**Atributos:**
- `id` (Long) - PK
- `name` (String) - Nombre del tipo de gasto (único)
- `monthlyRecurrence` (Boolean, default false) - Si es recurrente mensualmente
- `active` (Boolean, default true) - Soft delete

**Validaciones:**
- `name` único (constraint de unicidad)
- `name` no vacío (NotBlank)
- `name` entre 3 y 100 caracteres

**Relaciones:**
- OneToMany con MonthlyExpenseEntity (mappedBy = "expenseType")

**Notas:**
- El soft delete (`active = false`) solo afecta que el tipo no sea más elegible para nuevos movimientos
- Los movimientos ya creados conservan su relación con el tipo, incluso si se marca como inactivo
- No se puede eliminar físicamente si tiene movimientos asociados (validación en servicio)

---

## 🔗 Relaciones con Entidades Existentes

### ReceiptEntity
- **Relación:** ManyToOne desde SettlementAllocationEntity (a través de tabla puente)
- **Cardinalidad:** Un recibo puede tener 0..* SettlementAllocation (puede saldar varios movimientos)
- **Propósito:** Para saldar movimientos cuando se emite un recibo
- **Efecto:** Al crear SettlementAllocation con un recibo, se actualiza el saldo del movimiento
- **Inmutabilidad:** Los SettlementAllocation con recibo asignado son INMUTABLES (no se pueden modificar ni eliminar)
- **Razón:** Los recibos son documentos oficiales que no pueden modificarse retroactivamente

### PayrollSettlementEntity
- **Relación:** ManyToOne desde SettlementAllocationEntity únicamente
- **Cardinalidad:** 
  - Un movimiento puede tener 0..* SettlementAllocation con payrollSettlement (para pagos parciales o totales)
  - Una liquidación puede tener 0..* SettlementAllocation (puede saldar varios movimientos)
- **Propósito:** Para saldar movimientos cuando se paga una liquidación
- **Efecto:** Al crear SettlementAllocation con una liquidación, se actualiza el saldo del movimiento
- **Inmutabilidad:** Los SettlementAllocation con liquidación asignada son INMUTABLES (no se pueden modificar ni eliminar)
- **Razón:** Las liquidaciones son documentos oficiales que no pueden modificarse retroactivamente
- **Nota:** Todos los pagos con liquidación se manejan a través de SettlementAllocationEntity, no hay relación directa con AbstractAccountMovementEntity

### CashMovement / NonCashMovement (AbstractMovementEntity)
- **Relación:** Campo `movementId` (Long, nullable) en SettlementAllocationEntity
- **Cardinalidad:** 0-1 a 0-1 (un movimiento de dinero puede tener 0..* SettlementAllocation)
- **Propósito:** Para saldar movimientos mediante movimientos de dinero
- **Efecto:** Al crear SettlementAllocation con un movementId, se actualiza el saldo del movimiento
- **Cascade DELETE:** Si se elimina un movimiento de dinero (soft delete), los SettlementAllocation asociados también se eliminan automáticamente (soft delete)
- **Modificable:** Los SettlementAllocation con movimiento de dinero pueden modificarse y eliminarse (a diferencia de los que tienen recibo/liquidación)
- **Razón:** Los movimientos de dinero son más "volátiles" y pueden corregirse
- **Nota:** No hay relación JPA directa porque AbstractMovementEntity es @MappedSuperclass. Se usa el ID para la referencia. La eliminación en cascada debe implementarse manualmente en el servicio de movimientos.

---

## 🔄 Automatización Mensual

**Servicio:** `AccountMovementSchedulerService`

**Funcionalidad:**
- Se ejecuta el día 1 de cada mes a las 00:00 (cron: `"0 0 0 1 * ?"`)
- Busca todos los `IncomeType` y `ExpenseType` con `monthlyRecurrence = true`
- Para cada tipo encontrado:
  - Busca movimientos del mes anterior con ese tipo
  - Crea nuevos movimientos para el mes actual con los mismos datos
  - **Validación de cuotas:** Si el movimiento tiene `currentInstallment` y `finalInstallment`:
    - Solo crea el nuevo movimiento si `currentInstallment < finalInstallment`
    - Si `currentInstallment == finalInstallment`, NO crea el movimiento (ya terminó de pagarse)
    - Incrementa `currentInstallment` en el nuevo movimiento
  - Los nuevos movimientos tienen `added = false` por defecto

**Endpoints:**
- `POST /account-movements/generate-monthly-movements` - Ejecutar manualmente (para testing)

---

## 📊 Estructura de Tablas (JOINED Strategy)

```
account_movements (tabla padre)
├── id_account_movement (PK)
├── id_member_account (FK, nullable)
├── id_subscriber_account (FK, nullable)
├── id_vehicle_account (FK, nullable)
├── amount (siempre positivo)
├── period (yearMonth) - Mes devengado
├── added
├── added_date (nullable)
├── note (nullable)
├── current_installment (nullable)
├── final_installment (nullable)
└── active

account_incomes (tabla hija)
├── id_account_income (PK, FK a account_movements)
└── id_income_type (FK, nullable)

account_expenses (tabla intermedia abstracta)
└── id_account_expense (PK, FK a account_movements)

monthly_expenses (tabla hija)
├── id_monthly_expense (PK, FK a account_expenses)
└── id_expense_type (FK, nullable)

workshop_repairs (tabla hija)
├── id_workshop_repair (PK, FK a account_expenses)
├── repair_type (enum)
└── remaining_balance (nullable) - Editable y persistido

settlement_allocations (tabla puente)
├── id_settlement_allocation (PK)
├── id_account_movement (FK, nullable)
├── id_receipt (FK, nullable)
├── id_payroll_settlement (FK, nullable)
├── movement_id (Long, nullable)
├── allocated_amount
├── allocation_date
├── note (nullable)
└── active

income_types
├── id_income_type (PK)
├── name (unique)
├── monthly_recurrence
└── active

expense_types
├── id_expense_type (PK)
├── name (unique)
├── monthly_recurrence
└── active
```

---

## 💡 Lógica de Negocio

### Cálculo de Saldo Pendiente
- **Saldo pendiente** = `accountMovement.amount - SUM(settlementAllocations.allocatedAmount WHERE active = true)`
- Cuando `saldo pendiente = 0`, el movimiento se marca como `added = true` y `addedDate = fecha actual`

### Actualización de remainingBalance en WorkshopRepair
- **Inicial:** `remainingBalance = amount` (al crear el WorkshopRepair)
- **Al crear SettlementAllocation:** `remainingBalance = remainingBalance - allocatedAmount`
- **Al eliminar SettlementAllocation (soft delete):** `remainingBalance = remainingBalance + allocatedAmount` (reversión)
- **Manual:** Puede actualizarse directamente si es necesario

### Eliminación y Modificación de SettlementAllocation

#### Caso 1: SettlementAllocation con Recibo o Liquidación
- **INMUTABLE:** No se puede modificar ni eliminar (ni soft delete ni físico)
- **Razón:** El recibo/liquidación es un documento oficial que ya tiene en cuenta ese movimiento para su monto
- **Validación:** Al intentar modificar o eliminar, lanzar excepción `InvalidDataException` con mensaje apropiado
- **Campos protegidos:** Especialmente `allocatedAmount` no puede cambiarse

#### Caso 2: SettlementAllocation con Movimiento de Dinero
- **Modificable:** Puede editarse (excepto `accountMovement` una vez creado)
- **Eliminable:** Puede hacerse soft delete (`active = false`)
- **Cascade DELETE:** Si se elimina el movimiento de dinero (soft delete), el `SettlementAllocation` asociado también se elimina automáticamente (soft delete)
- **Revertir efectos al eliminar:**
  - Si el movimiento es `WorkshopRepair`: revertir `remainingBalance` sumando el `allocatedAmount`
  - Recalcular `added` del movimiento: si `SUM(allocatedAmount WHERE active = true) < amount`, entonces `added = false` y `addedDate = null`
  - **NO revertir el saldo de la cuenta:** El saldo de la cuenta ya fue afectado cuando se creó el movimiento de dinero, y eso debe mantenerse para auditoría
- **Razón:** Los movimientos de dinero son más "volátiles" y pueden corregirse, por lo que la asignación también puede corregirse

#### Implementación Técnica
- **Validación en Service:** Antes de modificar/eliminar, verificar si tiene `receipt` o `payrollSettlement` asignado
- **Cascade en Movement Service:** Al eliminar un `CashMovement` o `NonCashMovement`, buscar `SettlementAllocation` asociados por `movementId` y hacerles soft delete
- **Auditoría:** Mantener todas las asignaciones (activas e inactivas) permite rastrear el historial completo de pagos

### Validación de Cuenta
- Al crear un SettlementAllocation, se debe validar que:
  - Si es Receipt: la cuenta del recibo coincide con la cuenta del movimiento
  - Si es PayrollSettlement: la cuenta de la liquidación coincide con la cuenta del movimiento
  - Si es Movement (`movementId`): 
    - El movimiento de dinero debe existir y ser válido (CashMovement o NonCashMovement)
    - La cuenta del movimiento de dinero coincide con la cuenta del movimiento
    - **Nota:** Si el movimiento de dinero tiene cuenta asociada, NO debería tener SettlementAllocation (son casos diferentes)

### Implementación de Cascade Delete para Movimientos de Dinero
**En CashMovementService y NonCashMovementService:**
- Al eliminar un movimiento de dinero (soft delete), buscar todos los `SettlementAllocation` activos que tengan `movementId = movimiento.id`
- Para cada `SettlementAllocation` encontrado:
  1. Hacer soft delete del `SettlementAllocation` (`active = false`)
  2. Si el movimiento asociado es `WorkshopRepair`: revertir `remainingBalance` sumando el `allocatedAmount`
  3. Recalcular `added` del movimiento: si `SUM(allocatedAmount WHERE active = true) < amount`, entonces `added = false` y `addedDate = null`
- **No revertir el saldo de la cuenta:** El saldo ya fue afectado y debe mantenerse para auditoría

---


