package store.util;

import java.time.LocalDate;

public class Converter {
    public static int stringToInt(final String input) {
        return Integer.parseInt(input);
    }

    public static LocalDate stringToDate(final String input) {
        return LocalDate.parse(input);
    }
}