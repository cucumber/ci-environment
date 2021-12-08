# CiEnvironment

This library detects the CI environment based on environment variables defined
by CI servers.

If a CI server is detected, a `CiEnvironment` struct is returned:

```json
{
  "name": "...",
  "url": "...",
  "buildNumber": "...",
  "git": {
    "remote": "...",
    "revision": "...",
    "branch": "..."
  }
}
```

Some CI servers expose usernames and passwords in the environment variable
that is used to detect `git.remote`. For security reasons, this library removes
the username and password from the `git.remote` field in the `CiEnvironment` struct.

### TypeScript

```shell
npm install @cucumber/ci-environment
```

```typescript
import detectCiEnvironment from '@cucumber/ci-environment'

const ciEnvironment = detectCiEnvironment(process.env)
console.log(JSON.stringify(ciEnvironment, null, 2))
```

### Java

```xml
<dependency>
    <groupId>io.cucumber</groupId>
    <artifactId>ci-environment</artifactId>
</dependency>
```

```java
import static io.cucumber.cienvironment.DetectCiEnvironment.detectCiEnvironment;

public class CiEnvironmentExample {
    public static void main(String[] args) {
        CiEnvironment ciEnvironment = detectCiEnvironment(System.getenv());
        System.out.println("ciEnvironment = " + ciEnvironment);
    }
}
```

### Ruby

```Gemfile
gem 'cucumber-ci-environment'
```

```ruby
require 'cucumber/ci_environment'

ci_environment = Cucumber::CiEnvironment.detect_ci_environment(ENV)
p ci_environment
```

## Supported CI servers

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

## Contributing

Please see [CONTRIBUTING.md](./CONTRIBUTING.md) for more information.
