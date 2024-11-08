package store.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static store.model.ProductFactory.FILE_PATH;

import java.io.IOException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import store.util.FileLoader;

class OrderTest {

    @ParameterizedTest
    @ValueSource(strings = {"[감자칩-]", "[콜라- ]", "[사이다-사이다]"})
    void 형식에_맞지_않은_값이_입력되면_예외가_발생한다(String input) {
        assertThatThrownBy(() -> new Order(input))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"[감자-1]", "[고구마-2]", "[제로콜라-1]"})
    void 상품이_존재하지_않으면_예외가_발생한다(String input) {
        try {
            new Store(ProductFactory.init(FileLoader.load(FILE_PATH)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertThatThrownBy(() -> new Order(input))
                .isInstanceOf(IllegalArgumentException.class);
    }
}