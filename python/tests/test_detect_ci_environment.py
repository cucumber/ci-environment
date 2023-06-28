from json import loads
from pathlib import Path

from dotenv import dotenv_values
from pytest import mark

from ci_environment import detect_ci_environment

_data_path = Path(__file__) / ".." / ".." / ".." / "testdata"


@mark.parametrize("envfile", _data_path.glob("*.txt"))
def test_detect_ci_environment(envfile):
    env = dotenv_values(envfile)
    expected = loads((envfile.resolve().parent / f"{envfile.name}.json").read_text())

    assert detect_ci_environment(env) == expected


def test_detect_ci_environment_when_no_environment():
    assert detect_ci_environment({}) is None
