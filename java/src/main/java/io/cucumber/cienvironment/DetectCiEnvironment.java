package io.cucumber.cienvironment;

import java.util.Map;
import java.util.Optional;

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
                .map(ciEnvironment -> ciEnvironment.detect(env))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

}
