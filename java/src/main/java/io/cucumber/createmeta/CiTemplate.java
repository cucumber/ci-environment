package io.cucumber.createmeta;

public class CiTemplate {
    public final String name;
    public final String url;
    public final String buildNumber;
    public final GitTemplate git;

    public CiTemplate(String name, String url, String buildNumber, GitTemplate git) {
        this.name = name;
        this.url = url;
        this.buildNumber = buildNumber;
        this.git = git;
    }

    public static class GitTemplate {
        public final String remote;
        public final String revision;
        public final String branch;
        public final String tag;

        public GitTemplate(String remote, String revision, String branch, String tag) {
            this.remote = remote;
            this.revision = revision;
            this.branch = branch;
            this.tag = tag;
        }
    }
}
