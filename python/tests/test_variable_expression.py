from ci_environment.variable_expression import evaluate


def test_evaluate_returns_undefined_when_a_variable_iws_undefined():
    expression = "hello-${SOME_VAR}"
    result = evaluate(expression, {})
    assert result is None


def test_evaluate_gets_a_value_without_replacement():
    expression = "${SOME_VAR}"
    result = evaluate(expression, {"SOME_VAR": "some_value"})
    assert result == "some_value"


def test_evaluate_captures_a_group():
    expression = "${SOME_REF/refs\\/heads\\/(.*)/\\1}"
    result = evaluate(expression, {"SOME_REF": "refs/heads/main"})
    assert result == "main"


def test_evaluate_works_with_star_wildcard_in_var():
    expression = "${GO_SCM_*_PR_BRANCH/.*:(.*)/\\1}"
    result = evaluate(
        expression,
        {
            "GO_SCM_MY_MATERIAL_PR_BRANCH": "ashwankthkumar:feature-1",
        },
    )
    assert result == "feature-1"


def test_evaluate_evaluates_a_complex_expression():
    expression = "hello-${VAR1}-${VAR2/(.*) (.*)/\\2-\\1}-world"
    result = evaluate(
        expression,
        {
            "VAR1": "amazing",
            "VAR2": "gorgeous beautiful",
        },
    )
    assert result == "hello-amazing-beautiful-gorgeous-world"
