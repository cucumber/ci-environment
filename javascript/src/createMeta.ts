import * as messages from '@cucumber/messages'
import os from 'os'
import { format as formatUrl, parse as parseUrl } from 'url'

import defaultCiDict from './ciDict.json'
import evaluateVariableExpression from './evaluateVariableExpression'
import { CiDict, CiSystem, Env } from './types'

type GitInfo = {
  remote: string
  revision: string
  branch: string
  tag?: string
}

export default function createMeta(
  toolName: string,
  toolVersion: string,
  envDict: Env,
  ciDict?: CiDict
): messages.Meta {
  if (ciDict === undefined) {
    ciDict = defaultCiDict
  }
  return {
    protocolVersion: messages.version,
    implementation: {
      name: toolName,
      version: toolVersion,
    },
    cpu: {
      name: os.arch(),
    },
    os: {
      name: os.platform(),
      version: os.release(),
    },
    runtime: {
      name: 'node.js',
      version: process.versions.node,
    },
    ci: detectCI(ciDict, envDict),
  }
}

export function detectCI(ciDict: CiDict, envDict: Env): messages.Ci | undefined {
  const detected: messages.Ci[] = []
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

function createCi(ciName: string, ciSystem: CiSystem, envDict: Env): messages.Ci | undefined {
  const url = evaluateVariableExpression(ciSystem.url, envDict)
  const buildNumber = evaluateVariableExpression(ciSystem.buildNumber, envDict)
  if (url === undefined) {
    // The url is what consumers will use as the primary key for a build
    // If this cannot be determined, we return nothing.
    return undefined
  }

  const branch = evaluateVariableExpression(ciSystem.git.branch, envDict)
  const tag = evaluateVariableExpression(ciSystem.git.tag, envDict)
  const git: GitInfo = {
    remote: removeUserInfoFromUrl(evaluateVariableExpression(ciSystem.git.remote, envDict)),
    revision: evaluateVariableExpression(ciSystem.git.revision, envDict),
    branch: branch,
  }

  if (tag) {
    git['tag'] = tag
  }

  return {
    name: ciName,
    url,
    buildNumber,
    git: git,
  }
}
