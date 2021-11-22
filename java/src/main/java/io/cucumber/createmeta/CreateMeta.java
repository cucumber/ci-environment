package io.cucumber.createmeta;

import io.cucumber.messages.ProtocolVersion;
import io.cucumber.messages.types.Ci;
import io.cucumber.messages.types.Git;
import io.cucumber.messages.types.Meta;
import io.cucumber.messages.types.Product;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.cucumber.createmeta.VariableExpression.evaluate;

public final class CreateMeta {
    private CreateMeta(){

    }

    public static Meta createMeta(
            String implementationName,
            String implementationVersion,
            Map<String, String> env
    ) {
        return new Meta(
                ProtocolVersion.getVersion(),
                new Product(implementationName, implementationVersion),
                new Product(System.getProperty("java.vm.name"), System.getProperty("java.vm.version")),
                new Product(System.getProperty("os.name"), null),
                new Product(System.getProperty("os.arch"), null),
                detectCI(env)
        );
    }

    public static String removeUserInfoFromUrl(String value) {
        if (value == null) return null;
        try {
            URI uri = URI.create(value);
            return new URI(uri.getScheme(), null, uri.getHost(), uri.getPort(), uri.getPath(), uri.getQuery(), uri.getFragment()).toASCIIString();
        } catch (URISyntaxException | IllegalArgumentException e) {
            return value;
        }
    }

    static Ci detectCI(Map<String, String> env) {
        List<Ci> detected = new ArrayList<>();
        for (CiTemplate ciTemplate : CiServers.TEMPLATES) {
            Ci ci = createCi(ciTemplate, env);
            if (ci != null) {
                detected.add(ci);
            }
        }
        return detected.size() == 1 ? detected.get(0) : null;
    }

    private static Ci createCi(CiTemplate ciTemplate, Map<String, String> env) {
        String url = evaluate(ciTemplate.url, env);
        if (url == null) return null;
        String buildNumber = evaluate(ciTemplate.buildNumber, env);
        String remote = removeUserInfoFromUrl(evaluate(ciTemplate.git.remote, env));
        String revision = evaluate(ciTemplate.git.revision, env);
        String branch = evaluate(ciTemplate.git.branch, env);
        String tag = evaluate(ciTemplate.git.tag, env);

        return new Ci(
                ciTemplate.name,
                url,
                buildNumber,
                new Git(remote, revision, branch, tag)
        );
    }
}
