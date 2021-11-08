import assert from 'assert'
import fs from 'fs'
import path from 'path'

import CreateMeta from '../../src/createMeta'
import { Env } from '../../src/types'

const TEST_DATA_DIR = '../testdata'

describe('CreateMeta', () => {
  const test_data_files = fs.readdirSync(TEST_DATA_DIR)

  test_data_files.forEach((test_data_file) => {
    if (path.extname(test_data_file) !== '.txt') {
      return
    }

    const env_data = fs.readFileSync(`${TEST_DATA_DIR}/${test_data_file}`, { encoding: 'utf8' })
    const entries = env_data.split(/\n/).map((line) => line.split(/=/))
    const env: Env = Object.fromEntries(entries)
    const meta = CreateMeta('cucumber-something', '1.2.3', env)

    const expected_json = fs.readFileSync(`${TEST_DATA_DIR}/${test_data_file}.json`, {
      encoding: 'utf8',
    })

    it(`with ${path.basename(test_data_file, '.txt')}`, () => {
      assert.deepStrictEqual(meta.ci, JSON.parse(expected_json))
    })
  })
})
