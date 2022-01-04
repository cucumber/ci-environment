package io.cucumber.cienvironment;

import java.util.Optional;

public interface CiEnvironment {
    String getName();
    String getUrl();
    Optional<String> getBuildNumber();
    Optional<Git> getGit();

    interface Git {
        String getRemote();
        String getRevision();
        Optional<String> getBranch();
        Optional<String> getTag();
    }
}
