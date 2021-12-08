# Architecture

## CI definitions

The `CiEnvironment.json` file contains an array of `CiEnvironment` structs for
all the supported CI servers. Each property of a `CiEnvironment` struct
is an expression that evaluates a value from one or more environment variables.

The expression syntax for environment variables can use the form `${variable/pattern/replacement}`,
similar to [bash parameter substitution](https://tldp.org/LDP/abs/html/parameter-substitution.html),
but inspired from [sed's s command](https://www.gnu.org/software/sed/manual/html_node/The-_0022s_0022-Command.html)
which provides support for capture group back-references in the replacement.
