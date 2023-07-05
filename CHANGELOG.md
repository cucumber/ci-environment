# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

## [Unreleased]
- [Python] added ci-environment implementation in Python

## [9.2.0] - 2023-04-28
### Added
- [Java] Enabled reproducible builds
- Added support for [JetBrains Space](https://www.jetbrains.com/help/space/automation-parameters.html#use-provided-parameters), [#205](https://github.com/cucumber/ci-environment/pull/205)

## [9.1.0] - 2022-07-27
### Added
- [Go] added ci-environment implementation in Go

### Fixed
- [Java] Escape regex replacement patterns in environment variables ([#156](https://github.com/cucumber/ci-environment/issues/156), [#157](https://github.com/cucumber/ci-environment/pull/157))

## [9.0.4] - 2022-03-06
### Fixed
- [Java] Make `DetectCiEnvironment.detectCiEnvironment` public

## [9.0.3] - 2022-03-04
### Fixed
- Handle GitHub Action `opened` as well as `synchronize` event (and any other `pull_request` event with `.pull_request.head.sha` in the event payload) ([#86](https://github.com/cucumber/ci-environment/issues/86), [#87](https://github.com/cucumber/ci-environment/pull/87))

## [9.0.2] - 2022-03-04
### Fixed
- Correctly determine GitHub branch from `GITHUB_HEAD_REF` instead of `GITHUB_REF` ([#85](https://github.com/cucumber/ci-environment/pull/85))
- Better error message if revision cannot be determined from GitHub pull request ([#85](https://github.com/cucumber/ci-environment/pull/85))

## [9.0.1] - 2022-03-04
### Fixed
- Provide correct SHA for GitHub Actions ([#67](https://github.com/cucumber/ci-environment/issues/67), [#69](https://github.com/cucumber/ci-environment/pull/69))

## [9.0.0] - 2022-01-25
### Added
- [JavaScript] export `Env`, `Git`, `CiEnvironment` and `CiEnvironments` types

### Changed
- [JavaScript] make `buildNumber` optional ([#51](https://github.com/cucumber/ci-environment/pull/51))
- [Java] use `Optional<T>` for optional values ([#50](https://github.com/cucumber/ci-environment/pull/50))

## [8.1.0] - 2022-01-02
### Added
- [JavaScript] - package as hybrid esm/commonjs module ([#47](https://github.com/cucumber/ci-environment/pull/47))

## [8.0.1] - 2021-12-29
### Fixed
- [Ruby] `detect_ci_environment` was not returning `nil` if no CI had been detected

## [8.0.0] - 2021-12-15
### Changed
- Make `git` optional, make `git.revision` and `git.remote` mandatory ([#37](https://github.com/cucumber/ci-environment/pull/37))

## [7.0.1] - 2021-12-08
### Fixed
- Fix Ruby release

## [7.0.0] - 2021-12-08
### Changed
- This library has been renamed to `ci-environment` and no longer depends on Cucumber `messages`.
([PR#34](https://github.com/cucumber/ci-environment/pull/34))

## [6.0.4] - 2021-11-16
### Fixed
- Update links in descriptor files to point to the new repository at https://github.com/cucumber/ci-environment
- [Ruby][Java] Fix ciDict.json was missing from the Ruby Gem and the Java Artifact
([PR#13](https://github.com/cucumber/ci-environment/pull/13))

## [6.0.3] - 2021-11-15
### Fixed
- [JavaScript] Removed tag property from git object if it is undefined
([PR#4](https://github.com/cucumber/ci-environment/pull/4))

## [6.0.2] - 2021-10-18
### Fixed
- [Ruby] Generate cucumber messages rather than plain ruby hashes
([PR#1772](https://github.com/cucumber/common/pull/1772))

## [6.0.1] - 2021-07-19
### Changed
- Upgrade `cucumber-messages` to v17.0.1

## [6.0.0] - 2021-07-08
### Added
- Detect and populate `Ci#buildNumber` for all supported CI servers.
([#1632](https://github.com/cucumber/common/pull/1632)
[#1606](https://github.com/cucumber/common/issues/1606)
[aslakhellesoy](https://github.com/aslakhellesoy))

### Changed
- [Java] Removed implicit utility class constructors and made classes final

### Fixed
- Improve detection of Bamboo environment variables
- Improve detection of Azure environment variables
- Fix `Git#remote` for CodeFresh

### Removed
- Removed support for TeamCity since it doesn't seem to expose enough environment variables.

## [5.0.0] - 2021-05-17
### Added
- Added support for [Jenkins](https://www.jenkins.io/) (via the [Git plugin](https://plugins.jenkins.io/git/))
([#1253](https://github.com/cucumber/common/issues/1253)
[#1553](https://github.com/cucumber/common/pull/1553)
[aslakhellesoy](https://github.com/aslakhellesoy))
- Added support for [Bitrise](https://www.bitrise.io/)
([#1490](https://github.com/cucumber/common/issues/1490)
[#1553](https://github.com/cucumber/common/pull/1553)
[aslakhellesoy](https://github.com/aslakhellesoy))
- Added support for [CodeFresh](https://codefresh.io/)
([#1553](https://github.com/cucumber/common/pull/1553)
[aslakhellesoy](https://github.com/aslakhellesoy))
- Added support for [CodeShip](https://www.cloudbees.com/products/codeship)
([#1553](https://github.com/cucumber/common/pull/1553)
[aslakhellesoy](https://github.com/aslakhellesoy))

### Changed
- Upgrade messages to 16.0.0

### Fixed
- Fixed detection of Semaphore build url.
- Fixed detection of GoCD build url.

## [4.0.0] - 2021-03-29
### Changed
- Upgrade messages to 15.0.0

## [3.0.0] - 2021-02-09
### Changed
- Upgrade messages to 14.0.1

## [2.0.4] - 2020-10-29
### Fixed
- Handle null values in ci dict
([#1228](https://github.com/cucumber/cucumber/issues/1228)
[#1229](https://github.com/cucumber/cucumber/pull/1229)
[kgeilmann](https://github.com/kgeilmann))

## [2.0.2] - 2020-09-03

## [2.0.1] - 2020-08-18
### Fixed
- Ruby: Add the ci property to meta message

## [2.0.0] - 2020-08-07
### Changed
- Update `messages` to 13.0.1

### Fixed
- Java: Use `java.vm.name` instead of `java.vendor` (which is `N/A` on OpenJDK)
- Java: Use `java.vm.version` instead of `java.version`
- Support GitHub Enterprise by using `GITHUB_SERVER_URL` to construct URLs

## [1.2.0] - 2020-07-31
### Changed
- Updated `messages` to v12.4.0

## [1.1.0] - 2020-07-30
### Changed
- Updated `messages` to v12.3.2

## [1.0.0] - 2020-06-29
### Added
- First release

[Unreleased]: https://github.com/cucumber/ci-environment/compare/v9.2.0...main
[9.2.0]: https://github.com/cucumber/ci-environment/compare/v9.1.0...main
[9.1.0]: https://github.com/cucumber/ci-environment/compare/v9.0.4...main
[9.0.4]: https://github.com/cucumber/ci-environment/compare/v9.0.3...main
[9.0.3]: https://github.com/cucumber/ci-environment/compare/v9.0.2...main
[9.0.2]: https://github.com/cucumber/ci-environment/compare/v9.0.1...main
[9.0.1]: https://github.com/cucumber/ci-environment/compare/v9.0.0...main
[9.0.0]: https://github.com/cucumber/ci-environment/compare/v8.1.0...main
[8.1.0]: https://github.com/cucumber/ci-environment/compare/v8.0.1...v8.1.0
[8.0.1]: https://github.com/cucumber/ci-environment/compare/v8.0.0...v8.0.1
[8.0.0]: https://github.com/cucumber/ci-environment/compare/v7.0.1...v8.0.0
[7.0.1]: https://github.com/cucumber/ci-environment/compare/v7.0.0...v7.0.1
[7.0.0]: https://github.com/cucumber/ci-environment/compare/v6.0.4...v7.0.0
[6.0.4]: https://github.com/cucumber/ci-environment/compare/v6.0.3...v6.0.4
[6.0.3]: https://github.com/cucumber/ci-environment/compare/v6.0.2...v6.0.3
[6.0.2]: https://github.com/cucumber/ci-environment/compare/v6.0.1...v6.0.2
[6.0.1]: https://github.com/cucumber/ci-environment/compare/v6.0.0...v6.0.1
[6.0.0]: https://github.com/cucumber/ci-environment/compare/v5.0.0...v6.0.0
[5.0.0]: https://github.com/cucumber/ci-environment/compare/v4.0.0...v5.0.0
[4.0.0]: https://github.com/cucumber/ci-environment/compare/v3.0.0...v4.0.0
[3.0.0]: https://github.com/cucumber/ci-environment/compare/v2.0.4...v3.0.0
[2.0.4]: https://github.com/cucumber/ci-environment/compare/v2.0.2...v2.0.4
[2.0.2]: https://github.com/cucumber/ci-environment/compare/v2.0.1...v2.0.2
[2.0.1]: https://github.com/cucumber/ci-environment/compare/v2.0.0...v2.0.1
[2.0.0]: https://github.com/cucumber/ci-environment/compare/v1.2.0...v2.0.0
[1.2.0]: https://github.com/cucumber/ci-environment/compare/v1.1.0...v1.2.0
[1.1.0]: https://github.com/cucumber/ci-environment/compare/v1.0.0...v1.1.0
[1.0.0]: https://github.com/cucumber/cucumber/releases/tag/v1.0.0
