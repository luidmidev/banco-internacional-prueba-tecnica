package ec.com.bancointernacional.pruebatecnica.entities.converters;

import ec.com.bancointernacional.pruebatecnica.configuration.CustomerNumberCipherProperties;
import ec.com.bancointernacional.pruebatecnica.utils.CipherStringUtils;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Convertidor JPA responsable de cifrar y descifrar automáticamente los
 * números de cliente (customerNumber) al almacenar y recuperar datos desde la base.
 * <p>
 * Este convertidor permite proteger datos sensibles mediante cifrado simétrico
 * utilizando los parámetros configurados en {@link CustomerNumberCipherProperties}.
 * El cifrado se ejecuta durante la conversión de atributo a columna, mientras que
 * el descifrado ocurre al convertir desde la columna hacia la entidad.
 * </p>
 *
 * <h2>Funcionamiento</h2>
 * <ul>
 *   <li><b>convertToDatabaseColumn</b>: Cifra el valor antes de guardarlo.</li>
 *   <li><b>convertToEntityAttribute</b>: Descifra el valor leído desde la base.</li>
 * </ul>
 *
 * <h2>Ejemplo de uso</h2>
 * <pre>{@code
 * @Column(name = "customer_number")
 * @Convert(converter = CustomerNumberCipherConverter.class)
 * private String customerNumber;
 * }</pre>
 *
 * <p>
 * La clave y el algoritmo deben ser válidos para {@link javax.crypto.Cipher},
 * por ejemplo: "AES", "AES/ECB/PKCS5Padding", etc.
 * </p>
 */
@Converter
public class CustomerNumberCipherConverter implements AttributeConverter<String, String> {

    private final String key;
    private final String algorithm;

    /**
     * Crea una nueva instancia del convertidor utilizando los parámetros
     * de cifrado configurados en la aplicación.
     *
     * @param properties objeto de propiedades que contiene la clave y el algoritmo
     *                   a utilizar para las operaciones criptográficas.
     */
    public CustomerNumberCipherConverter(CustomerNumberCipherProperties properties) {
        this.key = properties.getKey();
        this.algorithm = properties.getAlgorithm();
    }

    /**
     * Convierte el valor de la entidad hacia la forma en que debe almacenarse
     * en la base de datos, aplicando cifrado si el atributo no es nulo.
     *
     * @param attribute el valor original (texto plano) desde la entidad.
     * @return el valor cifrado para la columna, o {@code null} si el atributo es nulo.
     */
    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        return CipherStringUtils.cipher(attribute, key, algorithm);
    }

    /**
     * Convierte el valor almacenado en la base de datos hacia su forma legible
     * por la entidad, aplicando descifrado si el valor no es nulo.
     *
     * @param dbData el texto cifrado proveniente de la base.
     * @return el valor descifrado para la entidad, o {@code null} si el dato es nulo.
     */
    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return CipherStringUtils.decipher(dbData, key, algorithm);
    }
}
