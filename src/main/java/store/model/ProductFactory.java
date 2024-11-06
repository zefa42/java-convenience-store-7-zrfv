package store.model;

import java.util.ArrayList;
import java.util.List;
import store.util.Converter;
import store.util.Splitter;

public class ProductFactory {
    public static final String FILE_PATH = "src/main/resources/products.md";

    public static List<Product> init(List<String> rawProduct) {
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
