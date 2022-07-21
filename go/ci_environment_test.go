package cienvironment_test

import (
	"testing"

	cienvironment "github.com/cucumber/ci-environment/go"
	"github.com/stretchr/testify/assert"
)

func TestRemoveUserInfo(t *testing.T) {
	testCases := []struct {
		input *cienvironment.CiEnvironment
		want  *cienvironment.Git
	}{
		{
			&cienvironment.CiEnvironment{},
			nil,
		},
		{
			&cienvironment.CiEnvironment{Git: &cienvironment.Git{Revision: "2a2f73c6", Remote: "https://cihost.com"}},
			&cienvironment.Git{Revision: "2a2f73c6", Remote: "https://cihost.com"},
		},
		{
			&cienvironment.CiEnvironment{Git: &cienvironment.Git{Revision: "2a2f73c6", Remote: "https://user:pass@cihost.com"}},
			&cienvironment.Git{Revision: "2a2f73c6", Remote: "https://cihost.com"},
		},
		{
			&cienvironment.CiEnvironment{Git: &cienvironment.Git{Revision: "2a2f73c6", Remote: "not_a_valid_url"}},
			&cienvironment.Git{Revision: "2a2f73c6", Remote: "not_a_valid_url"},
		},
	}
	for _, tc := range testCases {
		got := tc.input.RemoveUserInfo().Git
		assert.Equal(t, tc.want, got, tc.input.Git)
	}
}
