package ec.com.bancointernacional.pruebatecnica.entities;

import ec.com.bancointernacional.pruebatecnica.entities.converters.CustomerNumberCipherConverter;
import ec.com.bancointernacional.pruebatecnica.schemas.IdentificationType;
import io.github.luidmidev.jakarta.validations.Validations;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

import java.util.UUID;

/**
 * Entidad que representa a un cliente dentro del sistema.
 *
 * <p>
 * Cada cliente tiene un identificador único, un nombre, un número cifrado
 * y una cuenta asociada. La entidad utiliza un {@link Converter} para cifrar
 * y descifrar automáticamente el número del cliente al persistirlo.
 * </p>
 *
 * <p>
 * Se implementa {@link Persistable} para permitir a Spring Data determinar si
 * la entidad es nueva basándose en si el identificador es nulo.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
        name = "customers",
        indexes = {
                @Index(name = "idx_account_number", columnList = "account_number", unique = true)
        }
)
public class Customer {

    /**
     * Identificador único del cliente.
     *
     * <p>
     * Se genera automáticamente como un UUID mediante
     * {@link GenerationType#UUID}. No se asigna manualmente.
     * </p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Nombre completo del cliente.
     *
     * <p>
     * No puede ser nulo y se almacena en la columna {@code name}.
     * </p>
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Número del cliente, almacenado de forma segura mediante cifrado.
     *
     * <p>
     * Antes de persistir los datos, este campo se cifra usando
     * {@link CustomerNumberCipherConverter}. Al cargarse desde la base de datos,
     * se descifra automáticamente.
     * </p>
     *
     * <p>
     * No puede ser nulo y se almacena en la columna {@code number}.
     * </p>
     */
    @Convert(converter = CustomerNumberCipherConverter.class)
    @Column(name = "number", nullable = false)
    private String number;

    /**
     * Número de identificación del cliente (por ejemplo, cédula, pasaporte).
     */
    @Column(name = "identification_number", length = 15, nullable = false)
    private String identificationNumber;


    /**
     * Tipo de identificación del cliente.
     */
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "identification_type", nullable = false)
    private IdentificationType identificationType;

    /**
     * Información de la cuenta bancaria asociada al cliente.
     *
     * <p>
     * Este campo utiliza {@link Embedded} para almacenar los datos
     * directamente en la tabla del cliente. Representa un objeto de valor
     * con número de cuenta, tipo, saldo y estado.
     * </p>
     */
    @Embedded
    private Account account;

    @PrePersist
    @PreUpdate
    private void validate() {
        switch (identificationType) {
            case CI -> {
                if (Validations.isValidCi(identificationNumber)) {
                    return;
                }
                throw new IllegalArgumentException("El número de cédula de identidad no es válido.");
            }
            case RUC -> {
                var ciPart = identificationNumber.substring(0, identificationNumber.length() - 3);
                var rucPart = identificationNumber.substring(identificationNumber.length() - 3);
                if (Validations.isValidCi(ciPart) && rucPart.equals("001")) {
                    return;
                }
                throw new IllegalArgumentException("El número de RUC no es válido.");
            }
            case PASSPORT -> {
                // No se realizan validaciones específicas para pasaportes por ahora
            }
            case null -> throw new IllegalArgumentException("Tipo de identificación no puede ser nulo.");
        }
    }
}
