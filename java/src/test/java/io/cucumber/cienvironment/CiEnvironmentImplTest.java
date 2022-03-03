package io.cucumber.cienvironment;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CiEnvironmentImplTest {
    @Test
    void reads_before_property_from_multiline_json() {
        Stream<String> lines = Stream.of(
                "{\n",
                "  \"after\": \"3738117e3337e54955580f4e98cf767d96b42135\",\n",
                "  \"before\": \"06aa815724888e86f37e41e43e07b0ec1bb0ffe1\",",
                "  \"action\": \"synchronize\",\n",
                "}\n"
        );
        String rev = CiEnvironmentImpl.getBeforeProperty(lines);
        assertEquals("06aa815724888e86f37e41e43e07b0ec1bb0ffe1", rev);
    }
}
