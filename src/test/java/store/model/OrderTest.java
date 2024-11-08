package store.model;


import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class OrderTest {

    @ParameterizedTest
    @ValueSource(strings = {"[감자칩-]", "[콜라- ]", "[사이다-사이다]"})
    void 형식에_맞지_않은_값이_입력되면_예외가_발생한다(String input) {
        assertThatThrownBy(() -> new Order(input))
                .isInstanceOf(IllegalArgumentException.class);
    }
}