package store.view.validator;

public class InputValidator {
    public static final String EMPTY_INPUT_ERROR_MESSAGE = "[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.";

    public static void validateEmptyInput(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException(EMPTY_INPUT_ERROR_MESSAGE);
        }
    }
}
