package io.cucumber.cienvironment;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CiEnvironmentImplTest {
    @Test
    void reads_after_property_from_multiline_json() {
        Function<Path, Stream<String>> getLines = (Path path) -> Stream.of(
                "{\n",
                "  \"after\": \"3738117e3337e54955580f4e98cf767d96b42135\",\n",
                "  \"before\": \"06aa815724888e86f37e41e43e07b0ec1bb0ffe1\",\n",
                "  \"action\": \"synchronize\"\n",
                "}\n"
        );
        String rev = CiEnvironmentImpl.getJsonProperty(Paths.get("anywhere"), getLines, "after");
        assertEquals("3738117e3337e54955580f4e98cf767d96b42135", rev);
    }

    @Test
    void reads_before_property_from_multiline_json() {
        Function<Path, Stream<String>> getLines = (Path path) -> Stream.of(
                "{\n",
                "  \"after\": \"3738117e3337e54955580f4e98cf767d96b42135\",\n",
                "  \"before\": \"06aa815724888e86f37e41e43e07b0ec1bb0ffe1\",\n",
                "  \"action\": \"synchronize\"\n",
                "}\n"
        );
        String rev = CiEnvironmentImpl.getJsonProperty(Paths.get("anywhere"), getLines, "before");
        assertEquals("06aa815724888e86f37e41e43e07b0ec1bb0ffe1", rev);
    }

    @Test
    void throws_detailed_error_message_when_event_lacks_property() {
        Function<Path, Stream<String>> getLines = (Path path) -> Stream.of(
                "{\n",
                "  \"xafter\": \"3738117e3337e54955580f4e98cf767d96b42135\",\n",
                "  \"before\": \"06aa815724888e86f37e41e43e07b0ec1bb0ffe1\",\n",
                "  \"action\": \"synchronize\"\n",
                "}\n"
        );
        String expected = "No after property in event.json:\n" +
                "{\n" +
                "  \"xafter\": \"3738117e3337e54955580f4e98cf767d96b42135\",\n" +
                "  \"before\": \"06aa815724888e86f37e41e43e07b0ec1bb0ffe1\",\n" +
                "  \"action\": \"synchronize\",\n" +
                "}\n";
        assertThrows(RuntimeException.class, () -> CiEnvironmentImpl.getJsonProperty(Paths.get("event.json"), getLines, "after"), expected);
    }
}
