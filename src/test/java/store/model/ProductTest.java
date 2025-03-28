package store.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import store.util.FileLoader;

import java.io.IOException;
import java.util.List;

class ProductTest {


    @Test
    void 불러온_파일을_형식에_맞게_출력한다() throws IOException {
        List<String> rawProducts = FileLoader.load(ProductFactory.PRODUCT_FILE_PATH).subList(0, 3);
        List<Product> products = ProductFactory.init(rawProducts);
        List<String> expectedOutput = List.of(
                "- 콜라 1,000원 10개 탄산2+1",
                "- 콜라 1,000원 10개",
                "- 사이다 1,000원 8개 탄산2+1"
        );
        for (int i = 0; i < 3; i++) {
            assertEquals(expectedOutput.get(i), products.get(i).toString());
        }
    }
}
