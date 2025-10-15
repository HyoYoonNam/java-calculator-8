package calculator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
}
