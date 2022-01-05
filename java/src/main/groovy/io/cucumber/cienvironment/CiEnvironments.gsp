package io.cucumber.cienvironment;

import java.util.Collection;

import static java.util.Arrays.asList;

class CiEnvironments {
    static final Collection<CiEnvironmentImpl> TEMPLATES = asList(
<% ciEnvironments.eachWithIndex {ciEnvironment, index-> %>
        new CiEnvironmentImpl(<%= toJava(ciEnvironment.name) %>, <%= toJava(ciEnvironment.url) %>, <%= toJava(ciEnvironment.buildNumber) %>, new CiEnvironmentImpl.Git(<%= toJava(ciEnvironment.git.remote) %>, <%= toJava(ciEnvironment.git.revision) %>, <%= toJava(ciEnvironment.git.branch) %>, <%= toJava(ciEnvironment.git.tag) %>)) <%= index < ciEnvironments.size() -1 ? "," : "" %>
<% } %>
    );
}
