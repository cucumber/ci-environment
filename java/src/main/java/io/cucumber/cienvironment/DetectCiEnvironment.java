package io.cucumber.cienvironment;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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
        String revision = evaluateRevisionGithub(env);
        if (revision != null) return revision;
        return ci.getGit().map(git -> evaluate(git.getRevision(), env)).orElse(null);
    }

    /*
     * Evaluate the current revision on GitHub.
     *
     * The GITHUB_SHA environment variable doesn't quite work as expected.
     * See:
     *  * https://github.com/cucumber/ci-environment/issues/67
     *  * https://github.com/orgs/community/discussions/26325
     *  * https://github.com/cucumber/ci-environment/issues/86
     */
    private static String evaluateRevisionGithub(Map<String, String> env) {
        if (!"pull_request".equals(env.get("GITHUB_EVENT_NAME"))) {
            return null;
        }
        if (env.get("GITHUB_EVENT_PATH") == null) {
            throw new RuntimeException("GITHUB_EVENT_PATH not set");
        }
        try (InputStreamReader is = getGithubEvent(env)) {
            JsonValue event = Json.parse(is);
            return event.asObject().get("pull_request").asObject().get("head").asObject().get("sha").asString();
        } catch (Exception e) {
            throw new RuntimeException("Could not read .pull_request.head.sha from " + env.get("GITHUB_EVENT_PATH"), e);
        }
    }

    private static InputStreamReader getGithubEvent(Map<String, String> env) throws FileNotFoundException {
        return new InputStreamReader(new FileInputStream(env.get("GITHUB_EVENT_PATH")), StandardCharsets.UTF_8);
    }

}
