# frozen_string_literal: true

require 'cucumber/ci_environment'

describe Cucumber::CiEnvironment::VariableExpression do
  describe '.evaluate' do
    subject(:ci_environment) { Cucumber::CiEnvironment }

    it 'returns nil when a variable is undefined' do
      expression = 'hello-${SOME_VAR}'
      result = ci_environment.evaluate(expression, {})

      expect(result).to be_nil
    end

    it 'gets a value without replacement' do
      expression = '${SOME_VAR}'
      result = ci_environment.evaluate(expression, { 'SOME_VAR' => 'some_value' })

      expect(result).to eq('some_value')
    end

    it 'captures a group' do
      expression = '${SOME_REF/refs/heads/(.*)/\\1}'
      result = ci_environment.evaluate(expression, { 'SOME_REF' => 'refs/heads/main' })

      expect(result).to eq('main')
    end

    it 'works with star wildcard in var' do
      expression = '${GO_SCM_*_PR_BRANCH/.*:(.*)/\\1}'
      result = ci_environment.evaluate(expression, { 'GO_SCM_MY_MATERIAL_PR_BRANCH' => 'ashwankthkumar:feature-1' })

      expect(result).to eq('feature-1')
    end

    it 'ci_environment.evaluates a complex expression' do
      expression = 'hello-${VAR1}-${VAR2/(.*) (.*)/\\2-\\1}-world'
      result = ci_environment.evaluate(expression, { 'VAR1' => 'amazing', 'VAR2' => 'gorgeous beautiful' })

      expect(result).to eq('hello-amazing-beautiful-gorgeous-world')
    end
  end
end
