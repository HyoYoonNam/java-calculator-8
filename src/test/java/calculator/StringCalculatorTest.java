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

    @Test
    @DisplayName("사용자의 잘못된 입력에 대한 예외가 Separator에서 Calculator로 바르게 전달된다")
    // 예외가 전파되는 것만 확인하면 되므로 Separator에서 테스트한 모든 예외 상황에 대해서 중복적으로 할 필요는 없음
    void 예외_전파_테스트() {
        // given
        String userInput = "//1\\n11 2, 3";

        // when and then
        assertThatThrownBy(() -> {
            StringSeparator.separate(userInput);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("커스텀 구분자에는 숫자를 지정할 수 없습니다.");
    }
}
