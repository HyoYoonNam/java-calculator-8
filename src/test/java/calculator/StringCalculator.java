package calculator;

import java.util.stream.Stream;

public class StringCalculator {

    public static int[] separate(String userInput) {
        String[] split = userInput.split("[,:]");
        return Stream.of(split).
                        mapToInt(Integer::parseInt).
                                toArray();
    }
}
