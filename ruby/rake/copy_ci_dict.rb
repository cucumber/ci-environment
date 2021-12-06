# frozen_string_literal: true

require 'rake'

task :copy_ci_dict do
  FileUtils.cp '../ciDict.json', 'lib/cucumber/ciDict.json'
end
