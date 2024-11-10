package store.model;

import java.util.ArrayList;
import java.util.List;
import store.util.Converter;
import store.util.Splitter;

public class ProductFactory {
    public static final String PRODUCT_FILE_PATH = "src/main/resources/products.md";

    public static List<Product> init(List<String> rawProduct) {
        List<Product> products = new ArrayList<>();
        for (String line : rawProduct) {
            String[] parts = Splitter.split(line);
            products.add(new Product(parts[0],
                    Converter.stringToInt(parts[1]),
                    Converter.stringToInt(parts[2]),
                    parts[3]));
            addMissingProduct(products, parts);
        }
        return products;
    }

    private static void addMissingProduct(List<Product> products, String[] parts) {
        if(parts[0].equals("오렌지주스")) {
            products.add(new Product(parts[0], Converter.stringToInt(parts[1]), 0, null));
        }

        if(parts[0].equals("탄산수")) {
            products.add(new Product(parts[0], Converter.stringToInt(parts[1]), 0, null));
        }
    }
}
