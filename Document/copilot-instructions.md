# Project testing instructions

This repository uses Spring Boot 3.3, JUnit 5, Mockito, Spring Kafka, WireMock, and Testcontainers.

## Testing taxonomy

We use three test categories:

1. Component tests
- Purpose: validate internal business logic within one application boundary.
- Scope: controller/service/repository combinations inside this service.
- External HTTP services must be mocked or stubbed.
- Kafka brokers, databases, and Redis should not be shared with external environments.
- Prefer Mockito for collaborators and WireMock only if HTTP boundary exists inside the component scope.

2. Contract tests
- Purpose: validate API/event contract between this service and consumers/providers.
- Scope: request/response schema, headers, field names, status codes, error body, event payload schema.
- Use Spring Cloud Contract or approved contract framework.
- Contract tests must verify backward compatibility expectations.
- Do not use broad business workflow assertions here.

3. Integration tests
- Purpose: validate real integration among application modules and infrastructure.
- Scope: HTTP + DB + Kafka + Redis + internal adapters.
- Use Testcontainers for Kafka/Postgres/Redis where applicable.
- Use WireMock for independent internal/external services unless the story explicitly asks to run a real dependent service container.
- Use Awaitility for async assertions. Never use Thread.sleep unless unavoidable.

## Naming rules

- Component tests: *ComponentTest
- Contract tests: *ContractTest
- Integration tests: *IT

## Folder conventions

- src/test/java/.../component
- src/test/java/.../contract
- src/test/java/.../integration

## Spring Boot conventions

- Use @SpringBootTest only when the full application context is required.
- Prefer slice tests where possible.
- For WebFlux, use WebTestClient.
- For MVC, use MockMvc.
- For Kafka integration tests, use Testcontainers Kafka.
- For external HTTP stubbing, use WireMock.

## Assertions

Each generated test should clearly contain:
- given
- when
- then

Each test must verify:
- HTTP status and payload if API is involved
- persistence state if DB is involved
- published or consumed Kafka event if messaging is involved
- error mapping if failure scenario is part of the story

## CI rules

- Tests must be deterministic.
- Do not depend on shared dev/staging services.
- Prefer local containers/stubs.
- Tests must run in CI with Maven commands documented in the repository.

## Output expectation

When generating tests, also generate:
- any missing test fixture builders
- test data factories
- WireMock stubs
- Testcontainers configuration
- README update if new test execution steps are introduced