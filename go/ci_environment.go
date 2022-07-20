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
func (c *CiEnvironment) IsPresent() bool {
	return len(c.URL) > 0
}

// SanitizeGit removes any non-empty Git fields, when the Git.Revision or Git.Remote is empty.
// Standardizes that Git is nil when no Git repository is detected.
// Remove any user info from the git remote value.
func (c *CiEnvironment) SanitizeGit() *CiEnvironment {
	if c.Git == nil {
		return c
	}
	if len(c.Git.Remote) == 0 || len(c.Git.Revision) == 0 {
		c.Git = nil
	}
	return c.RemoveUserInfo()
}

// RemoveUserInfo removes the user info, especially password, from the Git remote URL
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
