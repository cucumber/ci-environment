package io.cucumber.createmeta;

import java.util.Arrays;

class CiServers {
    public static final Iterable<CiTemplate> TEMPLATES = Arrays.asList(
<% ciServers.eachWithIndex {name, ci, index-> %>
        new CiTemplate(<%= toJava(name) %>, <%= toJava(ci.url) %>, <%= toJava(ci.buildNumber) %>, new CiTemplate.GitTemplate(<%= toJava(ci.git.remote) %>, <%= toJava(ci.git.revision) %>, <%= toJava(ci.git.branch) %>, <%= toJava(ci.git.tag) %>)) <%= index < ciServers.size() -1 ? "," : "" %>
<% } %>
    );
}
