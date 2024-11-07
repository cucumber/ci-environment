# frozen_string_literal: true

require 'json'
require 'set'

# This script can be used to update the test data when the CiEnvironments.json file is modified.
# After running this script, the testdata should be inspected and updated manually

json = JSON.parse(File.read("#{File.dirname(__FILE__)}/CiEnvironments.json"))

def collect_vars(environment, env_vars)
  environment.each_value do |value|
    if value.is_a?(String)
      # Collect all variables: RegExp copied from ruby/lib/cucumber/ci_environment/variable_expression.rb
      value.scan(%r{\${(.*?)(?:(?<!\\)/(.*)/(.*))?}}) do |match|
        env_vars.add(match[0])
      end
    elsif value
      collect_vars(value, env_vars)
    end
  end
end

json.each do |name, ci|
  filename = "testdata/#{name.gsub(/\s/, '')}.txt"
  puts "--- #{filename}"
  txt_file_env_vars = []
  if File.exist?(filename)
    File.read(filename).each_line do |line|
      txt_file_env_vars.push(line.strip.split('='))
    end
  end
  used_env_vars = Set.new
  collect_vars(ci, used_env_vars)
  used_env_vars.to_a.sort.each do |used_env_var|
    exists = txt_file_env_vars.detect { |pair| pair[0] == used_env_var }
    txt_file_env_vars.push([used_env_var, '???']) unless exists
  end
  File.open(filename, 'w:UTF-8') do |io|
    txt = txt_file_env_vars.map { |pair| pair.join('=') }.join("\n") + "\n"
    io.write(txt)
  end
end
