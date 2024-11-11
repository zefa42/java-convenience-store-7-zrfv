package store.model;

import java.util.List;
import java.util.stream.Collectors;

public class Store {
    private final List<Product> products;

    public Store(final List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }

    public int getProductPriceByName(final String productName) {
        return products.stream()
                .filter(p -> p.getName().equals(productName))
                .findFirst()
                .map(Product::getPrice)
                .orElse(0);
    }

    public List<Product> getPromotionalProductsByName(final String productName) {
        return products.stream()
                .filter(p -> p.getName().equals(productName) && p.isPromotional())
                .collect(Collectors.toList());
    }

    public List<Product> getRegularProductsByName(final String productName) {
        return products.stream()
                .filter(p -> p.getName().equals(productName) && !p.isPromotional())
                .collect(Collectors.toList());
    }

    public int getTotalAvailableQuantity(final String productName) {
        return products.stream()
                .filter(p -> p.getName().equals(productName))
                .mapToInt(Product::getQuantity)
                .sum();
    }

    public int getTotalAvailablePromotionQuantity(final String productName) {
        return getPromotionalProductsByName(productName).stream()
                .mapToInt(Product::getQuantity)
                .sum();
    }

    public boolean isProductPromotional(final String productName) {
        return products.stream()
                .filter(p -> p.getName().equals(productName))
                .anyMatch(Product::isPromotional);
    }
}
