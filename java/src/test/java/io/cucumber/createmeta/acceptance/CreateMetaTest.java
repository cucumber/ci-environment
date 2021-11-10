package io.cucumber.createmeta.acceptance;

import io.cucumber.messages.types.Ci;
import io.cucumber.messages.types.Meta;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import static io.cucumber.createmeta.CreateMeta.createMeta;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static java.nio.file.Files.newDirectoryStream;

class CreateMetaTest {
    private final ObjectMapper mapper = new ObjectMapper()
        .enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);

    @Test
    void it_creates_meta_with_right_ci_info() throws IOException, FileNotFoundException{
        List<Path> paths = new ArrayList<>();
        newDirectoryStream(Paths.get("..", "testdata")).forEach(paths::add);

        for(Path path: paths) {
            if (path.toString().endsWith(".txt")) {
                BufferedReader in = new BufferedReader(new FileReader(path.toString()));
                String line;
                HashMap<String, String> env = new HashMap<>();

                while ((line = in.readLine()) != null) {
                    String[] parts = line.split("=");

                    if (parts.length == 1) {
                        env.put(parts[0], "");
                    } else {
                        env.put(parts[0], parts[1]);
                    }
                }

                Meta meta = createMeta("cucumber-jvm", "1.2.3", env);

                Ci expectedCi = mapper.readValue(new File(path.toString() + ".json"), Ci.class);

                assertEquals(expectedCi, meta.getCi());
            }
        }
    }
}
