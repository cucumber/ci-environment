package io.cucumber.cienvironment;

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

}
