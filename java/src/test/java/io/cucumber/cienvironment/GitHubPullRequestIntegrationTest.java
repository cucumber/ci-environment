package io.cucumber.cienvironment;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static io.cucumber.cienvironment.DetectCiEnvironment.detectCiEnvironment;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GitHubPullRequestIntegrationTest {
    
    @Test
    @EnabledIfEnvironmentVariable(named = "GITHUB_EVENT_NAME", matches = "pull_request", disabledReason = "Must be tested by creating a pull request on Github")
    void detects_the_correct_revision_for_pull_requests() throws IOException {
        Map<String, String> env = System.getenv();
        assertEquals(parsePullRequestHeadShaWithJackson(env), parseRevisionWithRegularExpression(env));
    }

    private static String parseRevisionWithRegularExpression(Map<String, String> env) {
        return detectCiEnvironment(env)
                .orElseThrow(() -> new RuntimeException("No CI environment detected"))
                .getGit().map(CiEnvironment.Git::getRevision)
                .orElseThrow(() -> new RuntimeException("No Github Event detected"));
    }

    private static String parsePullRequestHeadShaWithJackson(Map<String, String> env) throws IOException {
        File file = new File(env.get("GITHUB_EVENT_PATH"));
        JsonNode event = Jackson.OBJECT_MAPPER.readTree(file);
        return event.get("pull_request").get("head").get("sha").textValue();
    }
}
