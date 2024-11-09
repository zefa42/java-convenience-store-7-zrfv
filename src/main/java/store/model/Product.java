package store.model;

public class Product {
    private final String name;
    private final int price;
    private int quantity;
    private final String promotion;

    Product(final String name, final int price, final int quantity, final String promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
        ProductName.add(name);
    }

    // Getter 메서드
    public boolean isPromotional() {
        return !(promotion == null || promotion.equals("null"));
    }

    public int getQuantity() {
        return quantity;
    }

    public void reduceQuantity(int amount) {
        quantity -= amount;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getPromotion() {
        return promotion;
    }

    private String formattedPrice() {
        return String.format("%,d원 ", price);
    }

    private String formattedQuantity() {
        if (quantity > 0) {
            return quantity + "개";
        }
        return "재고 없음";
    }

    private String formattedPromotion() {
        if (promotion == null || promotion.equals("null")) {
            return "";
        }
        return " " + promotion;
    }

    @Override
    public String toString() {
        return "- " + name + " "
                + formattedPrice()
                + formattedQuantity()
                + formattedPromotion();
    }
}
