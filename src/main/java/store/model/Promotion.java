package store.model;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import store.util.Converter;
import store.util.Splitter;

public class Promotion {
    private static final String FILE_PATH = "src/main/resources/promotions.md";

    private final String name;
    private final int buy;
    private final int get;
    private final LocalDate start_date;
    private final LocalDate end_date;

    Promotion(String name, int buy, int get, LocalDate start_date, LocalDate end_date) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public static List<Promotion> init(List<String> rawPromotion) {
        List<Promotion> promotions = new ArrayList<>();
        for (String line : rawPromotion) {
            String[] parts = Splitter.split(line);
            promotions.add(new Promotion(parts[0],
                    Converter.stringToInt(parts[1]),
                    Converter.stringToInt(parts[2]),
                    Converter.stringToDate(parts[3]),
                    Converter.stringToDate(parts[4])));
        }
        return promotions;
    }

    public String getName() {
        return name;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }
}
