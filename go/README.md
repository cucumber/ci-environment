# CI-Environment: Go

This directory contains the Go implementation of the ci-environment implementation.

## Template Locations
To embed the templates for the ci-environment go module, we'll use go:embed to include CIEnvironments.json. Since go:embed does not support paths which include `..`, we can't just use a path the the CIEnvironments.json file in the root of the git repository. So a Makefile is included in this go module which will handle copying the most recent template from the root of the git repository to the go directory. The CiEnvironments.json file contains the templates to map available environment variables to values in templates for the variety of supported CI enviroments.

## Template Expressions

### Simple Environment Variables
Many are simple replacements of environment variables. E.g, for Azure DevOps, the URI is simply the value of the `${BUILD_BUILDURI}` environment variable.

### Multiple Environment Variables and/or Constants
For others, it is a combination of multiple enviroment variables and constants. E.g., for Github Actions, the URI is the combination of `${GITHUB_SERVER_URL}/${GITHUB_REPOSITORY}/actions/runs/${GITHUB_RUN_ID}`.

### Expression Syntax
Still for others, there is some additional processing of values that can be found in environment variables. In these cases, the value in the template consists of three parts: the environment variable to be used, the regex expression (including capture groups) to to used to derive the final value, and the template to be applied using the value(s) from those
capture groups.

E.g., the Git remote template for CodeShip has a value of `${CI_PULL_REQUEST/(.*)\\/pull\\/\\d+/\\1.git}`. The sections of this expression are delineated by unescaped slashes.  So the three parts are the `CI_PULL_REQUEST` environment variable, the regex of `(.*)\\/pull\\/\\d+`, and the template of `\\1.git`.

So in this example, the URL in the CI_PULL_REQUEST environment variable will have everything prior to `pull/\d+` in a capture group, then the first (and only in this example) capture group is prepended to the literal `.git`. So if the CI_PULL_REQUEST envrionment variable were `https://github.com/owner/repo/pull/42`, then the value of `https://github.com/owner/repo` would be part of that first capture group, and then the resulting value will be `https://github.com/owner/repo.git`.

Or as explained in ARCHITECTURE.md:
>The expression syntax for environment variables can use the form `${variable/pattern/replacement}`, similar to [bash parameter substitution](https://tldp.org/LDP/abs/html/parameter-substitution.html), but inspired from [sed's s command](https://www.gnu.org/software/sed/manual/html_node/The-_0022s_0022-Command.html) which provides support for capture group back-references in the replacement.

### Wildcards in Environment Variable Names
In some expressions, an environment variable name can contain a wildcard, e.g., GoCD. The git branch is derived using `GO_SCM_*_PR_BRANCH`. In this situation, the environment variable name contains the branch name (e.g., GO_SCM_MY_MATERIAL_PR_BRANCH where the branch name is MY_MATERIAL) and is thus not a constant. So the wildcard is used as a placeholder and when evaluating the expression, we must loop through all the environment variables until we find the one that matches the pattern.

## Why Parse the  Template Expressions?
In other implementations, such as the javascript implementation for ci-environments, a regex can be used when evaluating the variable/pattern/replacement expressions. An example regex is `'\\${(.*?)(?:(?<!\\\\)/(.*)/(.*))?}'`.  However, for Go, the [Google/re2](https://github.com/google/re2/wiki/Syntax) syntax does not support `after text not matching` expressions in the form of `(?<!re)`. So a quick and simple parser was put together to evaluate these expressions in lieu of using regex.
