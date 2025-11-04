package io.cucumber.cienvironment;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static io.cucumber.cienvironment.VariableExpression.evaluate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class VariableExpressionTest {
    @Test
    public void it_returns_null_when_a_variable_is_undefined() {
        String expression = "hello-${SOME_VAR}";
        String result = evaluate(expression, Collections.emptyMap());
        assertNull(result);
    }

    @Test
    public void it_gets_a_value_without_replacement() {
        String expression = "${SOME_VAR}";
        String result = evaluate(expression, Map.of("SOME_VAR", "some_value"));
        assertEquals("some_value", result);
    }
    @Test
    public void it_escapes_a_value_without_replacement() {
        String expression = "${SOME_VAR}";
        String result = evaluate(expression, Map.of("SOME_VAR", "${SOME_VAR}"));
        assertEquals("${SOME_VAR}", result);
    }

    @Test
    public void it_captures_a_group() {
        String expression = "${SOME_REF/refs\\/heads\\/(.*)/\\1}";
        String result = evaluate(expression, Map.of("SOME_REF", "refs/heads/main"));
        assertEquals("main", result);
    }

    @Test
    public void it_works_with_star_wildcard_in_var() {
        String expression = "${GO_SCM_*_PR_BRANCH/.*:(.*)/\\1}";
        String result = evaluate(expression, Map.of("GO_SCM_MY_MATERIAL_PR_BRANCH", "ashwankthkumar:feature-1"));
        assertEquals("feature-1", result);
    }

    @Test
    public void it_evaluates_a_complex_expression() {
        String expression = "hello-${VAR1}-${VAR2/(.*) (.*)/\\2-\\1}-world";
        String result = evaluate(expression, Map.of(
                "VAR1", "amazing",
                "VAR2", "gorgeous beautiful"
        ));
        assertEquals("hello-amazing-beautiful-gorgeous-world", result);
    }
    @Test
    public void it_escapes_a_complex_expression() {
        String expression = "hello-${VAR1}-${VAR2/(.*) (.*)/\\2-\\1}-world";
        String result = evaluate(expression, Map.of(
            "VAR1", "${VAR1}",
            "VAR2", "${VAR2a} ${VAR2b}"
        ));
        assertEquals("hello-${VAR1}-${VAR2b}-${VAR2a}-world", result);
    }
}
