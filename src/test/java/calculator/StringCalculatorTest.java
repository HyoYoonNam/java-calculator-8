package calculator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StringCalculatorTest {

    @Test
    @DisplayName("사용자가 입력한 문자열을 기본 구분자 콤마(`,`)를 기준으로 분리할 수 있다")
    void separate_user_input_by_basic_delimiter_comma() {
        // given
        String userInput = "1,2,3";
        String userInput2 = "1,2,3,4,5";

        // when
        int[] separated = StringCalculator.separate(userInput);
        int[] separated2 = StringCalculator.separate(userInput2);

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
        int[] separated = StringCalculator.separate(userInput);
        int[] separated2 = StringCalculator.separate(userInput2);

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
        int[] separated = StringCalculator.separate(userInput);
        int[] separated2 = StringCalculator.separate(userInput2);

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
            StringCalculator.separate(userInput);
        }).isExactlyInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> {
            StringCalculator.separate(userInput2);
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
            StringCalculator.separate(userInput);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("구분자가 연속해서 2회 이상 반복되었습니다.");
    }

    @Test
    @DisplayName("구분자가 사용자 입력의 가장 앞에 위치하면 예외를 발생시킨다")
    void when_delimiters_position_is_first_of_user_input_then_throw_IllegalArgEx() {
        // given
        String userInput = ",1,2:3";
        String userInput2 = ",:1,2:3";

        // when and then
        assertThatThrownBy(() -> {
            StringCalculator.separate(userInput);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("구분자가 가장 앞에 위치합니다. 구분자는 숫자 뒤에 위치해야 합니다.");

        assertThatThrownBy(() -> {
            StringCalculator.separate(userInput2);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("구분자가 가장 앞에 위치합니다. 구분자는 숫자 뒤에 위치해야 합니다.");
    }
}
