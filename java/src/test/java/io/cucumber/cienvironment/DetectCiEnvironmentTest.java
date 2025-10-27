package io.cucumber.cienvironment;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

class DetectCiEnvironmentTest {

    private static List<Path> acceptance_tests_pass() throws IOException {
        List<Path> paths = new ArrayList<>();
        try  (DirectoryStream<Path> testdata = newDirectoryStream(Paths.get("..", "testdata"), "*.txt")){
            testdata.forEach(paths::add);
        }
        paths.sort(Comparator.naturalOrder());
        return paths;
    }

    @ParameterizedTest
    @MethodSource
    void acceptance_tests_pass(@ConvertWith(Converter.class) Expectation expectation) {
        CiEnvironment ciEnvironment = detectCiEnvironment(expectation.env).orElseThrow(() -> new RuntimeException("Could not detect from env " + expectation.env));
        assertEquals(expectation.getExpected(), ciEnvironment);
    }

    static class Expectation {
        private final Map<String, String> env;
        private final CiEnvironment expected;

        Expectation(Map<String, String> env, CiEnvironment expected) {
            this.env = requireNonNull(env);
            this.expected = requireNonNull(expected);
        }

        public CiEnvironment getExpected() {
            return expected;
        }
    }

    static class Converter implements ArgumentConverter {
        @Override
        public Expectation convert(Object source, ParameterContext context) throws ArgumentConversionException {
            Path path = (Path) source;
            Map<String, String> env = new HashMap<>();

            try (BufferedReader in = newBufferedReader(path)){
                String line;
                while ((line = in.readLine()) != null) {
                    String[] parts = line.split("=");

                    if (parts.length == 1) {
                        env.put(parts[0], "");
                    } else {
                        env.put(parts[0], parts[1]);
                    }
                }
                CiEnvironment expected = OBJECT_MAPPER.readValue(new File(path + ".json"), CiEnvironmentImpl.class);
                return new Expectation(env, expected);
            } catch (IOException e) {
                throw new ArgumentConversionException("Could not load " + source, e);
            }
        }
    }
}
