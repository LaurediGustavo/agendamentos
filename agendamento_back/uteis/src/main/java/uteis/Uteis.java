package uteis;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Service
public class Uteis {

    public static boolean isNotNullOrNotEmpty(List<?> lista) {
        return lista != null && !lista.isEmpty();
    }

    public static boolean cpfValido(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11) {
            return false;
        }

        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int[] numeros = new int[11];
        for (int i = 0; i < 11; i++) {
            numeros[i] = Character.getNumericValue(cpf.charAt(i));
        }

        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += numeros[i] * (10 - i);
        }

        int primeiroDigito = 11 - (soma % 11);
        if (primeiroDigito >= 10) {
            primeiroDigito = 0;
        }

        if (primeiroDigito != numeros[9]) {
            return false;
        }

        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += numeros[i] * (11 - i);
        }

        int segundoDigito = 11 - (soma % 11);
        if (segundoDigito >= 10) {
            segundoDigito = 0;
        }

        return segundoDigito == numeros[10];
    }

    public static String formatarMoedaParaReal(BigDecimal valor) {
        Locale locale = new Locale.Builder().setLanguage("pt").setRegion("BR").build();

        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(locale);

        return formatoMoeda.format(valor);
    }

    public static boolean isValorNumerico(String mensagem) {
        try {
            Integer.parseInt(mensagem);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public static String removerCaracteresNaoNumericos(String value) {
        if (StringUtils.isNotBlank(value)) {
            value = value.replaceAll("[^0-9]", "");
        }

        return value;
    }

}
