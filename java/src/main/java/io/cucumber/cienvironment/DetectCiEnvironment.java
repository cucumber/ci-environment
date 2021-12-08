package io.cucumber.cienvironment;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public final class DetectCiEnvironment {
    private DetectCiEnvironment() {

    }

    public static CiEnvironment detectCiEnvironment(Map<String, String> env) {
        for (CiEnvironmentImpl ciTemplate : CiEnvironments.TEMPLATES) {
            CiEnvironment detected = ciTemplate.detect(env);
            if (detected != null) {
                return detected;
            }
        }
        return null;
    }

    static String removeUserInfoFromUrl(String value) {
        if (value == null) return null;
        try {
            URI uri = URI.create(value);
            return new URI(uri.getScheme(), null, uri.getHost(), uri.getPort(), uri.getPath(), uri.getQuery(), uri.getFragment()).toASCIIString();
        } catch (URISyntaxException | IllegalArgumentException e) {
            return value;
        }
    }
}
