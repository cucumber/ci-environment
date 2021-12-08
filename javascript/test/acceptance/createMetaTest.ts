import assert from 'assert'
import fs from 'fs'
import path from 'path'

import detectCiEnvironment from '../../src/detectCiEnvironment'
import { Env } from '../../src/types'

const TEST_DATA_DIR = '../testdata'

describe('CreateMeta', () => {
  const test_data_files = fs.readdirSync(TEST_DATA_DIR)

  test_data_files.forEach((test_data_file) => {
    if (path.extname(test_data_file) !== '.txt') {
      return
    }

    const envData = fs.readFileSync(`${TEST_DATA_DIR}/${test_data_file}`, { encoding: 'utf8' })
    const entries = envData.split(/\n/).map((line) => line.split(/=/))
    const env: Env = Object.fromEntries(entries)
    const ciEnvironment = detectCiEnvironment(env)

    const expectedJson = fs.readFileSync(`${TEST_DATA_DIR}/${test_data_file}.json`, {
      encoding: 'utf8',
    })

    it(`with ${path.basename(test_data_file, '.txt')}`, () => {
      assert.deepStrictEqual(ciEnvironment, JSON.parse(expectedJson))
    })
  })
})
