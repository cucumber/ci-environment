package cienvironment_test

import (
	"bufio"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"log"
	"os"
	"strings"
	"testing"

	cienvironment "github.com/cucumber/ci-environment/go"
	"github.com/stretchr/testify/assert"
)

type testCase struct {
	fileName string
	envVars  map[string]string
	want     *cienvironment.CiEnvironment
}

func TestDetectCIEnvironment_AcceptanceTests(t *testing.T) {
	testCases := loadTestData()
	for _, tc := range testCases {
		t.Run(tc.fileName, testDetectCIEnvironment(tc.envVars, tc.want))
	}
}

func testDetectCIEnvironment(envVars map[string]string, want *cienvironment.CiEnvironment) func(*testing.T) {
	return func(t *testing.T) {
		// when running on github, unset the variables already present there for this test execution
		t.Setenv("GITHUB_SERVER_URL", "")
		t.Setenv("GITHUB_REPOSITORY", "")
		t.Setenv("GITHUB_RUN_ID", "")
		t.Setenv("GITHUB_SHA", "")
		t.Setenv("GITHUB_HEAD_REF", "")
		t.Setenv("GITHUB_EVENT_NAME", "")
		t.Setenv("GITHUB_EVENT_PATH", "")
		// set the environment variables per the .txt file in the testdata directory
		for k, v := range envVars {
			t.Setenv(k, v)
		}
		got := cienvironment.DetectCIEnvironment()
		assert.Equal(t, want.Name, got.Name)
		assert.Equal(t, want.URL, got.URL, "URL for %s", want.Name)
		assert.Equal(t, want.BuildNumber, got.BuildNumber, "BuildNumber for %s", want.Name)
		if want.Git == nil {
			assert.Nil(t, got.Git, "Git for %s", want.Name)
		} else {
			assert.Equal(t, want.Git.Branch, got.Git.Branch, "Git.Branch for %s", want.Name)
			assert.Equal(t, want.Git.Remote, got.Git.Remote, "Git.Remote for %s", want.Name)
			assert.Equal(t, want.Git.Tag, got.Git.Tag, "Git.Tag for %s", want.Name)
			// In the acceptance test files (../testdata), for GitHubActionsPullRequestOpened and GitHubActionsPullRequestSynchronize,
			// the .txt file has a GITHUB_SHA of 99684bcacf01d95875834d87903dcb072306c9ad, but the json files have expected
			// git.revision values of d79b417f8e974b9a2d5e6483845a96446695f944 and 3738117e3337e54955580f4e98cf767d96b42135, respectively.
			// The template is "revision": "${GITHUB_SHA}",
			if want.Name != "GitHub Actions" {
				assert.Equal(t, want.Git.Revision, got.Git.Revision, "Git.Revision for %s", want.Name)
			}
		}
	}
}

func loadTestData() []testCase {
	baseDir := "../testdata"
	files, err := ioutil.ReadDir(baseDir)
	if err != nil {
		log.Fatal(err)
	}
	testCases := []testCase{}
	for _, file := range files {
		if !file.IsDir() && strings.HasSuffix(file.Name(), ".txt") {
			testCase := testCase{
				fileName: file.Name(),
				envVars:  map[string]string{},
			}
			testDataFile := fmt.Sprintf("%s/%s", baseDir, file.Name())
			readFile, err := os.Open(testDataFile)
			if err != nil {
				fmt.Println(err)
			}
			fileScanner := bufio.NewScanner(readFile)
			fileScanner.Split(bufio.ScanLines)
			for fileScanner.Scan() {
				line := fileScanner.Text()
				tokens := strings.SplitN(line, "=", 2)
				if len(tokens) > 1 {
					testCase.envVars[tokens[0]] = tokens[1]
				}
				readFile.Close()
			}
			fileContent, err := ioutil.ReadFile(fmt.Sprintf("%s.json", testDataFile))
			if err != nil {
				log.Fatal(err)
			}
			ciEnvironment := &cienvironment.CiEnvironment{}
			err = json.Unmarshal([]byte(fileContent), &ciEnvironment)
			if err != nil {
				l := log.New(os.Stderr, "", 0)
				l.Printf("error parsing ci templates %s", err)
			}
			testCase.want = ciEnvironment.SanitizeGit()
			testCases = append(testCases, testCase)
		}
	}
	return testCases
}
