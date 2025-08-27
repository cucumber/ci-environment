import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.Configuration;
import freemarker.template.SimpleScalar;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.nio.file.Files.newBufferedReader;
import static java.nio.file.Files.newBufferedWriter;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

/*
 * This class generates the CiEnvironments class using the FreeMarker
 * template engine and provided templates.
 */
public class Generate {

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            throw new IllegalArgumentException("Usage: <baseDirectory> <packagePath>");
        }
        String baseDirectory = args[0];
        String packagePath = args[1];
        Generate.generateCiEnvironments(baseDirectory, packagePath);
    }

    static void generateCiEnvironments(String baseDirectory, String packagePath) throws Exception {
        Path path = Paths.get(baseDirectory, packagePath, "CiEnvironments.java");

        Template dialectsSource = readTemplate();

        Map<String, Object> binding = new LinkedHashMap<>();
        binding.put("ciEnvironments", readCiEnvironments());
        binding.put("toJava", new ToJava());

        try {
            Files.createDirectories(path.getParent());
            dialectsSource.process(binding, newBufferedWriter(path, CREATE, TRUNCATE_EXISTING));
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    private static Template readTemplate() throws IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_21);
        cfg.setClassForTemplateLoading(Generate.class, "templates");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setLocale(Locale.US);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        return cfg.getTemplate("ci-environments.java.ftl");
    }

    private static List<Object> readCiEnvironments() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<Object>> mapObjectType = new TypeReference<List<Object>>() {};
        try (Reader reader = newBufferedReader(Paths.get("../CiEnvironments.json"))) {
            return objectMapper.readValue(reader, mapObjectType);
        }
    }

    private static class ToJava implements TemplateMethodModelEx {

        @Override
        public Object exec(List args) throws TemplateModelException {
            if (args.size() != 1) {
                throw new TemplateModelException("Wrong number of arguments");
            }

            SimpleScalar arg = (SimpleScalar) args.get(0);
            if (arg == null) {
                return "null";
            }
            String value = arg.getAsString();
            return "\"" + value.replace("\\", "\\\\") + "\"";
        }
    }
}
