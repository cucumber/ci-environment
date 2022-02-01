import { readFileSync } from 'fs'

import { CiEnvironments } from './CiEnvironments.js'
import evaluateVariableExpression from './evaluateVariableExpression.js'
import { CiEnvironment, Env, Git, GithubActionsEvent, SyncFileReader } from './types.js'

export default function detectCiEnvironment(
  env: Env,
  fileReader: SyncFileReader = readFileSync
): CiEnvironment | undefined {
  for (const ciEnvironment of CiEnvironments) {
    const detected = detect(ciEnvironment, env, fileReader)
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
  } catch (ignore) {
    return value
  }
}

function detectGit(
  ciEnvironment: CiEnvironment,
  env: Env,
  syncFileReader: SyncFileReader
): Git | undefined {
  const revision = detectRevision(ciEnvironment, env, syncFileReader)
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

function detectRevision(
  ciEnvironment: CiEnvironment,
  env: Env,
  syncFileReader: SyncFileReader
): string | undefined {
  if (ciEnvironment.name === 'GitHub Actions') {
    if (!env.GITHUB_EVENT_PATH) throw new Error('GITHUB_EVENT_PATH not set')
    const event: GithubActionsEvent = JSON.parse(syncFileReader(env.GITHUB_EVENT_PATH).toString())

    return event.pull_request.head.sha
  }
  return evaluateVariableExpression(ciEnvironment.git?.revision, env)
}

function detect(
  ciEnvironment: CiEnvironment,
  env: Env,
  syncFileReader: SyncFileReader
): CiEnvironment | undefined {
  const url = evaluateVariableExpression(ciEnvironment.url, env)
  if (url === undefined) {
    // The url is what consumers will use as the primary key for a build
    // If this cannot be determined, we return nothing.
    return undefined
  }
  const buildNumber = evaluateVariableExpression(ciEnvironment.buildNumber, env)
  const git = detectGit(ciEnvironment, env, syncFileReader)

  return {
    name: ciEnvironment.name,
    url,
    buildNumber,
    ...(git && { git }),
  }
}
