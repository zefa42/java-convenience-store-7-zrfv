package store.util;

public class Splitter {
    private static final String DELIMITER = ",";

    public static String[] split(final String input) {
        return input.split(DELIMITER);
    }
}