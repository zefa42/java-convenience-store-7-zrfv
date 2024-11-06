package store.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {
    private static final String BUY_PRODUCT_MESSAGE = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";

    public static String inputProduct() {
        System.out.println(BUY_PRODUCT_MESSAGE);
        return Console.readLine();
    }
    
    public static void close() {
        Console.close();
    }
}
