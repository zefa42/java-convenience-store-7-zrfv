package store.controller;

import static store.model.ProductFactory.FILE_PATH;

import java.io.IOException;
import java.util.List;
import store.model.Order;
import store.model.Product;
import store.model.ProductFactory;
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
            return ProductFactory.init(FileLoader.load(FILE_PATH));
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

    public void run() {
        outputView.printWelcomeMessage();
        Store store = new Store(initProduct());
        outputView.printProduct(store);

        Order order = readPurchase(store);
    }
}
