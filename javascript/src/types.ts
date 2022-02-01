export type CiEnvironment = {
  name: string
  url: string
  buildNumber?: string
  git?: Git
}

export type Git = {
  remote: string
  revision: string
  branch?: string
  tag?: string
}

export type CiEnvironments = {
  ciEnvironments: readonly CiEnvironment[]
}

export type Env = Record<string, string | undefined>

export type GithubActionsEvent = {
  pull_request: {
    head: {
      sha: string
    }
  }
}

export type SyncFileReader = (path: string) => Buffer
