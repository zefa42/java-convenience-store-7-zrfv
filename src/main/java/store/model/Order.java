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
            int remainingQuantity = reduceProductStock(productName, purchasedQuantity, true);
            if (remainingQuantity > 0) {
                remainingQuantity = reduceProductStock(productName, remainingQuantity, false);
                if (remainingQuantity > 0) {
                    throw new IllegalArgumentException(INVALID_QUANTITY);
                }
            }
        }
    }

    private int reduceProductStock(String productName, int quantity, boolean isPromotion) {
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

        return remainingQuantity;
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

        int nonPromotionalAmount = 0;
        for (Purchase purchase : purchases) {
            String productName = purchase.getProductName();
            int price = store.getProductPriceByName(productName);
            boolean isPromotionalProduct = store.isProductPromotional(productName);
            if (!isPromotionalProduct) {
                nonPromotionalAmount += price * purchase.getPurchasedQuantity();
            }
        }
        return Math.min((int) (nonPromotionalAmount * 0.3), 8000);
    }


    public String getOrderSummary(int totalAmount, int promotionDiscount, int membershipDiscount) {
        StringBuilder summary = new StringBuilder();
        summary.append("==============W 편의점================\n");
        summary.append("상품명\t\t\t\t수량\t\t\t금액\n");
        for (Purchase purchase : purchases) {
            String productName = purchase.getProductName();
            int purchasedQuantity = purchase.getPurchasedQuantity();
            int price = store.getProductPriceByName(productName);
            summary.append(String.format("%s\t\t\t\t%d\t\t\t%,d\n", productName, purchasedQuantity, price * purchasedQuantity));
        }
        summary.append("=============증\t정===============\n");
        for (Purchase purchase : purchases) {
            int freeQuantity = purchase.getFreeQuantity();
            if (freeQuantity > 0) {
                String productName = purchase.getProductName();
                summary.append(String.format("%s\t\t\t\t%d\n", productName, freeQuantity));
            }
        }
        summary.append("====================================\n");
        int finalAmount = totalAmount - promotionDiscount - membershipDiscount;
        summary.append(String.format("총구매액\t\t\t\t%d\t\t\t%,d\n", getTotalQuantity(), totalAmount));
        summary.append(String.format("행사할인\t\t\t\t\t\t\t-%,d\n", promotionDiscount));
        summary.append(String.format("멤버십할인\t\t\t\t\t\t-%,d\n", membershipDiscount));
        summary.append(String.format("내실돈\t\t\t\t\t\t\t%,d\n", finalAmount));
        return summary.toString();
    }

    public int getTotalQuantity() {
        int totalQuantity = 0;
        for (Purchase purchase : purchases) {
            totalQuantity += purchase.getPurchasedQuantity();
        }
        return totalQuantity;
    }
}
