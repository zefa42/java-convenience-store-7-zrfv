package store.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

public class StoreTest {
    private Store store;

    @BeforeEach
    public void setUp() {
        Promotion.init(Arrays.asList(
                "탄산2+1,2,1,2024-01-01,2024-12-31",
                "MD추천상품,1,1,2024-01-01,2024-12-31",
                "반짝할인,1,1,2024-11-01,2024-11-30"
        ));
        List<Product> products = Arrays.asList(
                new Product("콜라", 1000, 10, "탄산2+1"),
                new Product("콜라", 1000, 10, null),
                new Product("사이다", 1000, 8, "탄산2+1"),
                new Product("사이다", 1000, 7, null),
                new Product("오렌지주스", 1800, 9, "MD추천상품"),
                new Product("탄산수", 1200, 5, "탄산2+1"),
                new Product("물", 500, 10, null),
                new Product("비타민워터", 1500, 6, null),
                new Product("감자칩", 1500, 5, "반짝할인"),
                new Product("감자칩", 1500, 5, null),
                new Product("초코바", 1200, 5, "MD추천상품"),
                new Product("초코바", 1200, 5, null),
                new Product("에너지바", 2000, 5, null),
                new Product("정식도시락", 6400, 8, null),
                new Product("컵라면", 1700, 1, "MD추천상품"),
                new Product("컵라면", 1700, 10, null)
        );
        store = new Store(products);
    }

    @Test
    void 프로모션_제품과_일반_제품의_총합을_계산한다() {
        assertEquals(20, store.getTotalAvailableQuantity("콜라"));
        assertEquals(15, store.getTotalAvailableQuantity("사이다"));
        assertEquals(11, store.getTotalAvailableQuantity("컵라면"));
    }

    @Test
    void 프로모션_제품의_재고를_검사한다() {
        assertEquals(10, store.getTotalAvailablePromotionQuantity("콜라"));
        assertEquals(5, store.getTotalAvailablePromotionQuantity("탄산수"));
        assertEquals(1, store.getTotalAvailablePromotionQuantity("컵라면"));
        assertEquals(5, store.getTotalAvailablePromotionQuantity("감자칩"));
        assertEquals(0, store.getTotalAvailablePromotionQuantity("물"));
    }

    @Test
    void 프로모션_제품에_프로모션이_적용되었는지_테스트한다() {
        assertTrue(store.isProductPromotional("콜라"));
        assertTrue(store.isProductPromotional("사이다"));
        assertTrue(store.isProductPromotional("오렌지주스"));
        assertTrue(store.isProductPromotional("탄산수"));
        assertTrue(store.isProductPromotional("감자칩"));
        assertTrue(store.isProductPromotional("컵라면"));
        assertTrue(store.isProductPromotional("초코바"));
    }
}
