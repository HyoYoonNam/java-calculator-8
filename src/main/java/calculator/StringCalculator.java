package calculator;

public class StringCalculator {

    public static double calculate(String userInput) {
        double[] separated = StringSeparator.separate(userInput);

        double sum = 0;
        for (double number : separated) {
            sum += number;
        }
        return sum;
    }
}
