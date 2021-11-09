package io.cucumber.createmeta.acceptance;

import io.cucumber.messages.types.Ci;
import io.cucumber.messages.types.Git;
import io.cucumber.messages.types.Meta;
import io.cucumber.messages.JSON;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.file.Files;

import static io.cucumber.createmeta.CreateMeta.createMeta;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateMetaTest {
    private final String test_data_dir = "../testdata";

    @Test
    void it_creates_meta_with_right_ci_info() throws IOException {
        Files.list(new File(test_data_dir).toPath())
            .forEach(path -> {
                try {
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
                        assertEquals("", meta.getCi());
                    }
                } catch (FileNotFoundException fnfe) {
                    System.out.println(fnfe.toString());
                } catch (IOException ioe) {
                    System.out.println(ioe.toString());
                }
            });

        // Meta meta = createMeta("cucumber-jvm", "3.2.1", new HashMap<>());
        // assertEquals("cucumber-jvm", meta.getImplementation().getName());
    }
}
