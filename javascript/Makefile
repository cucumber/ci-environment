SHELL := /usr/bin/env bash

help: ## Show this help
	@awk 'BEGIN {FS = ":.*##"; printf "\nUsage:\n  make <target>\n\nWhere <target> is one of:\n"} /^[$$()% a-zA-Z_-]+:.*?##/ { printf "  \033[36m%-15s\033[0m %s\n", $$1, $$2 } /^##@/ { printf "\n\033[1m%s\033[0m\n", substr($$0, 5) } ' $(MAKEFILE_LIST)
.PHONY: help

copy-ci-environment: ## Copy CiEnvironments.json and/or generate derived files
	echo "Nothing to do"
.PHONY: copy-ci-environment

clean-ci-environment: ## Remove CiEnvironments.json and any derived files
	echo "Nothing to do"
.PHONY: clean-ci-environment
