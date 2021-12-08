export type CiEnvironment = {
  name: string
  url: string
  buildNumber: string
  git: Git
}

export type Git = {
  remote?: string
  branch?: string
  revision?: string
  tag?: string
}

export type CiDict = Record<string, CiEnvironment>
export type Env = Record<string, string | undefined>
