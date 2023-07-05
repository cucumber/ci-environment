from collections import namedtuple
from contextlib import suppress
from functools import partial, wraps
from itertools import filterfalse
from json import loads as loads_json
from operator import is_
from pathlib import Path
from pprint import pformat
from typing import Dict
from urllib.parse import urlparse

from importlib_resources import as_file, files

from ci_environment.variable_expression import evaluate

_key_value = namedtuple("_key_value", ["key", "value"])


@wraps(dict)
def _opt_dict(*args, **kwargs) -> Dict:
    return {k: v for k, v in dict(*args, **kwargs).items() if v is not None}


def _flip(func):
    def wrapped(*args, **kwargs):
        if len(args) > 1:
            first, *other, last = args
            return func(last, *other, first, **kwargs)
        else:
            return func(*args, **kwargs)

    return wrapped


def detect_ci_environment(env: Dict[str, str]):
    with as_file(files("ci_environment").joinpath("CiEnvironments.json")) as path:
        ci_environments = loads_json(Path(path).read_text())

    return next(filterfalse(partial(_flip(is_), None), map(partial(detect, env=env), ci_environments)), None)


def detect(ci_environment, env: Dict[str, str]):
    if (url := evaluate(ci_environment["url"], env)) is not None:
        return dict(
            name=ci_environment["name"],
            url=url,
            buildNumber=evaluate(ci_environment["buildNumber"], env),
            **_opt_dict(git=detect_git(ci_environment, env)),
        )


def detect_git(ci_environment, env: Dict[str, str]):
    if all(
        [
            (revision := detect_revision(ci_environment, env)) is not None,
            (remote := evaluate(ci_environment["git"]["remote"], env)) is not None,
        ]
    ):
        return dict(
            remote=remove_userinfo_from_url(remote),
            revision=revision,
            **_opt_dict(
                tag=evaluate(ci_environment.get("git", {}).get("tag"), env),
                branch=evaluate(ci_environment.get("git", {}).get("branch"), env),
            ),
        )


def detect_revision(ci_environment, env: Dict[str, str]):
    if env.get("GITHUB_EVENT_NAME") == "pull_request":
        try:
            event = loads_json(Path(env["GITHUB_EVENT_PATH"]).read_text())
        except KeyError as e:
            raise ValueError from e
        try:
            revision = event["pull_request"]["head"]["sha"]
        except KeyError as e:
            raise ValueError(pformat(event)) from e
        else:
            return revision
    else:
        return evaluate(ci_environment["git"]["revision"], env)


def remove_userinfo_from_url(remote):
    if remote is not None:
        with suppress(Exception):
            return (parsed_url := urlparse(remote))._replace(netloc=parsed_url.hostname).geturl()
        return remote
