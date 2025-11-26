package ec.com.bancointernacional.pruebatecnica.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "customer.number.cipher")
public class CustomerNumberCipherProperties {

    /**
     * Clave utilizada para el cifrado y descifrado del número de cliente.
     */
    private String key;

    /**
     * Algoritmo de cifrado utilizado, ejemplo: "AES".
     */
    private String algorithm;
}
