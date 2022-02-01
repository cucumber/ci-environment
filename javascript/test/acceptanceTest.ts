import assert from 'assert'
import fs, { readFileSync } from 'fs'
import glob from 'glob'
import path from 'path'

import detectCiEnvironment from '../src/index.js'
import { Env, GithubActionsEvent, SyncFileReader } from '../src/types.js'

describe('detectCiEnvironment', () => {
  for (const txt of glob.sync(`../testdata/*.txt`)) {
    it(`detects ${path.basename(txt, '.txt')}`, () => {
      const envData = fs.readFileSync(txt, { encoding: 'utf8' })
      const entries = envData.split(/\n/).map((line) => line.split(/=/))
      const env: Env = Object.fromEntries(entries)
      const ciEnvironment = detectCiEnvironment(env, syncFileReader)

      const expectedJson = fs.readFileSync(`${txt}.json`, {
        encoding: 'utf8',
      })
      assert.deepStrictEqual(ciEnvironment, JSON.parse(expectedJson))
    })
  }
})

const syncFileReader: SyncFileReader = (path: string) => {
  if (path.endsWith('_github_workflow/event.json')) {
    const event: GithubActionsEvent = {
      pull_request: {
        head: {
          sha: '2436f28fad432a895bfc595bce16e907144b0dc3',
        },
      },
    }
    return Buffer.from(JSON.stringify(event, null, 2), 'utf-8')
  }
  return readFileSync(path)
}
