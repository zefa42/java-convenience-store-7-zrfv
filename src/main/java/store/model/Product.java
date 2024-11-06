package store.model;

import java.util.ArrayList;
import java.util.List;
import store.util.Converter;
import store.util.Splitter;

public class Product {
    private final String name;
    private final int price;
    private final int quantity;
    private final String promotion;

    public Product(final String name, final int price, final int quantity, final String promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public List<Product> init(List<String> rawProduct) {
        List<Product> products = new ArrayList<>();
        for (String line : rawProduct) {
            String[] parts = Splitter.split(line);
            products.add(new Product(parts[0],
                    Converter.stringToInt(parts[1]),
                    Converter.stringToInt(parts[2]),
                    parts[3]));
        }
        return products;
    }
}