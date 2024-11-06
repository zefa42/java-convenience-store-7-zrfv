package store.view.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class InputValidatorTest {
    @ParameterizedTest
    @ValueSource(strings = {"", "\t  "})
    void 입력된_문자열이_공백일_때_예외가_발생한다(String input) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new InputValidator().validateEmptyInput(input));
        assertEquals("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"[콜라-3[", "   [콜라-1]"})
    void 구매_상품_입력이_대괄호로_시작하고_끝나지_않으면_예외를_발생시킨다(String input) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new InputValidator().validateProductFormat(input));
        assertEquals("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.", exception.getMessage());
    }
}