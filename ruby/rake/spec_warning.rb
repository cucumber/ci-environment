# frozen_string_literal: true

require 'rspec/core/rake_task'
require_relative '../spec/capture_warnings'

RSpec::Core::RakeTask.new(:spec)

namespace :spec do
  task :warnings do
    include CaptureWarnings

    report_warnings do
      Rake::Task['spec'].invoke
    end
  end
end
