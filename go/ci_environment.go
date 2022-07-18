package cienvironment

import (
	"net/url"
)

type Git struct {
	Remote   string `json:"remote"`
	Revision string `json:"revision"`
	Branch   string `json:"branch"`
	Tag      string `json:"tag"`
}

type CiEnvironment struct {
	Name        string `json:"name"`
	URL         string `json:"url"`
	BuildNumber string `json:"buildNumber"`
	Git         *Git   `json:"git"`
}

// IsPresent returns true is the CiEnvironment has a URL that has been built using detected environment variables.
// Most CI Environments have a URL that is a single environment variable, e.g., ${BUILD_BUILDURI}.
// A few are a combination of literals and multiple environment variables, e.g., ${SEMAPHORE_ORGANIZATION_URL}/jobs/${SEMAPHORE_JOB_ID}.
func (c *CiEnvironment) IsPresent() bool {
	// switch c.Name {
	// case "GitHub Actions":
	// 	return c.URL != "//actions/runs/"
	// case "GoCD":
	// 	return c.URL != "/pipelines////"
	// case "Semaphore":
	// 	return c.URL != "/jobs/"
	// }
	return len(c.URL) > 0
}

// SanitizeGit removes any non-empty Git fields, when the Git.Revision is empty.
// Remove any user info from the git remote value.
func (c *CiEnvironment) SanitizeGit() *CiEnvironment {
	if c.Git == nil {
		return c
	}
	// fmt.Printf("---> remote (%s) revision (%s) branch (%s)\n", c.Git.Remote, c.Git.Revision, c.Git.Branch)
	if len(c.Git.Remote) == 0 || len(c.Git.Revision) == 0 {
		c.Git = nil
	}
	return c.RemoveUserInfo()
}

func (c *CiEnvironment) RemoveUserInfo() *CiEnvironment {
	if c.Git == nil {
		return c
	}
	u, err := url.Parse(c.Git.Remote)
	if err != nil {
		return c
	}
	u.User = nil
	c.Git.Remote = u.String()
	return c
}
