package store.controller;

import static store.model.ProductFactory.PRODUCT_FILE_PATH;
import static store.model.Promotion.PROMOTION_FILE_PATH;

import java.io.IOException;
import java.util.List;
import store.model.Order;
import store.model.Product;
import store.model.ProductFactory;
import store.model.Promotion;
import store.model.Store;
import store.util.FileLoader;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    private final InputView inputView;
    private final OutputView outputView;

    public StoreController(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    private List<Product> initProduct() {
        try {
            return ProductFactory.init(FileLoader.load(PRODUCT_FILE_PATH));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Order readPurchase(Store store) {
        try {
            return new Order(inputView.inputPurchase(), store);
        }catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return readPurchase(store);
        }
    }

    private void initPromotion() {
        try {
            Promotion.init(FileLoader.load(PROMOTION_FILE_PATH));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        outputView.printWelcomeMessage();
        Store store = new Store(initProduct());
        initPromotion();

        boolean continueShopping = true;
        while (continueShopping) {
            outputView.printProduct(store);
            Order order = readPurchase(store);
            order.adjustPurchasesForPromotion(inputView);

            boolean isMember = inputView.inputMembership();
            int totalAmount = order.calculateTotalAmount();
            int promotionDiscount = order.calculatePromotionDiscount();
            int membershipDiscount = order.calculateMembershipDiscount(isMember);

            order.reduceStock();
            outputView.printOrderResult(order, totalAmount, promotionDiscount, membershipDiscount);
            continueShopping = inputView.inputContinueShopping();
        }
    }
}
