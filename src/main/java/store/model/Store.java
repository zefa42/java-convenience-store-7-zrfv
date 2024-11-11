package store.model;

import java.util.List;
import java.util.stream.Collectors;

public class Store {
    private final List<Product> products;

    public Store(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }

    public int getProductPriceByName(String productName) {
        return products.stream()
                .filter(p -> p.getName().equals(productName))
                .findFirst()
                .map(Product::getPrice)
                .orElse(0);
    }

    public List<Product> getPromotionalProductsByName(String productName) {
        return products.stream()
                .filter(p -> p.getName().equals(productName) && p.isPromotional())
                .collect(Collectors.toList());
    }

    public List<Product> getRegularProductsByName(String productName) {
        return products.stream()
                .filter(p -> p.getName().equals(productName) && !p.isPromotional())
                .collect(Collectors.toList());
    }

    public int getTotalAvailableQuantity(String productName) {
        return products.stream()
                .filter(p -> p.getName().equals(productName))
                .mapToInt(Product::getQuantity)
                .sum();
    }

    public int getTotalAvailablePromotionQuantity(String productName) {
        return getPromotionalProductsByName(productName).stream()
                .mapToInt(Product::getQuantity)
                .sum();
    }

    public boolean isProductPromotional(String productName) {
        return products.stream()
                .filter(p -> p.getName().equals(productName))
                .anyMatch(Product::isPromotional);
    }
}
