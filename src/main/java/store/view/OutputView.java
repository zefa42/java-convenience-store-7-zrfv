package store.view;

import store.model.Order;
import store.model.Product;
import store.model.Store;

public class OutputView {
    private static final String WELCOME_MESSAGE = """
        안녕하세요. W편의점입니다.
        현재 보유하고 있는 상품입니다.
        """;

    public void printWelcomeMessage() {
        System.out.println(WELCOME_MESSAGE);
    }

    public void printProduct(Store store) {
        for (Product product : store.getProducts()) {
            System.out.println(product);
        }
        System.out.println();
    }
}
