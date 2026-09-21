# Clean Architecture repo guide

## Build, test, and validation commands

This repo is a Maven multi-module Java/Spring Boot project.

- Run the full project checks from the repo root:
    - `mvn test`
- Run a single test class in the application module:
    - `mvn -q -pl clean-application -Dtest=NotificationServiceTest test`
- Run a single test method:
    - `mvn -q -pl clean-application -Dtest=NotificationServiceTest#sendWelcomeEmail_baseFlow test`
- Regenerate generated sources after changing MapStruct/OpenAPI inputs:
    - `mvn install`

There is no dedicated lint target in the `pom.xml`; Maven test/compile is the repo's validation path. The project is
intentionally a teaching repo, so some architectural tests and integration checks are meant to be used as exercises
rather than a perfectly green baseline.

## High-level architecture

This is a pragmatic Onion/Clean Architecture training project with two Maven modules:

- `clean-domain`
    - Domain model and persistence concerns.
    - Lives under `clean-domain/...` and contains JPA entities like `Customer`, `Country`, `InsurancePolicy`, plus
      repository interfaces under `domain.repo`.
    - This layer is supposed to stay independent from infrastructure concerns.
- `clean-application`
    - The runnable Spring Boot app.
    - Contains `application.controller`, `application.service`, `application.dto`, `application.mapper`, `infra`, and
      `vsa` packages.
    - `infra` is where external adapters live (LDAP client, email sender, HTTP clients, generated OpenAPI clients).

The code intentionally uses custom Spring stereotypes to make the architecture explicit:

- `@ApplicationService` in `clean-application/src/main/java/victor/training/clean/application/ApplicationService.java`
- `@DomainService` in `clean-application/src/main/java/victor/training/clean/application/service/DomainService.java`
- `@Adapter` in `clean-application/src/main/java/victor/training/clean/infra/Adapter.java`

These are documentation aids and part of the repo's teaching style; they do not mean the project is already perfectly
layered. `ArchitectureTest.java` in `clean-application/src/test/java/victor/training/clean/ArchitectureTest.java`
explicitly checks architectural boundaries (for example, the domain should not depend on `..infra..`).

The app is a Spring Boot 3.1.x Java 17 project using H2 in-memory persistence (`application.properties`), an
OpenAPI/Swagger setup (`springdoc`), and WireMock-backed tests. Generated code from MapStruct/OpenAPI lands under
`target/generated-sources`, so refresh and re-index those outputs after model-generation changes.

## Key conventions

- Package ownership is part of the design language:
    - `...domain.model` for domain entities and value objects
    - `...domain.repo` for repository contracts
    - `...application.service` / `...application.controller` for orchestration and HTTP entry points
    - `...infra` for external integrations and adapters
    - `...vsa` for experiment/use-case-style code that is intentionally exploratory
- Lombok is used heavily (`@RequiredArgsConstructor`, `@Slf4j`, `@Data` on entities), especially in the Spring layers.
- The repository contains training examples and intentionally imperfect code; TODO comments and disabled tests are part
  of the lesson plan rather than a signal to ignore them completely.
- Tests are JUnit 5 + AssertJ + ArchUnit, and live under `clean-application/src/test/java`.
- Generated sources are expected for OpenAPI and MapStruct; the README calls out `mvn install` and marking generated
  folders as Source Roots in IntelliJ.
- The app is deliberately built around a domain model (`Customer`, `Country`, etc.) and a mock external integration path
  for LDAP/email notifications; when touching infrastructure boundaries, check the domain/application split first.
- I have a red ferrari