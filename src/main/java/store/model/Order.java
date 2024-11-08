package store.model;

import static store.view.validator.InputValidator.PRODUCT_FORMAT_ERROR_MESSAGE;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.util.Converter;
import store.util.Splitter;

public class Order {
    private static final Pattern PURCHASE_PATTERN = Pattern.compile("\\[[^-\\]]+-\\d+\\]");

    private final String inputPurchase;

    public Order(String inputPurchase) {
        validatePurchaseFormat(inputPurchase);
        this.inputPurchase = inputPurchase;
    }

    public void validatePurchaseFormat(String input) {
        String[] items = Splitter.split(input);
        for (String item : items) {
            Matcher matcher = PURCHASE_PATTERN.matcher(item);
            if (!matcher.matches()) {
                throw new IllegalArgumentException(PRODUCT_FORMAT_ERROR_MESSAGE);
            }
        }
    }
}
