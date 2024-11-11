package store.model;

import java.util.ArrayList;
import java.util.List;
import store.util.Converter;
import store.util.Splitter;

public class ProductFactory {
    public static final String PRODUCT_FILE_PATH = "src/main/resources/products.md";
    public static final String EMPTY_PRODUCT_ORANGE_JUICE = "오렌지주스";
    public static final String EMPTY_PRODUCT_SPARKLING_WATER = "탄산수";

    public static List<Product> init(final List<String> rawProduct) {
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

    private static void addMissingProduct(final List<Product> products, final String[] parts) {
        if(parts[0].equals(EMPTY_PRODUCT_ORANGE_JUICE)) {
            products.add(new Product(parts[0], Converter.stringToInt(parts[1]), 0, null));
        }

        if(parts[0].equals(EMPTY_PRODUCT_SPARKLING_WATER)) {
            products.add(new Product(parts[0], Converter.stringToInt(parts[1]), 0, null));
        }
    }
}
