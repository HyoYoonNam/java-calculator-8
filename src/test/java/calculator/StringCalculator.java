package calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.Character.*;

public class StringCalculator {

    private static final List<String> BASIC_DELIMITERS = new ArrayList<>(List.of(",", ":"));
    private static final List<String> DELIMITERS = new ArrayList<>(BASIC_DELIMITERS);
    private static final String DECIMAL_REGEX_PATTERN = "0-9";

    public static int calculate(String userInput) {
        int[] separated = separate(userInput);

        int sum = 0;
        for (int number : separated) {
            sum += number;
        }
        return sum;
    }

    public static int[] separate(String userInput) {
        userInput = processCustomDelimiterDefinition(userInput);

        // 사용자가 커스텀 문자열 정의만 하고, 실제 데이터 부분은 입력하지 않는 상황은 정상 입력으로 판단한다.
        // 따라서 빈 문자열 검증은 커스텀 문자열 정의부를 제거한 후인 해당 시점에 진행
        if (userInput.isEmpty()) {
            return new int[]{};
        }

        validateUserInput(userInput);

        String splitRegex = "[" + String.join("", DELIMITERS) + "]";
        String[] split = userInput.split(splitRegex);
        return Stream.of(split)
                .map(String::strip)
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    private static String processCustomDelimiterDefinition(String userInput) {
        String customDelimiterDefinitionRegex = "^//(.*)\\\\n";
        Matcher matcher = Pattern.compile(customDelimiterDefinitionRegex).matcher(userInput);
        if (!matcher.find()) {
            return userInput;
        }

        String delimiter = matcher.group(1);

        validateCustomDelimiter(delimiter);

        if (!DELIMITERS.contains(delimiter)) {
            DELIMITERS.add(delimiter); // 커스텀 구분자를 구분자 리스트에 추가
        }
        return userInput.replace(matcher.group(0), "");
    }

    private static void validateCustomDelimiter(String delimiter) {
        if (delimiter.length() != 1) {
            throw new IllegalArgumentException("커스텀 구분자의 길이는 반드시 1이어야 합니다.");
        }

        if (isNumber(delimiter.charAt(0))) {
            throw new IllegalArgumentException("커스텀 구분자에는 숫자를 지정할 수 없습니다.");
        }

        if (isWhitespace(delimiter.charAt(0))) {
            throw new IllegalArgumentException("커스텀 구분자에는 공백을 지정할 수 없습니다.");
        }
    }

    // 테스트 메서드에서의 단어(number)와 맞추기 위해 Character.isDigit()을 래핑
    private static boolean isNumber(char ch) {
        return isDigit(ch);
    }

    private static void validateUserInput(String userInput) {
        validateAllowedCharacters(userInput);
        validateDelimitersPosition(userInput);
        validateDelimitersRepetition(userInput);
        validateWhitespacePosition(userInput);
    }

    private static void validateWhitespacePosition(String userInput) {
        String whitespaceBetweenNumbersRegex = "\\d[\\s]+\\d";
        if (Pattern.compile(whitespaceBetweenNumbersRegex).matcher(userInput).find()) {
            throw new IllegalArgumentException("숫자와 숫자 사이에는 공백이 존재할 수 없습니다.");
        }
    }

    private static void validateDelimitersRepetition(String userInput) {
        // 정규표현식 "[,:]{2,}"는 '['와 ']' 사이에 있는 문자가 2회 이상 반복되는 패턴과 매칭된다.
        String repeatedDelimiterRegex = "[" + String.join("", DELIMITERS) + "]{2,}";
        if (Pattern.compile(repeatedDelimiterRegex).matcher(userInput).find()) {
            throw new IllegalArgumentException("구분자가 연속해서 2회 이상 반복되었습니다.");
        }
    }

    private static void validateDelimitersPosition(String userInput) {
        // 문자열의 처음이나 끝에 구분자가 있는 패턴에 매칭되는 정규표현식
        String delimiterIllegalPositionRegex = "^[" + String.join("", DELIMITERS) + "]"
                + "|[" + String.join("", DELIMITERS) + "]$";

        if (Pattern.compile(delimiterIllegalPositionRegex).matcher(userInput).find()) {
            throw new IllegalArgumentException("구분자는 숫자와 숫자 사이에만 존재할 수 있습니다.");
        }
    }

    private static void validateAllowedCharacters(String userInput) {
        String delimiterRegexPattern = String.join("", DELIMITERS);
        /*
         * [^0-9,:\\s] 정규표현식은 '[^'와 ']' 사이에 있는 문자들을 제외한 모든 패턴에 매칭된다.
         * whitespace(\\s)는 일단 존재 자체를 허용하고, 올바른 위치에 대한 검증은 validateWhitespacePosition 메서드에서 한다.
         */
        String disallowedCharactersRegex = "[^" + DECIMAL_REGEX_PATTERN + delimiterRegexPattern + "\\s]";
        if (Pattern.compile(disallowedCharactersRegex).matcher(userInput).find()) {
            throw new IllegalArgumentException("허용되지 않은 구분자가 존재합니다.");
        }
    }
}
