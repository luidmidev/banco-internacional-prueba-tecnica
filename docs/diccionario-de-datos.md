# Diccionario de Datos – Tabla `customers`

## Tabla: `customers`

Representa la información básica del cliente y su cuenta asociada.

### Campos

| Campo                 | Tipo SQL     | Tipo Lógico / Java | Descripción                                     | Restricciones    |
|-----------------------|--------------|--------------------|-------------------------------------------------|------------------|
| id                    | UUID         | UUID               | Identificador único del cliente.                | PK, NOT NULL     |
| name                  | VARCHAR(255) | String             | Nombre completo del cliente.                    | NOT NULL         |
| number                | VARCHAR(255) | String             | Número interno del cliente, generado y cifrado. | NOT NULL         |
| identification_number | VARCHAR(15)  | String             | Número del documento de identificación.         | NOT NULL         |
| identification_type   | SMALLINT     | IdentificationType | Tipo de documento del cliente.                  | NOT NULL         |
| account_number        | VARCHAR(20)  | String             | Número de cuenta bancaria.                      | NOT NULL, UNIQUE |
| account_type          | SMALLINT     | AccountType        | Tipo de cuenta bancaria.                        | NOT NULL         |
| account_balance       | DECIMAL      | BigDecimal         | Saldo actual de la cuenta.                      | NOT NULL         |
| account_status        | SMALLINT     | AccountStatus      | Estado actual de la cuenta.                     | NOT NULL         |

---

## Catálogos de Enumeraciones

### AccountStatus (account_status)

| Valor | Enum      | Descripción |
|-------|-----------|-------------|
| 0     | ACTIVE    | activa      |
| 1     | INACTIVE  | inactiva    |
| 2     | SUSPENDED | suspendida  |
| 3     | CLOSED    | cerrada     |

### AccountType (account_type)

| Valor | Enum     | Descripción       |
|-------|----------|-------------------|
| 0     | SAVINGS  | Cuenta de ahorros |
| 1     | CHECKING | Cuenta corriente  |

### IdentificationType (identification_type)

| Valor | Enum     | Descripción                      |
|-------|----------|----------------------------------|
| 0     | RUC      | Registro Único de Contribuyentes |
| 1     | CI       | Cédula de Identidad              |
| 2     | PASSPORT | Pasaporte                        |

---

## Secuencias

### customer_number_seq

Secuencia utilizada para generar números internos únicos para los clientes antes de cifrarlos.

- Inicio: 1
- Incremento: 1