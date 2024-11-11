package store.model;

public class Purchase {
    private final String productName;
    private int quantity;
    private int freeQuantity = 0;

    public Purchase(final String productName, final int quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }

    public void setPurchasedQuantity(final int quantity) {
        this.quantity = quantity;
    }

    public void setFreeQuantity(final int freeQuantity) {
        this.freeQuantity = freeQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getFreeQuantity() {
        return freeQuantity;
    }

    public int getPurchasedQuantity() {
        return quantity;
    }
}