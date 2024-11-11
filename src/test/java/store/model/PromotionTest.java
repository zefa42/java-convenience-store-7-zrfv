package store.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class PromotionTest {
    @Test
    void 프로모션_정보가_형식에_맞추어_입력됐는지_테스트한다() {
        List<String> rawPromotions = Arrays.asList(
                "탄산2+1,2,1,2024-01-01,2024-12-31",
                "MD추천상품,1,1,2024-01-01,2024-12-31"
        );
        Promotion.init(rawPromotions);
        List<Promotion> promotions = Promotion.getPromotions();

        Promotion firstPromotion = promotions.get(0);
        assertEquals("탄산2+1", firstPromotion.getName());
        assertEquals(LocalDate.of(2024, 01, 01), firstPromotion.getStart_date());
        assertEquals(LocalDate.of(2024, 12, 31), firstPromotion.getEnd_date());

        Promotion secondPromotion = promotions.get(1);
        assertEquals("MD추천상품", secondPromotion.getName());
        assertEquals(LocalDate.of(2024, 01, 01), secondPromotion.getStart_date());
        assertEquals(LocalDate.of(2024, 12, 31), secondPromotion.getEnd_date());
    }
}
