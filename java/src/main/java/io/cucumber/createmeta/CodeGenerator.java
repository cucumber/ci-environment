package io.cucumber.createmeta;

import io.cucumber.messages.internal.com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.cucumber.messages.internal.com.fasterxml.jackson.databind.DeserializationFeature;
import io.cucumber.messages.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.messages.internal.com.fasterxml.jackson.databind.type.MapType;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;
import java.util.SortedMap;
import java.util.stream.Collectors;

// https://newbedev.com/generating-sources-by-running-a-project-s-java-class-in-maven
public class CodeGenerator {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CiDict {
        public String url;
        public String buildNumber;
        public Git git;
    }

    public static class Git {
        public String remote;
        public String revision;
        public String branch;
        public String tag;
    }

    private static final String CLASS_FORMAT = "package io.cucumber.createmeta;\n" +
            "\n" +
            "import java.util.Arrays;\n" +
            "\n" +
            "public class CiTemplates '{'\n" +
            "    public static final Iterable<? extends CiTemplate> TEMPLATES = Arrays.asList(\n" +
            "    {0}\n" +
            "    );\n" +
            "'}'";

    private static final String CI_TEMPLATE_FORMAT = "new CiTemplate({0}, {1}, {2}, new CiTemplate.GitTemplate({3}, {4}, {5}, {6}))";

    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper()
                .enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        MapType type = mapper.getTypeFactory().constructMapType(SortedMap.class, String.class, CiDict.class);
        Map<String, CiDict> cis = mapper.readValue(new File("../ciDict.json"), type);

        String ciCode = cis.entrySet().stream().map(entry -> MessageFormat.format(CI_TEMPLATE_FORMAT,
                toJava(entry.getKey()),
                toJava(entry.getValue().url),
                toJava(entry.getValue().buildNumber),
                toJava(entry.getValue().git.remote),
                toJava(entry.getValue().git.revision),
                toJava(entry.getValue().git.branch),
                toJava(entry.getValue().git.tag)
        )).collect(Collectors.joining(",\n"));

        String sourceCode = MessageFormat.format(CLASS_FORMAT, ciCode);
        System.out.println(sourceCode);
    }

    private static String toJava(String s) {
        if(s == null) return "null";
        return '"' + s.replace("\\", "\\\\") + '"';
    }
}
