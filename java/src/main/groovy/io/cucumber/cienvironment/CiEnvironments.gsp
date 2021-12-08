package io.cucumber.cienvironment;

import static java.util.Arrays.asList;

class CiEnvironments {
    public static final Iterable<CiEnvironmentImpl> TEMPLATES = asList(
<% ciEnvironments.eachWithIndex {ciEnvironment, index-> %>
        new CiEnvironmentImpl(<%= toJava(ciEnvironment.name) %>, <%= toJava(ciEnvironment.url) %>, <%= toJava(ciEnvironment.buildNumber) %>, new CiEnvironmentImpl.Git(<%= toJava(ciEnvironment.git.remote) %>, <%= toJava(ciEnvironment.git.revision) %>, <%= toJava(ciEnvironment.git.branch) %>, <%= toJava(ciEnvironment.git.tag) %>)) <%= index < ciEnvironments.size() -1 ? "," : "" %>
<% } %>
    );
}
