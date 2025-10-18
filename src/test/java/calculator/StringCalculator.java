package calculator;

import java.util.regex.Pattern;
import java.util.stream.Stream;

public class StringCalculator {

    private static final String[] DELIMITERS = new String[]{",", ":"};
    private static final String DECIMAL_REGEX_PATTERN = "0-9";

    public static int[] separate(String userInput) {
        // TODO : '잘못된 입력'에 대한 경우가 더 많아질 것으로 예상되는데, 그때는 별도로 validate() 메서드나 객체를 만들면 좋겠음
        if (!hasOnlyAllowedCharacters(userInput)) {
            throw new IllegalArgumentException("허용되지 않은 구분자가 존재합니다.");
        }

        delimitersPositionValidate(userInput);

        delimitersRepetitionValidate(userInput);

        String whitespaceBetweenNumbersRegex = "\\d[\\s]+\\d";
        if (Pattern.compile(whitespaceBetweenNumbersRegex).matcher(userInput).find()) {
            throw new IllegalArgumentException("숫자와 숫자 사이에는 공백이 존재할 수 없습니다.");
        }

        String splitRegex = "[" + String.join("", DELIMITERS) + "]";
        String[] split = userInput.split(splitRegex);
        return Stream.of(split)
                .map(String::strip)
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    private static void delimitersRepetitionValidate(String userInput) {
        // 정규표현식 "[,:]{2,}"는 '['와 ']' 사이에 있는 문자가 2회 이상 반복되는 패턴과 매칭된다.
        String repeatedDelimiterRegex = "[" + String.join("", DELIMITERS) + "]{2,}";
        if (Pattern.compile(repeatedDelimiterRegex).matcher(userInput).find()) {
            throw new IllegalArgumentException("구분자가 연속해서 2회 이상 반복되었습니다.");
        }
    }

    private static void delimitersPositionValidate(String userInput) {
        // 문자열의 처음이나 끝에 구분자가 있는 패턴에 매칭되는 정규표현식
        String delimiterIllegalPositionRegex = "^[" + String.join("", DELIMITERS) + "]"
                + "|[" + String.join("", DELIMITERS) + "]$";

        if (Pattern.compile(delimiterIllegalPositionRegex).matcher(userInput).find()) {
            throw new IllegalArgumentException("구분자는 숫자와 숫자 사이에만 존재할 수 있습니다.");
        }
    }

    private static boolean hasOnlyAllowedCharacters(String userInput) {
        String delimiterRegexPattern = String.join("", DELIMITERS);
        /* regex는 "^[0-9,:]+$" 꼴이 된다.
         * 이를 str.matches(regex)로 검증하면,
         * - 처음부터('^') 끝까지('$')
         * - '['와 ']' 사이에 있는 문자들로만
         * - 0번 이상 반복되는('*')
         * 경우에만 true를 리턴한다.
         * "\\s"는 whitespace를 나타냄
         * 이때 1번 이상 반복을 의미하는 '+'가 아니라, 0번 이상 반복을 의미하는 '*'를 쓴 것은 공백 입력 허용을 나타냄
         */
        String regex = "^[" + DECIMAL_REGEX_PATTERN + delimiterRegexPattern + "\\s" + "]+$";
        return userInput.matches(regex);
    }
}
