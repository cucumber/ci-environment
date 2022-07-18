FOUND_GO_VERSION := $(shell go version)
EXPECTED_GO_VERSION = 1.17

copy-template:
	cp ../CiEnvironments.json CiEnvironments.json

check-go-version:
	@$(if $(findstring ${EXPECTED_GO_VERSION}, ${FOUND_GO_VERSION}),(exit 0),(echo Wrong go version! Please install ${EXPECTED_GO_VERSION}; exit 1))

test: copy-template check-go-version
	@echo "running all tests"
	@go install ./...
	@go fmt ./...
	@go run honnef.co/go/tools/cmd/staticcheck@v0.2.2 github.com/cucumber/ci-environment/go
	go vet ./...
	go test -race ./...
