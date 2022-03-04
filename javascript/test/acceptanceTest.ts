import assert from 'assert'
import fs from 'fs'
import glob from 'glob'
import path from 'path'

import { GithubActionsEvent, SyncFileReader } from '../src/detectCiEnvironment.js'
import detectCiEnvironment from '../src/index.js'
import { Env } from '../src/types.js'

describe('detectCiEnvironment', () => {
  for (const txt of glob.sync(`../testdata/*.txt`)) {
    it(`detects ${path.basename(txt, '.txt')}`, () => {
      const envData = fs.readFileSync(txt, { encoding: 'utf8' })
      const entries = envData.split(/\n/).map((line) => line.split(/=/))
      const env: Env = Object.fromEntries(entries)
      const ciEnvironment = detectCiEnvironment(env, gitHubActionReader)

      const expectedJson = fs.readFileSync(`${txt}.json`, {
        encoding: 'utf8',
      })
      assert.deepStrictEqual(ciEnvironment, JSON.parse(expectedJson))
    })
  }
})

const gitHubActionReader: SyncFileReader = () => {
  const event: GithubActionsEvent = {
    before: '2436f28fad432a895bfc595bce16e907144b0dc3',
  }
  return Buffer.from(JSON.stringify(event, null, 2), 'utf-8')
}
