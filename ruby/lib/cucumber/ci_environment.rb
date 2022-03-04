require 'uri'
require 'json'
require 'cucumber/ci_environment/variable_expression'

module Cucumber
  module CiEnvironment
    extend VariableExpression
    CI_ENVIRONMENTS_PATH = File.join(File.dirname(__FILE__), 'ci_environment/CiEnvironments.json')

    def detect_ci_environment(env, file_reader = IO.method(:read))
      ci_environments = JSON.parse(file_reader.call(CI_ENVIRONMENTS_PATH))
      ci_environments.each do |ci_environment|
        detected = detect(ci_environment, env, file_reader)
        return detected unless detected.nil?
      end

      nil
    end

    def detect(ci_environment, env, file_reader)
      url = evaluate(ci_environment['url'], env)
      return nil if url.nil?

      result = {
        name: ci_environment['name'],
        url: url,
        buildNumber: evaluate(ci_environment['buildNumber'], env),
      }

      detected_git = detect_git(ci_environment, env, file_reader)
      result[:git] = detected_git if detected_git
      result
    end

    def detect_git(ci_environment, env, file_reader)
      revision = detect_revision(ci_environment, env, file_reader)
      return nil if revision.nil?

      remote = evaluate(ci_environment['git']['remote'], env)
      return nil if remote.nil?

      git_info = {
        remote: remove_userinfo_from_url(remote),
        revision: revision,
      }

      tag = evaluate(ci_environment['git']['tag'], env)
      branch = evaluate(ci_environment['git']['branch'], env)
      git_info[:tag] = tag if tag
      git_info[:branch] = branch if branch
      git_info
    end

    def detect_revision(ci_environment, env, file_reader)
      if env['GITHUB_EVENT_NAME'] == 'pull_request'
        raise StandardError('GITHUB_EVENT_PATH not set') unless env['GITHUB_EVENT_PATH']
        event = JSON.parse(file_reader.call(env['GITHUB_EVENT_PATH']))
        raise StandardError("No after property in #{env['GITHUB_EVENT_PATH']}:\n#{JSON.pretty_generate(event)}") unless event['after']
        return event['after']
      end

      return evaluate(ci_environment['git']['revision'], env)
    end

    def remove_userinfo_from_url(value)
      return nil if value.nil?

      begin
        uri = URI(value)
        uri.userinfo = ''
        uri.to_s
      rescue StandardError
        value
      end
    end

    module_function :detect_ci_environment, :detect, :detect_git, :detect_revision, :remove_userinfo_from_url
  end
end
