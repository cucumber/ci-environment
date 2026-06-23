package io.cucumber.cienvironment;

import tools.jackson.core.StreamWriteFeature;
import tools.jackson.core.util.DefaultPrettyPrinter;
import tools.jackson.core.util.Separators;
import tools.jackson.databind.cfg.ConstructorDetector;
import tools.jackson.databind.json.JsonMapper;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_ABSENT;

final class Jackson {
    public static final JsonMapper OBJECT_MAPPER = JsonMapper.builder()
            .changeDefaultPropertyInclusion(value -> value
                    .withContentInclusion(NON_ABSENT)
                    .withValueInclusion(NON_ABSENT)
            )
            .constructorDetector(ConstructorDetector.USE_PROPERTIES_BASED)
            .disable(StreamWriteFeature.AUTO_CLOSE_TARGET)
            .defaultPrettyPrinter(new DefaultPrettyPrinter()
                    .withSeparators(Separators.createDefaultInstance()
                    .withObjectNameValueSpacing(Separators.Spacing.AFTER)))
            .build();

    private Jackson() {
    }
}
