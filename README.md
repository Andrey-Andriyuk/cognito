## Commit message validation

Commit messages are automatically validated against the
[Conventional Commits](https://www.conventionalcommits.org) specification
using [commitlint](https://commitlint.js.org) via a Git hook.

### Prerequisites
This requires a working [Node.js](https://nodejs.org) and `npm` installation.
Installation of Node.js and npm is outside the scope of this guide.

### Setup
Run once after cloning the repository:

```bash
make bootstrap
```

### Usage

On every commit, the `commit-msg` hook will run and reject non-conformant messages.

### Commit Messages

Commits **must** follow the [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/) specification.
Commit message format is checked automatically in each PR.

**Examples:**
- `feat: add support for Azure Blob storage`
- `fix: correct null pointer in config loader`
- `refactor: simplify Kafka consumer initialization`
- `ci: update GitHub Actions workflow for semantic-release`

**Breaking changes:**
- `feat!: change feature flag evaluation API`

**With Jira ticket:**
- `feat(VD-123): add new Kafka message serializer`
- `fix(VD-456)!: remove deprecated endpoint`

**Allowed commit types:**
- `build`: Changes that affect the build system or external dependencies.
- `chore`: Maintenance tasks that don't affect application code (e.g., dependency bumps).
- `ci`: Changes to CI/CD configuration or scripts.
- `docs`: Documentation-only changes.
- `feat`: A new feature.
- `fix`: A bug fix.
- `perf`: Code changes that improve performance.
- `refactor`: Code changes that neither fix a bug nor add a feature.
- `revert`: Revert a previous commit.
- `style`: Changes that do not affect the meaning of the code (e.g., formatting, missing semicolons).
- `test`: Adding or modifying tests.
