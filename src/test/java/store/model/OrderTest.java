package store.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static store.model.ProductFactory.FILE_PATH;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import store.util.FileLoader;

class OrderTest {
    private Store initStore() {
        try {
            List<Product> products = ProductFactory.init(FileLoader.load(FILE_PATH));
            return new Store(products);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"[감자칩-]", "[콜라- ]", "[사이다-사이다]"})
    void 형식에_맞지_않은_값이_입력되면_예외가_발생한다(String input) {
        Store store = initStore();
        assertThatThrownBy(() -> new Order(input, store))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"[감자-1]", "[고구마-2]", "[제로콜라-1]"})
    void 상품이_존재하지_않으면_예외가_발생한다(String input) {
        Store store = initStore();
        assertThatThrownBy(() -> new Order(input, store))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"[감자칩-100]", "[초코바-100]", "[오렌지주스-110]"})
    void 구매_수량이_재고_수량을_초과하면_예외가_발생한다(String input) {
        Store store = initStore();
        assertThatThrownBy(() -> new Order(input, store))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
