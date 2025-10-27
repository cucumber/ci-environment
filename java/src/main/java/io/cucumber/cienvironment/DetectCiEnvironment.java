package io.cucumber.cienvironment;

import java.util.Map;
import java.util.Optional;

import static io.cucumber.cienvironment.VariableExpression.evaluate;
import static java.util.Optional.empty;
import static java.util.Optional.of;

public final class DetectCiEnvironment {
    private DetectCiEnvironment() {
    }

    /**
     * Detects the ci system currently in use based on well known environment
     * variables.
     *
     * @param env the environment variables (e.g {@code System.getenv()}
     * @return the detected ci system
     */
    public static Optional<CiEnvironment> detectCiEnvironment(Map<String, String> env) {
        return CiEnvironments.TEMPLATES.stream()
                .map(ciEnvironment -> detect(ciEnvironment, env))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    private static Optional<CiEnvironment> detect(CiEnvironment ci, Map<String, String> env) {
        String url = evaluate(ci.getUrl(), env);
        if (url == null) return empty();

        return of(new CiEnvironmentImpl(
                ci.getName(),
                url,
                ci.getBuildNumber().map(buildNumber -> evaluate(buildNumber, env)).orElse(null),
                detectGit(ci, env)
        ));
    }

    private static CiEnvironmentImpl.Git detectGit(CiEnvironment ci, Map<String, String> env) {
        String revision = evaluateRevision(ci, env);
        if (revision == null) return null;

        if (!ci.getGit().isPresent()) {
            return null;
        }
        CiEnvironment.Git git = ci.getGit().get();

        String remote = evaluate(git.getRemote(), env);
        if (remote == null) return null;

        return new CiEnvironmentImpl.Git(
                RemoveUserInfo.fromUrl(remote),
                revision,
                git.getBranch().map(branch -> evaluate(branch, env)).orElse(null),
                git.getTag().map(tag -> evaluate(tag, env)).orElse(null)
        );
    }

    private static String evaluateRevision(CiEnvironment ci, Map<String, String> env) {
        String revision = GithubEventParser.evaluateRevisionGithub(env);
        if (revision != null) return revision;
        return ci.getGit().map(git -> evaluate(git.getRevision(), env)).orElse(null);
    }

}
