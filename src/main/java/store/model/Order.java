package store.model;

import static store.view.InputView.CONFIRM;
import static store.view.OutputView.RECEIPT_MEMBERSHIP_DISCOUNT;
import static store.view.OutputView.RECEIPT_PROMOTION_DISCOUNT;
import static store.view.OutputView.RECEIPT_PROMOTION_STATUS;
import static store.view.OutputView.RECEIPT_PROMOTION_TITLE;
import static store.view.OutputView.RECEIPT_PURCHASE_MONEY;
import static store.view.OutputView.RECEIPT_PURCHASE_STATUS;
import static store.view.OutputView.RECEIPT_RESULT_LINE;
import static store.view.OutputView.RECEIPT_STORE_NAME;
import static store.view.OutputView.RECEIPT_TITLE;
import static store.view.OutputView.RECEIPT_TOTAL_MONEY;
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
    private static final String ADD_ADDITIONAL_PROMOTION = "현재 %s은(는) %d개를 무료로 더 받으실 수 있습니다. 추가하시겠습니까? (Y/N)\n";
    private static final String ASK_NO_PROMOTION = "현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)\n";
    private static final double MEMBERSHIP_DISCOUNT = 0.3;
    private static final int MAX_MEMBERSHIP_DISCOUNT = 8000;

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

    private void parsePurchases(final String input) {
        String[] items = Splitter.split(input);
        for (String item : items) {
            Matcher matcher = PURCHASE_PATTERN.matcher(item);
            if (matcher.matches()) {
                purchases.add(new Purchase(matcher.group(1), Converter.stringToInt(matcher.group(2))));
            }
        }
    }

    public void validatePurchaseFormat(final String input) {
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

    private void validateProductQuantity(final Purchase purchase) {
        validateZeroQuantity(purchase.getQuantity());
        if (purchase.getQuantity() > store.getTotalAvailableQuantity(purchase.getProductName())) {
            throw new IllegalArgumentException(INVALID_QUANTITY);
        }
    }

    private void validateZeroQuantity(final int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException(NOT_POSITIVE_QUANTITY);
        }
    }

    public void adjustPurchasesForPromotion(final InputView inputView) {
        for (Purchase purchase : purchases) {
            String productName = purchase.getProductName();
            int totalQuantity = purchase.getPurchasedQuantity();

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

                int totalPromotionSet = promotionBuy + promotionGet;
                int numberOfPromotions = totalQuantity / totalPromotionSet;
                int freeQuantity = numberOfPromotions * promotionGet;
                int purchasedQuantity = numberOfPromotions * promotionBuy;

                int remainder = totalQuantity % totalPromotionSet;

                if (remainder >= promotionBuy) {
                    System.out.printf(ADD_ADDITIONAL_PROMOTION, productName, promotionGet);
                    String response;
                    while(true) {
                        try {
                            response = inputView.inputAddFreePromotion();
                            break;
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    if (response.equals(CONFIRM)) {
                        purchasedQuantity += promotionBuy;
                        freeQuantity += promotionGet;
                        numberOfPromotions += 1;
                    } else {
                        purchasedQuantity += remainder;
                    }
                } else if (remainder > 0 && remainder < promotionBuy) {

                    purchasedQuantity += remainder;
                }
                purchase.setPurchasedQuantity(purchasedQuantity);
                purchase.setFreeQuantity(freeQuantity);
            }
        }
    }

    public void reduceStock() {
        for (Purchase purchase : purchases) {
            String productName = purchase.getProductName();
            int purchasedQuantity = purchase.getPurchasedQuantity();
            int freeQuantity = purchase.getFreeQuantity();
            int remainingPurchased = reduceProductStock(productName, purchasedQuantity, true);
            reduceNonPromotion(remainingPurchased, productName);
            askNoPromotion(freeQuantity, productName, purchase);
        }
    }

    private void reduceNonPromotion(int remainingPurchased, final String productName) {
        if (remainingPurchased > 0) {
            remainingPurchased = reduceProductStock(productName, remainingPurchased, false);
            if (remainingPurchased > 0) {
                throw new IllegalArgumentException(INVALID_QUANTITY);
            }
        }
    }

    private void askNoPromotion(final int freeQuantity, final String productName, final Purchase purchase) {
        if (freeQuantity > 0) {
            int remainingFree = reduceProductStock(productName, freeQuantity, true);
            if (remainingFree < 0) {
                System.out.printf(ASK_NO_PROMOTION, productName, remainingFree);
                purchase.setFreeQuantity(freeQuantity - remainingFree);
            }
        }
    }

    private List<Product> isPromotion(final String productName, final boolean isPromotion) {
        if (isPromotion) {
            return store.getPromotionalProductsByName(productName);
        }
        return store.getRegularProductsByName(productName);
    }

    private int reduceProductStock(final String productName, final int quantity, final boolean isPromotion) {
        List<Product> products = isPromotion(productName, isPromotion);
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
        int totalMoney = 0;
        for (Purchase purchase : purchases) {
            String productName = purchase.getProductName();
            int totalQuantity = purchase.getPurchasedQuantity() + purchase.getFreeQuantity();
            totalMoney += store.getProductPriceByName(productName) * totalQuantity;
        }
        return totalMoney;
    }

    public int calculatePromotionDiscount() {
        int discount = 0;
        for (Purchase purchase : purchases) {
            discount += store.getProductPriceByName(purchase.getProductName()) * purchase.getFreeQuantity();
        }
        return discount;
    }

    public int calculateMembershipDiscount(final boolean isMember) {
        if (!isMember) {
            return 0;
        }
        int nonPromotionalAmount = 0;
        for (Purchase purchase : purchases) {
            if (!store.isProductPromotional(purchase.getProductName())) {
                nonPromotionalAmount += store.getProductPriceByName(purchase.getProductName()) * purchase.getPurchasedQuantity();
            }
        }
        return Math.min((int) (nonPromotionalAmount * MEMBERSHIP_DISCOUNT), MAX_MEMBERSHIP_DISCOUNT);
    }

    public String getOrderSummary(final int totalAmount, final int promotionDiscount, final int membershipDiscount) {
        StringBuilder receipt = new StringBuilder();
        setReceiptPurchase(receipt);
        setReceiptPromotion(receipt);
        setReceiptResult(receipt, totalAmount, promotionDiscount, membershipDiscount);
        return receipt.toString();
    }

    private void setReceiptPurchase(final StringBuilder receipt) {
        receipt.append(RECEIPT_STORE_NAME);
        receipt.append(RECEIPT_TITLE);
        for (Purchase purchase : purchases) {
            String productName = purchase.getProductName();
            int totalQuantity = purchase.getPurchasedQuantity() + purchase.getFreeQuantity();
            receipt.append(String.format(RECEIPT_PURCHASE_STATUS, productName, totalQuantity, store.getProductPriceByName(productName) * totalQuantity));
        }
    }

    private void setReceiptPromotion(final StringBuilder receipt) {
        receipt.append(RECEIPT_PROMOTION_TITLE);
        for (Purchase purchase : purchases) {
            int freeQuantity = purchase.getFreeQuantity();
            if (freeQuantity > 0) {
                String productName = purchase.getProductName();
                receipt.append(String.format(RECEIPT_PROMOTION_STATUS, productName, freeQuantity));
            }
        }
    }

    private void setReceiptResult(final StringBuilder receipt, final int totalAmount, final int promotionDiscount, final int membershipDiscount) {
        receipt.append(RECEIPT_RESULT_LINE);
        int finalAmount = totalAmount - promotionDiscount - membershipDiscount;
        receipt.append(String.format(RECEIPT_TOTAL_MONEY, getTotalQuantity(), totalAmount));
        receipt.append(String.format(RECEIPT_PROMOTION_DISCOUNT, promotionDiscount));
        receipt.append(String.format(RECEIPT_MEMBERSHIP_DISCOUNT, membershipDiscount));
        receipt.append(String.format(RECEIPT_PURCHASE_MONEY, finalAmount));
    }

    public int getTotalQuantity() {
        int totalQuantity = 0;
        for (Purchase purchase : purchases) {
            totalQuantity += (purchase.getPurchasedQuantity() + purchase.getFreeQuantity());
        }
        return totalQuantity;
    }
}
