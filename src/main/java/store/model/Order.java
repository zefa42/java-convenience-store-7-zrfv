package store.model;

import static store.view.validator.InputValidator.PRODUCT_FORMAT_ERROR_MESSAGE;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import store.util.Converter;
import store.util.Splitter;

public class Order {
    private static final Pattern PURCHASE_PATTERN = Pattern.compile("\\[(\\S+)-(\\d+)\\]");
    private static final String INVALID_PRODUCT_NAME = "[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.";
    private static final String INVALID_QUANTITY = "[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.";

    private final String inputPurchase;
    private final Store store;

    public Order(String inputPurchase, Store store) {
        validatePurchaseFormat(inputPurchase);
        validateContainName(inputPurchase);
        validateQuantity(inputPurchase, store);
        this.inputPurchase = inputPurchase;
        this.store = store;
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


    public void validateQuantity(String input, Store store) {
        Arrays.stream(Splitter.split(input))
                .map(PURCHASE_PATTERN::matcher)
                .filter(Matcher::matches)
                .forEach(matcher -> validateProductQuantity(matcher, store));
    }

    private void validateProductQuantity(Matcher matcher, Store store) {
        String productName = matcher.group(1);
        int requestedQuantity = Converter.stringToInt(matcher.group(2));

        List<Product> promotionalProducts = getPromotionalProducts(productName, store);
        List<Product> regularProducts = getRegularProducts(productName, store);

        int remainingQuantity = reduceStock(promotionalProducts, requestedQuantity);
        remainingQuantity = reduceStock(regularProducts, remainingQuantity);

        if (remainingQuantity > 0) {
            throw new IllegalArgumentException(INVALID_QUANTITY);
        }
    }

    private List<Product> getPromotionalProducts(String productName, Store store) {
        return store.getProducts().stream()
                .filter(p -> p.getName().equals(productName) && p.isPromotional())
                .collect(Collectors.toList());
    }

    private List<Product> getRegularProducts(String productName, Store store) {
        return store.getProducts().stream()
                .filter(p -> p.getName().equals(productName) && !p.isPromotional())
                .collect(Collectors.toList());
    }

    private int reduceStock(List<Product> products, int requestedQuantity) {
        int remainingQuantity = requestedQuantity;
        for (Product product : products) {
            if (remainingQuantity > 0) {
                int quantityToReduce = Math.min(remainingQuantity, product.getQuantity());
                product.reduceQuantity(quantityToReduce);
                remainingQuantity -= quantityToReduce;
            }
        }
        return remainingQuantity;
    }
}
