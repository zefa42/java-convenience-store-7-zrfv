package store.view.validator;

public class InputValidator {
    public static final String EMPTY_INPUT_ERROR_MESSAGE = "[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.";
    public static final String PRODUCT_FORMAT_ERROR_MESSAGE = "[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.";

    public static void validateEmptyInput(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException(EMPTY_INPUT_ERROR_MESSAGE);
        }
    }

    public static void validateProductFormat(String input) {
        if (!(input.startsWith("[") && input.endsWith("]"))) {
            throw new IllegalArgumentException(PRODUCT_FORMAT_ERROR_MESSAGE);
        }

        if (!input.contains("-")) {
            throw new IllegalArgumentException(PRODUCT_FORMAT_ERROR_MESSAGE);
        }
    }

    public static void validateYesOrNo(String input) {
        if (!(input.equals("Y")) && !(input.equals("N"))) {
            throw new IllegalArgumentException(PRODUCT_FORMAT_ERROR_MESSAGE);
        }
    }
}
