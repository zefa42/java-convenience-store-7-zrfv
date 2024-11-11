package store.view;

import store.model.Order;
import store.model.Product;
import store.model.Store;

public class OutputView {
    private static final String WELCOME_MESSAGE = """
        안녕하세요. W편의점입니다.
        현재 보유하고 있는 상품입니다.
        """;

    public static final String RECEIPT_STORE_NAME= "==============W 편의점================\n";
    public static final String RECEIPT_TITLE= "상품명\t\t\t\t수량\t\t\t금액\n";
    public static final String RECEIPT_PURCHASE_STATUS= "%s\t\t\t\t%d\t\t\t%,d\n";
    public static final String RECEIPT_PROMOTION_TITLE= "=============증\t정===============\n";
    public static final String RECEIPT_PROMOTION_STATUS= "%s\t\t\t\t%d\n";
    public static final String RECEIPT_RESULT_LINE= "====================================\n";
    public static final String RECEIPT_TOTAL_MONEY= "총구매액\t\t\t\t%d\t\t\t%,d\n";
    public static final String RECEIPT_PROMOTION_DISCOUNT= "행사할인\t\t\t\t\t\t\t-%,d\n";
    public static final String RECEIPT_MEMBERSHIP_DISCOUNT= "멤버십할인\t\t\t\t\t\t-%,d\n";
    public static final String RECEIPT_PURCHASE_MONEY = "내실돈\t\t\t\t\t\t\t%,d\n";

    public void printWelcomeMessage() {
        System.out.println(WELCOME_MESSAGE);
    }

    public void printProduct(Store store) {
        for (Product product : store.getProducts()) {
            System.out.println(product);
        }
        System.out.println();
    }

    public void printOrderResult(Order order, int totalAmount, int promotionDiscount, int membershipDiscount) {
        System.out.println(order.getOrderSummary(totalAmount, promotionDiscount, membershipDiscount));
    }
}
