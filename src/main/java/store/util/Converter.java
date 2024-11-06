package store.util;

import java.time.LocalDate;

public class Converter {
    public static int stringToInt(String input) {
        return Integer.parseInt(input);
    }

    public static LocalDate stringToDate(String input) {
        return LocalDate.parse(input);
    }
}