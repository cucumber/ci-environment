import assert from 'assert'
import fs from 'fs'
import glob from 'glob'
import path from 'path'

import detectCiEnvironment from '../src/index'
import { Env } from '../src/types'

describe('detectCiEnvironment', () => {
  for (const txt of glob.sync(`${__dirname}/../../testdata/*.txt`)) {
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
