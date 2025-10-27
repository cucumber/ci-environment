package io.cucumber.cienvironment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class GithubEventParser {

    /*
     * Evaluate the current revision on GitHub.
     *
     * The GITHUB_SHA environment variable doesn't quite work as expected.
     * See:
     *  * https://github.com/cucumber/ci-environment/issues/67
     *  * https://github.com/orgs/community/discussions/26325
     *  * https://github.com/cucumber/ci-environment/issues/86
     */
    static String evaluateRevisionGithub(Map<String, String> env) {
        if (!"pull_request".equals(env.get("GITHUB_EVENT_NAME"))) {
            return null;
        }
        if (env.get("GITHUB_EVENT_PATH") == null) {
            return null;
        }
        try {
            Path path = Paths.get(env.get("GITHUB_EVENT_PATH"));
            String event = String.join(" ", Files.readAllLines(path));
            return parsePullRequestHeadSha(event);
        } catch (IOException e) {
            return null;
        }
    }

    private static final Pattern GITHUB_EVENT_PATTERN = Pattern.compile(
            // Start of object
            "^\\{" +
                    // Any leading key-value pairs.
                    ".*?" +
                    // Pull request key
                    "\"pull_request\" *: *" +
                    // Start of pull request value, must be an object
                    "\\{" +
                    // Any leading key-value pairs.
                    ".*?" +
                    // Head key
                    "\"head\" *: *" +
                    // Start of head value, must be an object
                    "\\{" +
                    // Any leading key-value pairs.
                    ".*?" +
                    // sha key and value, must be a hash-like string 
                    "\"sha\" *: *\"([a-z0-9]+)\"" +
                    // Any key-value pairs.
                    ".*" +
                    // End of head value.
                    "}" +
                    // Any trailing key-value pairs.
                    ".*" +
                    // End of pull request value
                    "}" +
                    // Any trailing key-value pairs.
                    ".*" +
                    // End of object
                    "}$");

    static String parsePullRequestHeadSha(String eventJson) {
        // Parse json using regex. Not ideal but works for the limited input.
        Matcher matcher = GITHUB_EVENT_PATTERN.matcher(eventJson.trim());
        if (!matcher.matches()) {
            return null;
        }
        return matcher.group(1);
    }


}
