package io.cucumber.cienvironment;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RemoveUserInfoTest {

    @Test
    void returns_empty_string_for_empty_string() {
        assertEquals("", RemoveUserInfo.fromUrl(""));
    }

    @Test
    void leaves_the_data_intact_when_no_sensitive_information_is_detected() {
        assertEquals("pretty safe", RemoveUserInfo.fromUrl("pretty safe"));
    }

    @Test
    void with_URLS_leaves_intact_when_no_password_is_found() {
        assertEquals("https://example.com/git/repo.git", RemoveUserInfo.fromUrl("https://example.com/git/repo.git"));
    }

    @Test
    void with_URLS_removes_credentials_when_found() {
        assertEquals("http://example.com/git/repo.git", RemoveUserInfo.fromUrl("http://login@example.com/git/repo.git"));
    }

    @Test
    void with_URLS_removes_credentials_and_passwords_when_found() {
        assertEquals("ssh://example.com/git/repo.git", RemoveUserInfo.fromUrl("ssh://login:password@example.com/git/repo.git"));
    }
}
