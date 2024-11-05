# frozen_string_literal: true

require 'cucumber/ci_environment'
require 'json'

describe Cucumber::CiEnvironment do
  describe '.detect_ci_environment' do
    subject { JSON.parse(ci_environment.to_json) }

    let(:ci_environment) { described_class.detect_ci_environment(env) }

    Dir.glob('../testdata/*.txt') do |test_data_file|
      context "with #{File.basename(test_data_file, '.txt')}" do
        let(:entries) { env_data.split("\n").map { |line| line.split('=') } }
        let(:env) { entries.to_h }
        let(:env_data) { File.read(test_data_file) }

        let(:expected_json) { File.read("#{test_data_file}.json") }

        it { is_expected.to eq(JSON.parse(expected_json)) }
      end
    end

    context 'with no CI environment' do
      let(:env) { {} }

      it { is_expected.to be_nil }
    end

    context 'for GitHub pull requests' do
      if ENV['GITHUB_EVENT_NAME'] == 'pull_request'
        it 'detects the correct revision for pull requests' do
          ci_environment = described_class.detect_ci_environment(ENV)

          expect(ci_environment).to be_truthy
        end
      end
    end
  end

  describe '.remove_user_info_from_url' do
    it 'returns nil for nil' do
      expect(described_class.remove_userinfo_from_url(nil)).to be_nil
    end

    it 'returns empty string for empty string' do
      expect(described_class.remove_userinfo_from_url('')).to eq('')
    end

    it 'leaves the data intact when no sensitive information is detected' do
      expect(described_class.remove_userinfo_from_url('pretty safe')).to eq('pretty safe')
    end

    context 'with URLs' do
      it 'leaves intact when no password is found' do
        expect(described_class.remove_userinfo_from_url('https://example.com/git/repo.git')).to eq('https://example.com/git/repo.git')
      end

      it 'removes credentials when found' do
        expect(described_class.remove_userinfo_from_url('http://login@example.com/git/repo.git')).to eq('http://example.com/git/repo.git')
      end

      it 'removes credentials and passwords when found' do
        expect(described_class.remove_userinfo_from_url('ssh://login:password@example.com/git/repo.git')).to eq('ssh://example.com/git/repo.git')
      end
    end
  end
end
