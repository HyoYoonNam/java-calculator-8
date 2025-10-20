package calculator;

public class StringCalculator {

    public static long calculate(String userInput) {
        long[] separated = StringSeparator.separate(userInput);

        long sum = 0;
        for (long number : separated) {
            sum += number;
        }
        return sum;
    }
}
