package calculator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StringSeparatorTest {

    @Test
    @DisplayName("사용자가 입력한 문자열을 기본 구분자 콤마(`,`)를 기준으로 분리할 수 있다")
    void separate_user_input_by_basic_delimiter_comma() {
        // given
        String userInput = "1,2,3";
        String userInput2 = "1,2,3,4,5";

        // when
        int[] separated = StringSeparator.separate(userInput);
        int[] separated2 = StringSeparator.separate(userInput2);

        // then
        assertThat(separated).isEqualTo(new int[]{1, 2, 3});
        assertThat(separated2).isEqualTo(new int[]{1, 2, 3, 4, 5});
    }

    @Test
    @DisplayName("사용자가 입력한 문자열을 기본 구분자 콜론(`:`)을 기준으로 분리할 수 있다")
    void separate_user_input_by_basic_delimiter_colon() {
        // given
        String userInput = "1:2:3";
        String userInput2 = "1:2:3:4:5";

        // when
        int[] separated = StringSeparator.separate(userInput);
        int[] separated2 = StringSeparator.separate(userInput2);

        // then
        assertThat(separated).isEqualTo(new int[]{1, 2, 3});
        assertThat(separated2).isEqualTo(new int[]{1, 2, 3, 4, 5});
    }

    @Test
    @DisplayName("기본 구분자 콤마(,)와 콜론(:)이 모두 존재해도 분리할 수 있다.")
    void separate_user_input_by_basic_delimiter() {
        // given
        String userInput = "1,2:3";
        String userInput2 = "1:2,3,4:5";

        // when
        int[] separated = StringSeparator.separate(userInput);
        int[] separated2 = StringSeparator.separate(userInput2);

        // then
        assertThat(separated).isEqualTo(new int[]{1, 2, 3});
        assertThat(separated2).isEqualTo(new int[]{1, 2, 3, 4, 5});
    }

    @Test
    @DisplayName("구분자가 아닌 문자열이 존재하면 예외를 발생시킨다")
    void when_user_input_contains_non_delimiter_then_throw_IllegalArgEx() {
        // given
        String userInput = "1,2.3:4;5";
        String userInput2 = "1,2a3,4a5";

        // when and then
        assertThatThrownBy(() -> {
            StringSeparator.separate(userInput);
        }).isExactlyInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> {
            StringSeparator.separate(userInput2);
        }).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("구분자가 연속해서 2회 이상 반복되면 예외를 발생시킨다")
    void when_delimiters_repeated_then_throw_IllegalArgEx() {
        // given
        String userInput = "1,,2,3";
        String userInput2 = "1,2:,:,3";

        // when and then
        assertThatThrownBy(() -> {
            StringSeparator.separate(userInput);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("구분자가 연속해서 2회 이상 반복되었습니다.");
    }

    @Test
    @DisplayName("구분자는 숫자와 숫자 사이에만 위치할 수 있다. 다른 위치라면 예외를 발생시킨다.")
    void when_delimiters_position_is_not_between_numbers_then_throw_IllegalArgEx() {
        // given
        String userInput = ",:1,2:3";
        String userInput2 = "1,2:3:,";

        // when and then
        assertThatThrownBy(() -> {
            StringSeparator.separate(userInput);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("구분자는 숫자와 숫자 사이에만 존재할 수 있습니다.");

        assertThatThrownBy(() -> {
            StringSeparator.separate(userInput2);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("구분자는 숫자와 숫자 사이에만 존재할 수 있습니다.");
    }

    @Test
    @DisplayName("숫자의 앞, 뒤, 숫자와 구분자 사이에 있는 공백은 허용한다")
    void when_user_input_has_whitespace_then_strip() {
        // given
        String userInput = " 1, 2 ,3 , 4,   5 ";

        // when
        int[] separated = StringSeparator.separate(userInput);

        // then
        assertThat(separated).isEqualTo(new int[]{1, 2, 3, 4, 5});
    }

    @Test
    @DisplayName("숫자와 숫자 사이에 공백이 있으면 예외를 발생시킨다")
    void when_user_input_has_whitespace_between_numbers_then_throw_IllegalArgEx() {
        // given
        String userInput = "1, 2 ,3 4";

        // when and then
        assertThatThrownBy(() -> {
            StringSeparator.separate(userInput);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("숫자와 숫자 사이에는 공백이 존재할 수 없습니다.");
    }

    @Test
    @DisplayName("커스텀 구분자를 추가해도 기본 구분자는 여전히 사용 가능하다")
    void separate_user_input_by_basic_delimiter_and_also_custom_delimiter() {
        // given
        String userInput = "//;\\n1; 2, 3: 4";

        // when
        int[] separated = StringSeparator.separate(userInput);

        // then
        assertThat(separated).isEqualTo(new int[]{1, 2, 3, 4});
    }

    @Test
    @DisplayName("커스텀 구분자에 숫자를 지정하면 예외를 발생시킨다")
    void when_custom_delimiter_is_number_then_throw_IllegalArgEx() {
        // given
        String userInput = "//1\\n11 2, 3"; // 커스텀 구분자에 숫자를 지정한 것으로 인한 예외가 먼저 발생

        // when and then
        assertThatThrownBy(() -> {
            StringSeparator.separate(userInput);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("커스텀 구분자에는 숫자를 지정할 수 없습니다.");
    }

    @Test
    @DisplayName("커스텀 구분자에 공백을 지정하면 예외를 발생시킨다")
    void when_custom_delimiter_is_whitespace_then_throw_IllegalArgEx() {
        // given
        String userInput = "// \\n1 2, 3"; // 커스텀 구분자에 공백을 지정한 것으로 인한 예외가 먼저 발생

        // when and then
        assertThatThrownBy(() -> {
            StringSeparator.separate(userInput);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("커스텀 구분자에는 공백을 지정할 수 없습니다.");
    }

    @Test
    @DisplayName("커스텀 구분자의 길이가 1이 아니라면 예외를 발생시킨다")
    void when_length_of_custom_delimiter_is_not_1_then_throw_IllegalArgEx() {
        // given
        String userInput = "//,,\\n1, 2,, 3, 4";

        // when and then
        assertThatThrownBy(() -> {
            StringSeparator.separate(userInput);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("커스텀 구분자의 길이는 반드시 1이어야 합니다.");
    }


    @Test
    @DisplayName("커스텀 구분자로 기본 구분자(,와 :)를 명시한 경우, 사용자의 실수라고 판단하여 보정하고 정상 진행한다")
    void when_custom_delimiter_is_basic_delimiter_then_separates_correctly() {
        // given
        String userInput = "//:\\n1: 2, 3: 4";

        // when
        int[] separated = StringSeparator.separate(userInput);

        // then
        assertThat(separated).isEqualTo(new int[]{1, 2, 3, 4});
    }

    @Test
    @DisplayName("빈 문자열이 입력되면, 빈 int 배열을 리턴한다")
    void when_user_input_is_empty_string_then_return_empty_int_array() {
        // given
        String userInput = "";
        String userInput2 = "//;\\n"; // 커스텀 문자열 정의만 하고, 실제 데이터 부분은 입력하지 않은 경우

        // when
        int[] separated = StringSeparator.separate(userInput);
        int[] separated2 = StringSeparator.separate(userInput2);

        // then
        assertThat(separated).isEqualTo(new int[]{});
        assertThat(separated2).isEqualTo(new int[]{});
    }
}
