from contextlib import suppress
from re import compile as compile_re
from re import subn


def evaluate(expression, env):
    if expression is None:
        return None
    with suppress(Exception):
        re_pattern = compile_re(r"\$\{(.*?)(?:(?<!\\)/(.*)/(.*))?}")

    def repl(match):
        variable = match.group(1)
        value = get_value(env, variable)
        if value is None:
            raise ValueError
        pattern = match.group(2)
        if pattern is None:
            return value
        regexp = compile_re(subn("\\/", "/", pattern)[0])

        replacement = match.group(3)
        if regexp.match(value):
            return regexp.subn(replacement, value)[0]
        raise ValueError

    try:
        return re_pattern.subn(repl=repl, string=expression)[0]
    except ValueError:
        ...


def get_value(env: dict, variable: str):
    if "*" in variable:
        pattern = compile_re(variable.replace("*", ".*"))
        for name, value in env.items():
            if pattern.match(name):
                return value
    return env.get(variable)
