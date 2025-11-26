package ec.com.bancointernacional.pruebatecnica.entities;

import ec.com.bancointernacional.pruebatecnica.schemas.AccountStatus;
import ec.com.bancointernacional.pruebatecnica.schemas.AccountType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Representa los datos de una cuenta bancaria embebida dentro de otra entidad JPA.
 *
 * <p>
 * Al estar anotada con {@link Embeddable}, sus campos se almacenan directamente
 * en la fila de la entidad que la contiene. Incluye el número de cuenta, el tipo,
 * el saldo y el estado actual.
 * </p>
 *
 * <p>
 * Notas importantes:
 * <ul>
 *     <li>El número de cuenta debe ser único y no nulo.</li>
 *     <li>Los enums se almacenan como valores ordinales, por lo que cambiar el orden
 *     de los valores en el enum afectará los datos existentes.</li>
 * </ul>
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class Account {

    /**
     * Número de la cuenta bancaria.
     *
     * <p>
     * Se almacena en la columna {@code account_number}, no permite nulos y debe ser único.
     * </p>
     */
    @Column(name = "account_number", nullable = false, unique = true, length = 20)
    private String number;

    /**
     * Tipo de cuenta bancaria (ahorros, corriente, etc.).
     *
     * <p>
     * Se almacena como un valor ordinal usando {@link EnumType#ORDINAL}.
     * No permite nulos.
     * </p>
     */
    @Column(name = "account_type", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private AccountType type;

    /**
     * Saldo actual de la cuenta.
     *
     * <p>
     * Se almacena en la columna {@code account_balance} y no permite nulos.
     * </p>
     */
    @Column(name = "account_balance", nullable = false)
    private BigDecimal balance;

    /**
     * Estado actual de la cuenta (activa, bloqueada, cerrada, etc.).
     *
     * <p>
     * Se almacena como un valor ordinal usando {@link EnumType#ORDINAL}
     * y no permite nulos.
     * </p>
     */
    @Column(name = "account_status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private AccountStatus status;
}
