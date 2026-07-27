languages = go java javascript python ruby

.DEFAULT_GOAL = help

help: ## Show this help
	@awk 'BEGIN {FS = ":.*##"; printf "\nUsage:\n  make <target>\n\nWhere <target> is one of:\n"} /^[$$()% a-zA-Z_-]+:.*?##/ { printf "  \033[36m%-15s\033[0m %s\n", $$1, $$2 } /^##@/ { printf "\n\033[1m%s\033[0m\n", substr($$0, 5) } ' $(MAKEFILE_LIST)

copy-ci-environment: $(patsubst %,copy-ci-environment-%,$(languages)) ## Copy CiEnvironment.json to all supported languages
.PHONY: copy-ci-environment

copy-ci-environment-%: %
	$(MAKE) --directory=$< copy-ci-environment
.PHONY: generate-%

clean-ci-environment: $(patsubst %,clean-ci-environment-%,$(languages)) ## Clean CiEnvironment.json
.PHONY: clean-ci-environment

clean-ci-environment-%: %
	$(MAKE) --directory=$< clean-ci-environment
.PHONY: clean-ci-environment-%
