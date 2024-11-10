package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.view.validator.InputValidator;

public class InputView {
    private static final String BUY_PRODUCT_MESSAGE = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String MEMBERSHIP_MESSAGE = "멤버십 할인을 받으시겠습니까? (Y/N)";

    public String inputPurchase() {
        System.out.println(BUY_PRODUCT_MESSAGE);
        String input = Console.readLine();
        InputValidator.validateEmptyInput(input);
        return input;
    }

    public boolean inputMembership() {
        System.out.println(MEMBERSHIP_MESSAGE);
        String input = Console.readLine();
        return input.equalsIgnoreCase("Y");
    }

    public String inputYesOrNo() {
        String input = Console.readLine();
        return input;
    }

    public static void close() {
        Console.close();
    }
}
