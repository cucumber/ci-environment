# frozen_string_literal: true

require 'cucumber/create_meta'
require 'json'

TEST_DATA_DIR = '../testdata'

describe 'CreateMeta' do
  Dir.each_child(TEST_DATA_DIR) do |test_data_file|
    next unless File.extname(test_data_file) == '.txt'

    context "with #{File.basename(test_data_file, '.*')}" do
      subject { JSON.parse(meta.ci.to_json) }

      let(:meta) { Cucumber::CreateMeta.create_meta('cucumber-something', '1.2.3', env) }
      let(:env) { Hash[entries] }
      let(:entries) { env_data.split(/\n/).map { |line| line.split(/=/) } }
      let(:env_data) { IO.read("#{TEST_DATA_DIR}/#{test_data_file}") }

      let(:expected_json) { File.read("#{TEST_DATA_DIR}/#{test_data_file}.json") }

      it { is_expected.to eq JSON.parse(expected_json) }
    end
  end
end
