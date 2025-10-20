package calculator;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StringCalculatorTest {

    @Test
    @DisplayName("기본 구분자만 있는 입력의 합을 리턴할 수 있다")
    void calculate_user_input_with_basic_delimiters() {
        // given
        String userInput = "1,2:3";

        // when
        int result = StringCalculator.calculate(userInput);

        // then
        assertThat(result).isEqualTo(6);
    }

    @Test
    @DisplayName("빈 문자열이 입력되는 경우 0을 리턴한다")
    void when_user_input_is_zero_then_return_zero() {
        // given
        String userInput = "";

        // when
        int result = StringCalculator.calculate(userInput);

        // then
        assertThat(result).isEqualTo(0);
    }

    @Test
    @DisplayName("커스텀 구분자가 있는 입력의 합을 리턴할 수 있다")
    void calculate_user_input_with_custom_delimiters() {
        // given
        String userInput = "//;\\n1,2:3;4";

        // when
        int result = StringCalculator.calculate(userInput);

        // then
        assertThat(result).isEqualTo(10);
    }
}
