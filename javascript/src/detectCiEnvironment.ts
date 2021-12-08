import { CiEnvironments } from './CiEnvironments'
import evaluateVariableExpression from './evaluateVariableExpression'
import { CiEnvironment, Env, Git } from './types'

export default function detectCiEnvironment(
  env: Env,
  ciEnvironments = CiEnvironments
): CiEnvironment | undefined {
  for (const ciEnvironment of ciEnvironments) {
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
  } catch (ignore) {
    return value
  }
}

function detect(ciEnvironment: CiEnvironment, env: Env): CiEnvironment | undefined {
  const url = evaluateVariableExpression(ciEnvironment.url, env)
  const buildNumber = evaluateVariableExpression(ciEnvironment.buildNumber, env)
  if (url === undefined) {
    // The url is what consumers will use as the primary key for a build
    // If this cannot be determined, we return nothing.
    return undefined
  }

  const tag = evaluateVariableExpression(ciEnvironment.git.tag, env)
  const git: Git = {
    remote: removeUserInfoFromUrl(evaluateVariableExpression(ciEnvironment.git.remote, env)),
    revision: evaluateVariableExpression(ciEnvironment.git.revision, env),
    branch: evaluateVariableExpression(ciEnvironment.git.branch, env),
    ...(tag ? { tag } : {}),
  }

  return {
    name: ciEnvironment.name,
    url,
    buildNumber,
    git: git,
  }
}
