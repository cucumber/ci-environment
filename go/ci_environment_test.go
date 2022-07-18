package cienvironment_test

import (
	"testing"

	cienvironment "github.com/cucumber/ci-environment/go"
	"github.com/stretchr/testify/assert"
)

// func TestIsPresent(t *testing.T) {
// 	testCases := []struct {
// 		ciEnvironment *cienvironment.CiEnvironment
// 		want          bool
// 	}{
// 		{&cienvironment.CiEnvironment{Name: "Azure Pipelines", URL: "https://cihost.com/path/to/the/build"}, true},
// 		{&cienvironment.CiEnvironment{Name: "Azure Pipelines", URL: ""}, false},
// 		{&cienvironment.CiEnvironment{Name: "GitHub Actions", URL: "https://github.com/cucumber-ltd/shouty.rb/actions/runs/154666429"}, true},
// 		{&cienvironment.CiEnvironment{Name: "GitHub Actions", URL: "//actions/runs/"}, false},
// 		{&cienvironment.CiEnvironment{Name: "GoCD", URL: "https://cihost.com/pipelines/pname/154666428/sname/154666429"}, true},
// 		{&cienvironment.CiEnvironment{Name: "GoCD", URL: "/pipelines////"}, false},
// 		{&cienvironment.CiEnvironment{Name: "Semaphore", URL: "https://cihost.com/jobs/154666429"}, true},
// 		{&cienvironment.CiEnvironment{Name: "Semaphore", URL: "/jobs/"}, false},
// 	}
// 	for _, tc := range testCases {
// 		got := tc.ciEnvironment.IsPresent()
// 		assert.Equal(t, tc.want, got, tc.ciEnvironment.URL)
// 	}
// }

// func TestSanitizeGit(t *testing.T) {
// 	testCases := []struct {
// 		input *cienvironment.CiEnvironment
// 		want  *cienvironment.Git
// 	}{
// 		{
// 			&cienvironment.CiEnvironment{},
// 			nil,
// 		},
// 		{
// 			&cienvironment.CiEnvironment{Git: &cienvironment.Git{}},
// 			nil,
// 		},
// 		{
// 			&cienvironment.CiEnvironment{Git: &cienvironment.Git{Revision: "2a2f73c6"}},
// 			&cienvironment.Git{Revision: "2a2f73c6"},
// 		},
// 	}
// 	for _, tc := range testCases {
// 		got := tc.input.SanitizeGit()
// 		assert.Equal(t, tc.want, got.Git, tc.input.Git)
// 	}
// }

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
