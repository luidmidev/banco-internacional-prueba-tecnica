package ec.com.bancointernacional.pruebatecnica.utils;

import lombok.experimental.UtilityClass;

import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class EcuadorIdGenerator {

    public static String generateCedula() {
        var random = ThreadLocalRandom.current();

        var provincia = random.nextInt(1, 25); // 01 - 24
        var tercerDigito = random.nextInt(0, 6); // 0-5 personas naturales

        var base = String.format("%02d%d", provincia, tercerDigito);

        // generar los siguientes 6 dígitos
        var sb = new StringBuilder(base);
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(0, 10));
        }

        // calcular dígito verificador
        var verificador = calcularDigitoVerificador(sb.toString());
        sb.append(verificador);

        return sb.toString();
    }

    private static int calcularDigitoVerificador(String nineDigits) {
        var suma = 0;
        for (int i = 0; i < nineDigits.length(); i++) {
            int dig = nineDigits.charAt(i) - '0';

            // posiciones impares (0,2,4,6,8)
            if (i % 2 == 0) {
                dig = dig * 2;
                if (dig > 9) dig -= 9;
            }

            suma += dig;
        }

        var decena = ((suma + 9) / 10) * 10;
        var verificador = decena - suma;

        return verificador == 10 ? 0 : verificador;
    }
}
