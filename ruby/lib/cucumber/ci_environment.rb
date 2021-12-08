require 'uri'
require 'json'
require 'cucumber/ci_environment/variable_expression'

module Cucumber
  module CiEnvironment
    extend VariableExpression
    CI_ENVIRONMENTS_PATH = File.join(File.dirname(__FILE__), 'ci_environment/CiEnvironments.json')

    def detect_ci_environment(env = ENV)
      ci_environments = JSON.parse(IO.read(CI_ENVIRONMENTS_PATH))
      ci_environments.each do |ci_environment|
        detected = detect(ci_environment, env)
        return detected unless detected.nil?
      end
    end

    def detect(ci_environment, env)
      url = evaluate(ci_environment['url'], env)
      return nil if url.nil?

      result = {
        name: ci_environment['name'],
        url: url,
        buildNumber: evaluate(ci_environment['buildNumber'], env),
        git: {
          remote: remove_userinfo_from_url(evaluate(ci_environment['git']['remote'], env)),
          revision: evaluate(ci_environment['git']['revision'], env),
          branch: evaluate(ci_environment['git']['branch'], env),
        }
      }
      tag = evaluate(ci_environment['git']['tag'], env)
      if tag
        result[:git][:tag] = tag
      end
      result
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

    module_function :detect_ci_environment, :detect, :remove_userinfo_from_url
  end
end
