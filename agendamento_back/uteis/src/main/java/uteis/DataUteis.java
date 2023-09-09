package uteis;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataUteis {

    public static LocalDateTime getLocalDateTime(String data) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.parse(data, formatter);
        } catch (Exception e) {
            throw e;
        }
    }

    public static String getLocalDateTime_ddMMaaaaHHMM(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        return localDateTime.format(formatter);
    }

}
