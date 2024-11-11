package store.model;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import store.util.Converter;
import store.util.Splitter;

public class Promotion {
    public static final String PROMOTION_FILE_PATH = "src/main/resources/promotions.md";
    private static List<Promotion> promotions = new ArrayList<>();

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

    public static void init(final List<String> rawPromotion) {
        promotions = new ArrayList<>();
        for (String line : rawPromotion) {
            String[] parts = Splitter.split(line);
            promotions.add(new Promotion(parts[0],
                    Converter.stringToInt(parts[1]),
                    Converter.stringToInt(parts[2]),
                    Converter.stringToDate(parts[3]),
                    Converter.stringToDate(parts[4])));
        }
    }

    public static Optional<Promotion> getPromotionByName(final String promotionName) {
        LocalDate currentDate = LocalDate.from(DateTimes.now());
        return promotions.stream()
                .filter(promo -> promo.getName().equals(promotionName)
                        && (currentDate.isEqual(promo.getStart_date()) || currentDate.isAfter(promo.getStart_date()))
                        && (currentDate.isEqual(promo.getEnd_date()) || currentDate.isBefore(promo.getEnd_date())))
                .findFirst();
    }

    public static List<Promotion> getPromotions() {
        return promotions;
    }

    public String getName() {
        return name;
    }

    public int getBuy() {
        return buy;
    }

    public int getGet() {
        return get;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }
}
