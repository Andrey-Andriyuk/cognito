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

## AWS Cognito + Spring Security demo

This project contains a simple Spring Boot app (`helloworld-app`) configured with Spring Security to authenticate users via AWS Cognito (OIDC) and validate JWTs for API access.

### What it demonstrates
- OAuth2 Login with AWS Cognito User Pool (browser sign-in)
- Resource Server JWT validation using Cognito issuer
- Public vs protected endpoints
  - `/` and `/actuator/health` are public
  - `/me` requires authentication and returns user claims

### Configure Cognito
1. Create a Cognito User Pool and a domain.
2. Create an App Client with following settings:
   - Allowed OAuth Flows: Authorization code grant
   - Allowed OAuth Scopes: `openid`, `email`, `profile`
   - Callback URL: `http://localhost:8080/login/oauth2/code/cognito`
   - Sign-out URL: `http://localhost:8080/`
3. Collect values:
   - Client ID and Client Secret
   - Issuer URI: `https://cognito-idp.<region>.amazonaws.com/<userPoolId>`

### Local configuration
Set these in `helloworld-app/src/main/resources/etc/helloworld-app.localrun.properties`:

```
spring.security.oauth2.client.registration.cognito.client-id=YOUR_CLIENT_ID
spring.security.oauth2.client.registration.cognito.client-secret=YOUR_CLIENT_SECRET
spring.security.oauth2.client.provider.cognito.issuer-uri=https://cognito-idp.<region>.amazonaws.com/<userPoolId>
spring.security.oauth2.resourceserver.jwt.issuer-uri=https://cognito-idp.<region>.amazonaws.com/<userPoolId>
```

Alternatively export environment variables to override Spring properties.

### Run locally
```
./gradlew :helloworld-app:bootRun -DENV_NAME=localrun
```

Open:
- `http://localhost:8080/` (public)
- `http://localhost:8080/me` (will redirect to Cognito login, or 401 if using API without token)
- `http://localhost:8080/actuator/health` (public)

### Use JWT for API calls
If you obtain an access token from Cognito, call:

```
curl -H "Authorization: Bearer <token>" http://localhost:8080/me
```

The response contains parsed claims when authenticated.
