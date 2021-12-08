package io.cucumber.createmeta;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RemoveUserInfoFromUrlTest {
    @Test
    void returns_null_for_null() {
        assertNull(DetectCiEnvironment.removeUserInfoFromUrl(null));
    }

    @Test
    void returns_empty_string_for_empty_string() {
        assertEquals("", DetectCiEnvironment.removeUserInfoFromUrl(""));
    }

    @Test
    void leaves_the_data_intact_when_no_sensitive_information_is_detected() {
        assertEquals("pretty safe", DetectCiEnvironment.removeUserInfoFromUrl("pretty safe"));
    }

    @Test
    void with_URLS_leaves_intact_when_no_password_is_found() {
        assertEquals("https://example.com/git/repo.git", DetectCiEnvironment.removeUserInfoFromUrl("https://example.com/git/repo.git"));
    }

    @Test
    void with_URLS_removes_credentials_when_found() {
        assertEquals("http://example.com/git/repo.git", DetectCiEnvironment.removeUserInfoFromUrl("http://login@example.com/git/repo.git"));
    }

    @Test
    void with_URLS_removes_credentials_and_passwords_when_found() {
        assertEquals("ssh://example.com/git/repo.git", DetectCiEnvironment.removeUserInfoFromUrl("ssh://login:password@example.com/git/repo.git"));
    }
}
