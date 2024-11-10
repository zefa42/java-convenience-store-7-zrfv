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

    public int getMaxPromotionApplications(String productName, Promotion promotion) {
        int availablePromotionStock = getTotalAvailablePromotionQuantity(productName);
        int availablePurchaseStock = getTotalAvailableQuantity(productName);

        int maxByPromotionStock = availablePromotionStock / promotion.getGet();
        int maxByPurchaseStock = availablePurchaseStock / promotion.getBuy();

        return Math.min(maxByPromotionStock, maxByPurchaseStock);
    }

    public int getTotalAvailablePromotionQuantity(String productName) {
        return getPromotionalProductsByName(productName).stream()
                .mapToInt(Product::getQuantity)
                .sum();
    }

    public int getProductPriceByName(String productName) {
        return products.stream()
                .filter(p -> p.getName().equals(productName))
                .findFirst()
                .map(Product::getPrice)
                .orElse(0);
    }
}
