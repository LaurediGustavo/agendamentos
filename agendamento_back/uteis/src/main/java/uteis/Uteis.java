package uteis;

import java.util.List;

public class Uteis {

    public static boolean isNotNullOrNotEmpty(List<?> lista) {
        if(lista != null && !lista.isEmpty()) {
            return true;
        }

        return false;
    }

}
