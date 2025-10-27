package io.cucumber.cienvironment;

import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

final class CiEnvironmentImpl implements CiEnvironment {
    public String name;
    public String url;
    public String buildNumber;
    public Git git;

    CiEnvironmentImpl(String name, String url, String buildNumber, Git git) {
        this.name = requireNonNull(name);
        this.url = requireNonNull(url);
        this.buildNumber = buildNumber;
        this.git = git;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public Optional<String> getBuildNumber() {
        return ofNullable(buildNumber);
    }

    @Override
    public Optional<CiEnvironment.Git> getGit() {
        return ofNullable(git);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CiEnvironmentImpl that = (CiEnvironmentImpl) o;
        return Objects.equals(name, that.name) && Objects.equals(url, that.url) && Objects.equals(buildNumber, that.buildNumber) && Objects.equals(git, that.git);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url, buildNumber, git);
    }

    @Override
    public String toString() {
        return "CiEnvironmentImpl{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", buildNumber='" + buildNumber + '\'' +
                ", git=" + git +
                '}';
    }

    final static class Git implements CiEnvironment.Git {
        public String remote;
        public String revision;
        public String branch;
        public String tag;

        Git(String remote, String revision, String branch, String tag) {
            this.remote = requireNonNull(remote);
            this.revision = requireNonNull(revision);
            this.branch = branch;
            this.tag = tag;
        }

        @Override
        public String getRemote() {
            return remote;
        }

        @Override
        public String getRevision() {
            return revision;
        }

        @Override
        public Optional<String> getBranch() {
            return ofNullable(branch);
        }

        @Override
        public Optional<String> getTag() {
            return ofNullable(tag);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Git git = (Git) o;
            return Objects.equals(remote, git.remote) && Objects.equals(revision, git.revision) && Objects.equals(branch, git.branch) && Objects.equals(tag, git.tag);
        }

        @Override
        public int hashCode() {
            return Objects.hash(remote, revision, branch, tag);
        }

        @Override
        public String toString() {
            return "Git{" +
                    "remote='" + remote + '\'' +
                    ", revision='" + revision + '\'' +
                    ", branch='" + branch + '\'' +
                    ", tag='" + tag + '\'' +
                    '}';
        }
    }
}
