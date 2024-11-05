# frozen_string_literal: true

describe 'GitHub' do
  if ENV['GITHUB_EVENT_NAME'] == 'pull_request'
    it 'detects the correct revision for pull requests' do
      ci_environment = Cucumber::CiEnvironment.detect_ci_environment(ENV)

      expect(ci_environment).to be_truthy
    end
  end
end
