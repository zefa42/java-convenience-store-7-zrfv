package store.model;

import static store.view.validator.InputValidator.PRODUCT_FORMAT_ERROR_MESSAGE;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.util.Converter;
import store.util.Splitter;

public class Order {
    private static final Pattern PURCHASE_PATTERN = Pattern.compile("\\[(\\S+)-\\d+\\]");
    private static final String INVALID_PRODUCT_NAME = "[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.";

    private final String inputPurchase;

    public Order(String inputPurchase) {
        validatePurchaseFormat(inputPurchase);
        validateContainName(inputPurchase);
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

    public void validateContainName(String input) {
        Arrays.stream(Splitter.split(input))
                .map(PURCHASE_PATTERN::matcher)
                .filter(matcher -> matcher.matches())
                .map(matcher -> matcher.group(1))
                .filter(productName -> !ProductName.getNames().contains(productName))
                .findFirst()
                .ifPresent(invalidName -> {
                    throw new IllegalArgumentException(INVALID_PRODUCT_NAME);
                });
    }
}