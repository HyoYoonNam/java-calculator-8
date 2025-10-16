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

        if (String.join("", DELIMITERS)
                .contains(String.valueOf(userInput.charAt(0)))) {
            throw new IllegalArgumentException("구분자가 가장 앞에 위치합니다. 구분자는 숫자 뒤에 위치해야 합니다.");
        }

        if (String.join("", DELIMITERS)
                .contains(String.valueOf(userInput.charAt(userInput.length() - 1)))) {
            throw new IllegalArgumentException("구분자가 가장 뒤에 위치합니다. 구분자 뒤에는 숫자가 존재해야 합니다.");
        }

        // 정규표현식 "[,:]{2,}"는 '['와 ']' 사이에 있는 문자가 2회 이상 반복되는 패턴과 매칭된다.
        String repeatedDelimiterRegex = "[" + String.join("", DELIMITERS) + "]{2,}";
        if (Pattern.compile(repeatedDelimiterRegex).matcher(userInput).find()) {
            throw new IllegalArgumentException("구분자가 연속해서 2회 이상 반복되었습니다.");
        }

        String splitRegex = "[" + String.join("", DELIMITERS) + "]";
        String[] split = userInput.split(splitRegex);
        return Stream.of(split)
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    private static boolean hasOnlyAllowedCharacters(String userInput) {
        String delimiterRegexPattern = String.join("", DELIMITERS);
        /* regex는 "^[0-9,:]+$" 꼴이 된다.
         * 이를 str.matches(regex)로 검증하면,
         * - 처음부터('^') 끝까지('$')
         * - '['와 ']' 사이에 있는 문자들로만
         * - 0번 이상 반복되는('*')
         * 경우에만 true를 리턴한다.
         * 이때 1번 이상 반복을 의미하는 '+'가 아니라, 0번 이상 반복을 의미하는 '*'를 쓴 것은 공백 입력 허용을 나타냄
         */
        String regex = "^[" + DECIMAL_REGEX_PATTERN + delimiterRegexPattern + "]+$";
        return userInput.matches(regex);
    }
}
