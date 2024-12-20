import { readFileSync } from 'fs'

import { CiEnvironments } from './CiEnvironments.js'
import evaluateVariableExpression from './evaluateVariableExpression.js'
import { CiEnvironment, Env, Git } from './types.js'

export default function detectCiEnvironment(env: Env): CiEnvironment | undefined {
  for (const ciEnvironment of CiEnvironments) {
    const detected = detect(ciEnvironment, env)
    if (detected) {
      return detected
    }
  }
}

export function removeUserInfoFromUrl(value: string): string {
  if (!value) return value
  try {
    const url = new URL(value)
    url.password = ''
    url.username = ''
    return url.toString()
  } catch (_) {
    return value
  }
}

function detectGit(ciEnvironment: CiEnvironment, env: Env): Git | undefined {
  const revision = detectRevision(ciEnvironment, env)
  if (!revision) {
    return undefined
  }

  const remote = evaluateVariableExpression(ciEnvironment.git?.remote, env)
  if (!remote) {
    return undefined
  }

  const tag = evaluateVariableExpression(ciEnvironment.git?.tag, env)
  const branch = evaluateVariableExpression(ciEnvironment.git?.branch, env)

  return {
    revision,
    remote: removeUserInfoFromUrl(remote),
    ...(tag && { tag }),
    ...(branch && { branch }),
  }
}

function detectRevision(ciEnvironment: CiEnvironment, env: Env): string | undefined {
  if (env.GITHUB_EVENT_NAME === 'pull_request') {
    if (!env.GITHUB_EVENT_PATH) throw new Error('GITHUB_EVENT_PATH not set')
    const json = readFileSync(env.GITHUB_EVENT_PATH, 'utf-8')
    const event = JSON.parse(json)
    const revision = event.pull_request?.head?.sha
    if (!revision) {
      throw new Error(
        `Could not find .pull_request.head.sha in ${env.GITHUB_EVENT_PATH}:\n${JSON.stringify(
          event,
          null,
          2
        )}`
      )
    }
    return revision
  }
  return evaluateVariableExpression(ciEnvironment.git?.revision, env)
}

function detect(ciEnvironment: CiEnvironment, env: Env): CiEnvironment | undefined {
  const url = evaluateVariableExpression(ciEnvironment.url, env)
  if (url === undefined) {
    // The url is what consumers will use as the primary key for a build
    // If this cannot be determined, we return nothing.
    return undefined
  }
  const buildNumber = evaluateVariableExpression(ciEnvironment.buildNumber, env)
  const git = detectGit(ciEnvironment, env)

  return {
    name: ciEnvironment.name,
    url,
    buildNumber,
    ...(git && { git }),
  }
}
