package io.cucumber.cienvironment;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Optional;

public final class DetectCiEnvironment {
    private DetectCiEnvironment() {

    }
    public static Optional<CiEnvironment> detectCiEnvironment(Map<String, String> env) {
        return CiEnvironments.TEMPLATES.stream()
                .map(ciEnvironment -> ciEnvironment.detect(env))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
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
