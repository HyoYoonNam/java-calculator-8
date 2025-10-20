package calculator;

public class StringCalculator {

    public static int calculate(String userInput) {
        int[] separated = StringSeparator.separate(userInput);

        int sum = 0;
        for (int number : separated) {
            sum += number;
        }
        return sum;
    }
}
