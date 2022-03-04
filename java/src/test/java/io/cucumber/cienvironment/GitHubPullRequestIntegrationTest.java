package io.cucumber.cienvironment;

import org.junit.jupiter.api.Test;

import static io.cucumber.cienvironment.DetectCiEnvironment.detectCiEnvironment;

class GitHubPullRequestIntegrationTest {
    @Test
    void detects_the_correct_revision_for_pull_requests() {
        if ("pull_request".equals(System.getenv().get("GITHUB_EVENT_NAME"))) {
            CiEnvironment ciEnvironment = detectCiEnvironment(System.getenv()).orElseThrow(() -> new RuntimeException("No CI environment detected"));
            System.out.println("Manually verify that the revision is correct");
            System.out.println(ciEnvironment);
        }
    }
}
