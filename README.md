# createMeta

Utility function for creating system-specific `Meta` messages.
## Supported CI systems

The `ci` field of the `Meta` message contains values from environment variables
defined by the following supported CI and build servers:

* [Azure Pipelines](https://docs.microsoft.com/en-us/azure/devops/pipelines/build/variables?tabs=yaml&view=azure-devops#build-variables)
* [Bamboo](https://confluence.atlassian.com/bamboo/bamboo-variables-289277087.html)
* [Buddy](https://buddy.works/docs/pipelines/environment-variables#default-environment-variables)
* [Bitrise](https://devcenter.bitrise.io/builds/available-environment-variables/)
* [CircleCI](https://circleci.com/docs/2.0/env-vars/#built-in-environment-variables)
* [CodeFresh](https://codefresh.io/docs/docs/codefresh-yaml/variables/#system-provided-variables)
* [CodeShip](https://documentation.codeship.com/basic/builds-and-configuration/set-environment-variables/)
* [GitHub Actions](https://help.github.com/en/actions/configuring-and-managing-workflows/using-environment-variables)
* [GitLab](https://docs.gitlab.com/ee/ci/variables/predefined_variables.html)
* [GoCD](https://docs.gocd.org/current/faq/dev_use_current_revision_in_build.html)
* [Jenkins](https://www.jenkins.io/doc/book/pipeline/jenkinsfile/#using-environment-variables) and [Jenkins Git plugin](https://plugins.jenkins.io/git/#environment-variables)
* [Semaphore](https://docs.semaphoreci.com/ci-cd-environment/environment-variables/)
* [Travis CI](https://docs.travis-ci.com/user/environment-variables/#Default-Environment-Variables)
* [Wercker](https://devcenter.wercker.com/administration/environment-variables/available-env-vars/)

## Adding new CI system / contributing

Please see [CONTRIBUTING.md](./CONTRIBUTING.md) for more information.
