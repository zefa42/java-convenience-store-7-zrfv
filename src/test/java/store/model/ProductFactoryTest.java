package store.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

class ProductFactoryTest {

    @Test
    void 일반_상품이_없는_프로모션_상품의_일반_상품이_추가되는지_테스트한다() {
        List<String> rawProduct = Arrays.asList(
                "오렌지주스,1800,9,MD추천상품",
                "탄산수,1200,5,탄산2+1"
        );
        List<Product> products = ProductFactory.init(rawProduct);

        Product orangeJuiceBasic = products.stream()
                .filter(p -> p.getName().equals("오렌지주스") && p.getQuantity() == 0 && p.getPromotion() == null)
                .findFirst()
                .orElse(null);
        assertEquals(1800, orangeJuiceBasic.getPrice());

        Product sodaWaterBasic = products.stream()
                .filter(p -> p.getName().equals("탄산수") && p.getQuantity() == 0 && p.getPromotion() == null)
                .findFirst()
                .orElse(null);
        assertEquals(1200, sodaWaterBasic.getPrice());
    }
    }

