/* This file is auto-generated using npm run build-ci-environments */

import type { CiEnvironment } from './types.js'

export const CiEnvironments: readonly CiEnvironment[] = [
  {
    buildNumber: '${BUILD_BUILDNUMBER}',
    git: {
      branch: '${BUILD_SOURCEBRANCH/refs/heads/(.*)/\\1}',
      remote: '${BUILD_REPOSITORY_URI}',
      revision: '${BUILD_SOURCEVERSION}',
      tag: '${BUILD_SOURCEBRANCH/refs/tags/(.*)/\\1}',
    },
    name: 'Azure Pipelines',
    url: '${BUILD_BUILDURI}',
  },
  {
    buildNumber: '${bamboo_buildNumber}',
    git: {
      branch: '${bamboo_planRepository_branch}',
      remote: '${bamboo_planRepository_repositoryUrl}',
      revision: '${bamboo_planRepository_revision}',
    },
    name: 'Bamboo',
    url: '${bamboo_buildResultsUrl}',
  },
  {
    buildNumber: '${BUDDY_EXECUTION_ID}',
    git: {
      branch: '${BUDDY_EXECUTION_BRANCH}',
      remote: '${BUDDY_SCM_URL}',
      revision: '${BUDDY_EXECUTION_REVISION}',
      tag: '${BUDDY_EXECUTION_TAG}',
    },
    name: 'Buddy',
    url: '${BUDDY_EXECUTION_URL}',
  },
  {
    buildNumber: '${BITRISE_BUILD_NUMBER}',
    git: {
      branch: '${BITRISE_GIT_BRANCH}',
      remote: '${GIT_REPOSITORY_URL}',
      revision: '${BITRISE_GIT_COMMIT}',
      tag: '${BITRISE_GIT_TAG}',
    },
    name: 'Bitrise',
    url: '${BITRISE_BUILD_URL}',
  },
  {
    buildNumber: '${CIRCLE_BUILD_NUM}',
    git: {
      branch: '${CIRCLE_BRANCH}',
      remote: '${CIRCLE_REPOSITORY_URL}',
      revision: '${CIRCLE_SHA1}',
      tag: '${CIRCLE_TAG}',
    },
    name: 'CircleCI',
    url: '${CIRCLE_BUILD_URL}',
  },
  {
    buildNumber: '${CF_BUILD_ID}',
    git: {
      branch: '${CF_BRANCH}',
      remote: '${CF_COMMIT_URL/(.*)\\/commit.+$/\\1}.git',
      revision: '${CF_REVISION}',
    },
    name: 'CodeFresh',
    url: '${CF_BUILD_URL}',
  },
  {
    buildNumber: '${CI_BUILD_NUMBER}',
    git: {
      branch: '${CI_BRANCH}',
      remote: '${CI_PULL_REQUEST/(.*)\\/pull\\/\\d+/\\1.git}',
      revision: '${CI_COMMIT_ID}',
    },
    name: 'CodeShip',
    url: '${CI_BUILD_URL}',
  },
  {
    buildNumber: '${GITHUB_RUN_ID}',
    git: {
      branch: '${GITHUB_HEAD_REF}',
      remote: '${GITHUB_SERVER_URL}/${GITHUB_REPOSITORY}.git',
      revision: '${GITHUB_SHA}',
      tag: '${GITHUB_REF/refs/tags/(.*)/\\1}',
    },
    name: 'GitHub Actions',
    url: '${GITHUB_SERVER_URL}/${GITHUB_REPOSITORY}/actions/runs/${GITHUB_RUN_ID}',
  },
  {
    buildNumber: '${CI_JOB_ID}',
    git: {
      branch: '${CI_COMMIT_BRANCH}',
      remote: '${CI_REPOSITORY_URL}',
      revision: '${CI_COMMIT_SHA}',
      tag: '${CI_COMMIT_TAG}',
    },
    name: 'GitLab',
    url: '${CI_JOB_URL}',
  },
  {
    buildNumber: '${GO_PIPELINE_NAME}/${GO_PIPELINE_COUNTER}/${GO_STAGE_NAME}/${GO_STAGE_COUNTER}',
    git: {
      branch: '${GO_SCM_*_PR_BRANCH/.*:(.*)/\\1}',
      remote: '${GO_SCM_*_PR_URL/(.*)\\/pull\\/\\d+/\\1.git}',
      revision: '${GO_REVISION}',
    },
    name: 'GoCD',
    url: '${GO_SERVER_URL}/pipelines/${GO_PIPELINE_NAME}/${GO_PIPELINE_COUNTER}/${GO_STAGE_NAME}/${GO_STAGE_COUNTER}',
  },
  {
    buildNumber: '${BUILD_NUMBER}',
    git: {
      branch: '${GIT_LOCAL_BRANCH}',
      remote: '${GIT_URL}',
      revision: '${GIT_COMMIT}',
    },
    name: 'Jenkins',
    url: '${BUILD_URL}',
  },
  {
    buildNumber: '${JB_SPACE_EXECUTION_NUMBER}',
    git: {
      branch: '${JB_SPACE_GIT_BRANCH}',
      remote:
        'https://${JB_SPACE_API_URL}/p/${JB_SPACE_PROJECT_KEY}/repositories/${JB_SPACE_GIT_REPOSITORY_NAME}',
      revision: '${JB_SPACE_GIT_REVISION}',
    },
    name: 'JetBrains Space',
    url: '${JB_SPACE_EXECUTION_URL}',
  },
  {
    buildNumber: '${SEMAPHORE_JOB_ID}',
    git: {
      branch: '${SEMAPHORE_GIT_BRANCH}',
      remote: '${SEMAPHORE_GIT_URL}',
      revision: '${SEMAPHORE_GIT_SHA}',
      tag: '${SEMAPHORE_GIT_TAG_NAME}',
    },
    name: 'Semaphore',
    url: '${SEMAPHORE_ORGANIZATION_URL}/jobs/${SEMAPHORE_JOB_ID}',
  },
  {
    buildNumber: '${TRAVIS_JOB_NUMBER}',
    git: {
      branch: '${TRAVIS_BRANCH}',
      remote: 'https://github.com/${TRAVIS_REPO_SLUG}.git',
      revision: '${TRAVIS_COMMIT}',
      tag: '${TRAVIS_TAG}',
    },
    name: 'Travis CI',
    url: '${TRAVIS_BUILD_WEB_URL}',
  },
  {
    buildNumber: '${WERCKER_RUN_URL/.*\\/([^\\/]+)$/\\1}',
    git: {
      branch: '${WERCKER_GIT_BRANCH}',
      remote: 'https://${WERCKER_GIT_DOMAIN}/${WERCKER_GIT_OWNER}/${WERCKER_GIT_REPOSITORY}.git',
      revision: '${WERCKER_GIT_COMMIT}',
    },
    name: 'Wercker',
    url: '${WERCKER_RUN_URL}',
  },
]
