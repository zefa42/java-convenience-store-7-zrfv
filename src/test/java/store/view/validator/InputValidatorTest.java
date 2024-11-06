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
}