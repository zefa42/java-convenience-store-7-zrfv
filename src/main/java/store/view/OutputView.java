package store.view;

public class OutputView {
    private static final String WELCOME_MESSAGE = """
        안녕하세요. W편의점입니다.
        현재 보유하고 있는 상품입니다.
        """;

    public static void printWelcomeMessage() {
        System.out.println(WELCOME_MESSAGE);
    }
}
