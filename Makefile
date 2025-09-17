# Use Bash with strict flags
SHELL := /usr/bin/env bash
.SHELLFLAGS := -eu -o pipefail -c
GRADLE := sh ./gradlew --stacktrace

# -------- ECR settings (override as needed) -----
ECR_AWS_REGION       ?= eu-central-1
ECR_AWS_ACCOUNT_ID   ?= 099100561859
ECR_REGISTRY         ?= $(ECR_AWS_ACCOUNT_ID).dkr.ecr.$(ECR_AWS_REGION).amazonaws.com

help:
	@echo "Targets:"
	@echo "  bootstrap        Install commitlint deps and register commit-msg hook"
	@echo "  unbootstrap      Remove commit-msg hook"
	@echo "  ecr-login-docker Docker login to AWS ECR (uses ECR_AWS_REGION/ECR_AWS_ACCOUNT_ID)"
	@echo "  ecr-login-helm   Helm repository login to AWS ECR (uses ECR_AWS_REGION/ECR_AWS_ACCOUNT_ID)"
	@echo "  local            Publish to Maven local"
	@echo "  boot             Build Spring Boot fat JAR"
	@echo "  build            Gradle build"
	@echo "  coverage         Unit tests + JaCoCo report"
	@echo "  int-coverage     Integration tests + JaCoCo report"
	@echo "  format           Apply Spotless formatting"
	@echo "  spot             Check Spotless formatting"
	@echo "  clean            Gradle clean"

# -------- Git hook: commitlint ------------------
bootstrap:
	cd .githooks && npm install --no-audit --no-fund
	ln -sfv ../../.githooks/commit-msg .git/hooks/commit-msg

unbootstrap:
	rm -fv .git/hooks/commit-msg

# -------- AWS ECR login -------------------------
ecr-login-docker:
	aws ecr get-login-password --region $(ECR_AWS_REGION) \
	        | docker login $(ECR_REGISTRY)  --username AWS --password-stdin

ecr-login-helm:
	aws ecr get-login-password --region $(ECR_AWS_REGION) \
	        | helm registry login $(ECR_REGISTRY) --username AWS --password-stdin

# -------- Gradle workflows ---------------------
local:
	${GRADLE} publishToMavenLocal

boot:
	${GRADLE} bootJar

build:
	${GRADLE} build

coverage:
	${GRADLE} test jacocoTestReportAggregate

int-coverage:
	${GRADLE} integrationTest jacocoIntegrationTestReportAggregate

format:
	${GRADLE} spotlessApply

spot:
	${GRADLE} spotlessCheck
