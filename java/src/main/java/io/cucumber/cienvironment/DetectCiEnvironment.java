package io.cucumber.cienvironment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public final class DetectCiEnvironment {
    private DetectCiEnvironment() {
    }

    public static Optional<CiEnvironment> detectCiEnvironment(Map<String, String> env) {
        return detectCiEnvironment(env, (path) -> {
            try {
                return Files.lines(path);
            } catch (IOException e) {
                throw new RuntimeException("Couldn't read " + path, e);
            }
        });
    }

    public static Optional<CiEnvironment> detectCiEnvironment(Map<String, String> env, Function<Path, Stream<String>> getLines) {
        return CiEnvironments.TEMPLATES.stream()
                .map(ciEnvironment -> ciEnvironment.detect(env, getLines))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

}
