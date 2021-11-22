package io.cucumber.createmeta.acceptance;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.messages.types.Ci;
import io.cucumber.messages.types.Meta;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.cucumber.createmeta.CreateMeta.createMeta;
import static java.nio.file.Files.newBufferedReader;
import static java.nio.file.Files.newDirectoryStream;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateMetaTest {
    private static final ObjectMapper mapper = new ObjectMapper()
            .enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);

    private static List<Path> acceptance_tests_pass() throws IOException {
        List<Path> paths = new ArrayList<>();
        newDirectoryStream(Paths.get("..", "testdata"), "*.txt").forEach(paths::add);
        paths.sort(Comparator.naturalOrder());
        return paths;
    }

    @ParameterizedTest
    @MethodSource
    void acceptance_tests_pass(@ConvertWith(Converter.class) Expectation expectation) throws IOException {
        Meta meta = createMeta("cucumber-jvm", "1.2.3", expectation.env);
        assertEquals(expectation.expectedCi, meta.getCi());
    }

    static class Expectation {
        public final Map<String, String> env;
        public final Ci expectedCi;

        Expectation(Map<String, String> env, Ci expectedCi) {
            this.env = env;
            this.expectedCi = expectedCi;
        }
    }

    static class Converter implements ArgumentConverter {
        @Override
        public Expectation convert(Object source, ParameterContext context) throws ArgumentConversionException {
            try {
                Path path = (Path) source;
                Map<String, String> env = new HashMap<>();
                BufferedReader in = newBufferedReader(path);
                String line;
                while ((line = in.readLine()) != null) {
                    String[] parts = line.split("=");

                    if (parts.length == 1) {
                        env.put(parts[0], "");
                    } else {
                        env.put(parts[0], parts[1]);
                    }
                }
                Ci expectedCi = mapper.readValue(new File(path.toString() + ".json"), Ci.class);
                return new Expectation(env, expectedCi);
            } catch (IOException e) {
                throw new ArgumentConversionException("Could not load " + source, e);
            }
        }
    }
}
