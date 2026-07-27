# frozen_string_literal: true

version = File.read(File.expand_path('VERSION', __dir__)).strip

Gem::Specification.new do |s|
  s.name        = 'cucumber-ci-environment'
  s.version     = version
  s.authors     = ['Vincent Prêtre', 'Luke Hill']
  s.description = 'Detect CI Environment from environment variables'
  s.summary     = "#{s.name}-#{s.version}"
  s.email       = 'cukes@googlegroups.com'
  s.homepage    = 'https://github.com/cucumber/ci-environment'
  s.platform    = Gem::Platform::RUBY
  s.license     = 'MIT'
  s.required_ruby_version = '>= 3.3'
  s.required_rubygems_version = '>= 3.2.8'

  s.metadata = {
    'bug_tracker_uri' => 'https://github.com/cucumber/ci-environment/issues',
    'changelog_uri' => 'https://github.com/cucumber/ci-environment/blob/main/CHANGELOG.md',
    'source_code_uri' => 'https://github.com/cucumber/ci-environment/tree/main/ruby'
  }

  s.add_development_dependency 'rake', '~> 13.3'
  s.add_development_dependency 'rspec', '~> 3.13'
  s.add_development_dependency 'rubocop', '~> 1.88.2'
  s.add_development_dependency 'rubocop-performance', '~> 1.26.1'
  s.add_development_dependency 'rubocop-rake', '~> 0.7.1'
  s.add_development_dependency 'rubocop-rspec', '~> 3.10.2'

  s.files            = Dir['README.md', 'LICENSE', 'lib/**/*']
  s.rdoc_options     = ['--charset=UTF-8']
  s.require_path     = 'lib'
end
