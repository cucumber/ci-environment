import { format as formatUrl, parse as parseUrl } from 'url'

import { ciDict as defaultCiDict } from './ciDict'
import evaluateVariableExpression from './evaluateVariableExpression'
import { CiDict, CiEnvironment, Env, Git } from './types'

export default function detectCiEnvironment(env: Env, ciDict?: CiDict): CiEnvironment {
  if (ciDict === undefined) {
    ciDict = defaultCiDict
  }
  return detectCI(ciDict, env)
}

export function detectCI(ciDict: CiDict, envDict: Env): CiEnvironment {
  const detected: CiEnvironment[] = []
  for (const [ciName, ciSystem] of Object.entries(ciDict)) {
    const ci = createCi(ciName, ciSystem, envDict)
    if (ci) {
      detected.push(ci)
    }
  }
  if (detected.length !== 1) {
    return undefined
  }
  if (detected.length > 1) {
    console.error(
      `@cucumber/create-meta WARNING: Detected more than one CI: ${JSON.stringify(
        detected,
        null,
        2
      )}`
    )
    console.error('Using the first one.')
  }
  return detected[0]
}

export function removeUserInfoFromUrl(value: string): string {
  if (!value) return value
  const url = parseUrl(value)
  if (url.auth === null) return value
  url.auth = null
  return formatUrl(url)
}

function createCi(
  ciName: string,
  ciSystem: CiEnvironment,
  envDict: Env
): CiEnvironment | undefined {
  const url = evaluateVariableExpression(ciSystem.url, envDict)
  const buildNumber = evaluateVariableExpression(ciSystem.buildNumber, envDict)
  if (url === undefined) {
    // The url is what consumers will use as the primary key for a build
    // If this cannot be determined, we return nothing.
    return undefined
  }

  const tag = evaluateVariableExpression(ciSystem.git.tag, envDict)
  const git: Git = {
    remote: removeUserInfoFromUrl(evaluateVariableExpression(ciSystem.git.remote, envDict)),
    revision: evaluateVariableExpression(ciSystem.git.revision, envDict),
    branch: evaluateVariableExpression(ciSystem.git.branch, envDict),
    ...(tag ? { tag } : {}),
  }

  return {
    name: ciName,
    url,
    buildNumber,
    git: git,
  }
}
