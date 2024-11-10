package store.model;

import static store.view.validator.InputValidator.PRODUCT_FORMAT_ERROR_MESSAGE;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.util.Converter;
import store.util.Splitter;
import store.view.InputView;

public class Order {
    private static final Pattern PURCHASE_PATTERN = Pattern.compile("\\[(\\S+)-(\\d+)]");
    private static final String INVALID_PRODUCT_NAME = "[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.";
    private static final String INVALID_QUANTITY = "[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.";
    private static final String NOT_POSITIVE_QUANTITY = "[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.";

    private final String inputPurchase;
    private final Store store;
    private final List<Purchase> purchases = new ArrayList<>();

    public Order(String inputPurchase, Store store) {
        this.inputPurchase = inputPurchase;
        this.store = store;
        validatePurchaseFormat(inputPurchase);
        parsePurchases(inputPurchase);
        validateContainName();
        validateQuantity();
    }

    private void parsePurchases(String input) {
        String[] items = Splitter.split(input);
        for (String item : items) {
            Matcher matcher = PURCHASE_PATTERN.matcher(item);
            if (matcher.matches()) {
                String productName = matcher.group(1);
                int quantity = Converter.stringToInt(matcher.group(2));
                purchases.add(new Purchase(productName, quantity));
            }
        }
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

    public void validateContainName() {
        for (Purchase purchase : purchases) {
            if (!ProductName.getNames().contains(purchase.getProductName())) {
                throw new IllegalArgumentException(INVALID_PRODUCT_NAME);
            }
        }
    }

    public void validateQuantity() {
        for (Purchase purchase : purchases) {
            validateProductQuantity(purchase);
        }
    }

    private void validateProductQuantity(Purchase purchase) {
        String productName = purchase.getProductName();
        int requestedQuantity = purchase.getQuantity();
        validateZeroQuantity(requestedQuantity);

        int availableQuantity = store.getTotalAvailableQuantity(productName);

        if (requestedQuantity > availableQuantity) {
            throw new IllegalArgumentException(INVALID_QUANTITY);
        }
    }

    private void validateZeroQuantity(int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException(NOT_POSITIVE_QUANTITY);
        }
    }

    public void adjustPurchasesForPromotion(InputView inputView) {
        for (Purchase purchase : purchases) {
            String productName = purchase.getProductName();
            int requestedQuantity = purchase.getPurchasedQuantity();

            List<Product> promotionalProducts = store.getPromotionalProductsByName(productName);
            Optional<Promotion> optionalPromotion = Optional.empty();

            if (!promotionalProducts.isEmpty()) {
                String promotionName = promotionalProducts.get(0).getPromotion();
                optionalPromotion = Promotion.getPromotionByName(promotionName);
            }

            if (optionalPromotion.isPresent()) {
                Promotion promotion = optionalPromotion.get();
                int promotionBuy = promotion.getBuy();
                int promotionGet = promotion.getGet();

                int maxPromotionApplications = store.getMaxPromotionApplications(productName, promotion);

                int possiblePromotionApplications = requestedQuantity / promotionBuy;
                int remainder = requestedQuantity % promotionBuy;

                if (requestedQuantity == promotionBuy) {
                    System.out.printf("현재 %s은(는) %d개를 무료로 받으실 수 있습니다. 추가하시겠습니까? (Y/N)\n", productName, promotionGet);
                    String response = inputView.inputYesOrNo();
                    if (response.equalsIgnoreCase("Y")) {
                        purchase.setFreeQuantity(promotionGet);
                    }
                } else if (possiblePromotionApplications > 0) {
                    int actualPromotionApplications = Math.min(possiblePromotionApplications, maxPromotionApplications);
                    int freeQuantity = actualPromotionApplications * promotionGet;
                    purchase.setFreeQuantity(freeQuantity);
                }
            }
        }
    }

    public void reduceStock() {
        for (Purchase purchase : purchases) {
            String productName = purchase.getProductName();
            int purchasedQuantity = purchase.getPurchasedQuantity();
            int freeQuantity = purchase.getFreeQuantity();

            reduceProductStock(productName, purchasedQuantity, false);
            if (freeQuantity > 0) {
                boolean promotionStockAvailable = reduceProductStock(productName, freeQuantity, true);
                if (!promotionStockAvailable) {
                    System.out.printf("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)\n", productName, purchasedQuantity);
                    // 프로모션 적용 x 에 대한 기능 구현
                    purchase.setFreeQuantity(0);
                }
            }
        }
    }

    private boolean reduceProductStock(String productName, int quantity, boolean isPromotion) {
        List<Product> products = isPromotion ?
                store.getPromotionalProductsByName(productName) :
                store.getRegularProductsByName(productName);

        int remainingQuantity = quantity;
        for (Product product : products) {
            if (remainingQuantity > 0) {
                int quantityToReduce = Math.min(remainingQuantity, product.getQuantity());
                product.reduceQuantity(quantityToReduce);
                remainingQuantity -= quantityToReduce;
            }
        }

        return remainingQuantity == 0;
    }

    public int calculateTotalAmount() {
        int totalAmount = 0;
        for (Purchase purchase : purchases) {
            String productName = purchase.getProductName();
            int purchasedQuantity = purchase.getPurchasedQuantity();
            int price = store.getProductPriceByName(productName);
            totalAmount += price * purchasedQuantity;
        }
        return totalAmount;
    }

    public int calculatePromotionDiscount() {
        int discount = 0;
        for (Purchase purchase : purchases) {
            String productName = purchase.getProductName();
            int freeQuantity = purchase.getFreeQuantity();
            int price = store.getProductPriceByName(productName);
            discount += price * freeQuantity;
        }
        return discount;
    }

    public int calculateMembershipDiscount(boolean isMember) {
        if (!isMember) return 0;

        int totalAmount = calculateTotalAmount();
        int promotionDiscount = calculatePromotionDiscount();
        int nonPromotionalAmount = totalAmount - promotionDiscount;

        int membershipDiscount = (int) (nonPromotionalAmount * 0.3);
        return Math.min(membershipDiscount, 8000);
    }
}
