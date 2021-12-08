# frozen_string_literal: true

require 'rake'

RSpec.configure do |config|
  config.before(:suite) do
    load 'Rakefile'
    Rake::Task[:copy_ci_dict].invoke
  end
end
