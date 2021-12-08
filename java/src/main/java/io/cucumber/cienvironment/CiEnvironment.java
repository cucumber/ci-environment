package io.cucumber.cienvironment;

public interface CiEnvironment {
    String getName();
    String getUrl();
    String getBuildNumber();
    Git getGit();

    interface Git {
        String getRemote();
        String getRevision();
        String getBranch();
        String getTag();
    }
}
