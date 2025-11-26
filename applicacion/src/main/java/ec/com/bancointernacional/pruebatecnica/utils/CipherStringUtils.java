package ec.com.bancointernacional.pruebatecnica.utils;

import lombok.experimental.UtilityClass;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.Base64;

/**
 * Utilidad para realizar operaciones de cifrado y descifrado simétrico de cadenas,
 * utilizando algoritmos soportados por {@link javax.crypto.Cipher}.
 * <p>
 * Esta clase está diseñada como una utility class y no puede ser instanciada.
 * Proporciona métodos para cifrar y descifrar texto usando una clave proporcionada
 * en forma de cadena. La clave es limpiada de forma segura al finalizar cada operación.
 * </p>
 *
 * <h2>Ejemplo de uso:</h2>
 *
 * <pre>{@code
 * String encrypted = CipherStringUtils.cipher("hola", "1234567890123456", "AES");
 * String plain = CipherStringUtils.decipher(encrypted, "1234567890123456", "AES");
 * }</pre>
 *
 * <p>
 * Nota: Es responsabilidad del usuario asegurar que el algoritmo y el tamaño de clave
 * sean compatibles. Por ejemplo, "AES" requiere claves de 16, 24 o 32 bytes.
 * </p>
 */
@UtilityClass
public final class CipherStringUtils {

    /**
     * Cifra el texto plano usando el algoritmo y clave especificados.
     *
     * @param plainText el texto sin cifrar que se desea proteger.
     * @param keyString la clave utilizada para el cifrado; se convierte en bytes y se limpia
     *                  de memoria al finalizar.
     * @param algorithm el nombre del algoritmo de cifrado (por ejemplo: "AES", "AES/ECB/PKCS5Padding").
     *
     * @return el texto cifrado codificado en Base64.
     *
     * @throws RuntimeException si ocurre cualquier error durante el proceso de cifrado.
     */
    public static String cipher(String plainText, String keyString, String algorithm) {
        var key = keyString.getBytes();
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, algorithm));
            return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Error al cifrar", e);
        } finally {
            Arrays.fill(key, (byte) 0);
        }
    }

    /**
     * Descifra un texto previamente cifrado con el algoritmo y clave especificados.
     *
     * @param cipherText el texto cifrado codificado en Base64.
     * @param keyString  la clave utilizada para descifrar; se convierte en bytes y se limpia
     *                   de memoria al finalizar.
     * @param algorithm  el nombre del algoritmo de cifrado (por ejemplo: "AES", "AES/ECB/PKCS5Padding").
     *
     * @return el texto descifrado en su forma original.
     *
     * @throws RuntimeException si ocurre cualquier error durante el proceso de descifrado,
     *                          incluyendo claves o algoritmos inválidos.
     */
    public static String decipher(String cipherText, String keyString, String algorithm) {
        var key = keyString.getBytes();
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, algorithm));
            return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
        } catch (Exception e) {
            throw new RuntimeException("Error al descifrar", e);
        } finally {
            Arrays.fill(key, (byte) 0);
        }
    }
}
