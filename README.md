# backend-golden-path

![workflow](https://github.com/navikt/backend-golden-path/actions/workflows/main.yaml/badge.svg)

## Overview
This is a demo repository that offers a golden path for JVM projects.
The workflows are defined using GitHub Actions and are located in the `.github/workflows` directory.

## Workflows

### 1. Run test & build on PRs

This workflow is triggered on pull requests and performs the following steps:

- **Checkout the code**: Uses the `actions/checkout` action to checkout the code.
- **Setup Java**: Uses the `actions/setup-java` action to set up Java 21 with the Temurin distribution and cache Gradle dependencies.
- **Setup Gradle**: Uses the `gradle/actions/setup-gradle` action to set up Gradle & verify the gradle-wrapper.
- **Test & build**: Runs the `./gradlew test build` command to test and build the project.

Workflow file: `.github/workflows/prs.yaml`

### 2. Build and deploy main

This workflow triggers on push to main and when dependabot updates the dependencies.

- **Setup same as test workflow** (see above).
- ...
- **Build & push docker image + SBOM**: Uses the `nais/docker-build-push` action to build and push a Docker image and generate a Software Bill of Materials (SBOM) file.
- **Generate and submit dependency graph**: Uses the `gradle/actions/dependency-submission` action to generate and submit the dependency graph to Github.
- **Scan docker image for secrets**: Uses the `aquasecurity/trivy-action` action to scan the Docker image for secrets and generates a SARIF file.
- **Upload SARIF file**: Uses the `github/codeql-action/upload-sarif` action to upload the SARIF file.

Workflow file: `.github/workflows/main.yaml`

### 3. Dependabot auto-merge

This workflow is triggered on pull requests created by Dependabot and performs the following steps:

- **Fetch Dependabot metadata**: Uses the `dependabot/fetch-metadata` action to fetch metadata for the Dependabot pull request.
- **Auto-merge changes**: Uses the `gh pr merge` command to auto-merge Dependabot pull requests, except for major version updates, unless the package ecosystem is GitHub Actions.

Workflow file: `.github/workflows/dependabot-automerge.yml`

### 4. CodeQL Analysis

This workflow is triggered on push and pull requests to perform CodeQL analysis.

- **Checkout the code**: Uses the `actions/checkout` action to checkout the code.
- **Initialize CodeQL**: Uses the `github/codeql-action/init` action to initialize the CodeQL analysis.
- **Perform CodeQL analysis**: Uses the `github/codeql-action/analyze` action to perform the CodeQL analysis.

Workflow file: `.github/workflows/codeql.yml`

## License
[MIT](LICENSE).

## Contact

This project is maintained by [@appsec](https://github.com/orgs/navikt/teams/appsec).

Questions and/or feature requests? Please create an [issue](https://github.com/navikt/appsec-stats/issues).

If you work in [@navikt](https://github.com/navikt) you can reach us at the Slack channel [#appsec](https://nav-it.slack.com/archives/C06P91VN27M).


