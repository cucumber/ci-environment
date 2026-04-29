import assert from 'node:assert'
import fs from 'node:fs'
import path from 'node:path'
import { sync } from 'glob'

import detectCiEnvironment, { type Env } from '../src/index.js'

describe('detectCiEnvironment', () => {
  for (const txt of sync(`../testdata/src/*.txt`)) {
    it(`detects ${path.basename(txt, '.txt')}`, () => {
      const envData = fs.readFileSync(txt, { encoding: 'utf8' })
      const entries = envData.split(/\n/).map((line) => line.split(/=/))
      const env: Env = Object.fromEntries(entries)
      const ciEnvironment = detectCiEnvironment(env)

      const expectedJson = fs.readFileSync(`${txt}.json`, {
        encoding: 'utf8',
      })
      assert.deepStrictEqual(ciEnvironment, JSON.parse(expectedJson))
    })
  }
})
