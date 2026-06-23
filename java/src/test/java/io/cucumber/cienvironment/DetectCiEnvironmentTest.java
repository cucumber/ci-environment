package io.cucumber.cienvironment;

import org.assertj.core.api.Assertions;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.cucumber.cienvironment.DetectCiEnvironment.detectCiEnvironment;
import static io.cucumber.cienvironment.Jackson.OBJECT_MAPPER;
import static java.nio.file.Files.newBufferedReader;
import static java.nio.file.Files.newDirectoryStream;
import static java.util.Objects.requireNonNull;

class DetectCiEnvironmentTest {

    static List<Path> acceptance_tests_pass() throws IOException {
        List<Path> paths = new ArrayList<>();
        try (DirectoryStream<Path> testdata = newDirectoryStream(Paths.get("..", "testdata", "src"), "*.txt")) {
            testdata.forEach(paths::add);
        }
        paths.sort(Comparator.naturalOrder());
        return paths;
    }

    @ParameterizedTest
    @MethodSource
    void acceptance_tests_pass(@ConvertWith(Converter.class) Expectation expectation) {
        CiEnvironment ciEnvironment = detectCiEnvironment(expectation.env).orElseThrow(() -> new RuntimeException("Could not detect from env " + expectation.env));
        var actual = OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(ciEnvironment);
        Assertions.assertThat(actual).isEqualToIgnoringNewLines(expectation.getExpected());
    }

    static class Expectation {
        private final Map<String, String> env;
        private final String expected;

        Expectation(Map<String, String> env, String expected) {
            this.env = requireNonNull(env);
            this.expected = requireNonNull(expected);
        }

        public String getExpected() {
            return expected;
        }
    }

    static class Converter implements ArgumentConverter {
        @Override
        public Expectation convert(@Nullable Object source, @Nullable ParameterContext context) throws ArgumentConversionException {
            if (source == null) {
                throw new ArgumentConversionException("Could not convert null");
            }

            Path path = (Path) source;
            Map<String, String> env = new HashMap<>();

            try (BufferedReader in = newBufferedReader(path)) {
                String line;
                while ((line = in.readLine()) != null) {
                    String[] parts = line.split("=", 2);

                    if (parts.length == 1) {
                        env.put(parts[0], "");
                    } else {
                        env.put(parts[0], parts[1]);
                    }
                }
                String expected = Files.readString(Paths.get(path + ".json"));
                return new Expectation(env, expected);
            } catch (IOException e) {
                throw new ArgumentConversionException("Could not load " + source, e);
            }
        }
    }
}
