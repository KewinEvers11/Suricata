# AGENTS.md

Suricata: REST API for monitoring product prices. Spring Boot 4.1.1, Java 21, single Gradle module (group `org.oyabun`, base package `org.oyabun.suricata`).

## Commands

- Verify any change: `./gradlew test` — fast, no DB needed, all current tests are unit or web-slice.
- Single test: `./gradlew test --tests "org.oyabun.suricata.services.SolicitudMonitoreoServiceImplTest"`
- Full check: `./gradlew build` (same as `test` here; no separate lint/typecheck tasks exist).
- Run locally: `./gradlew bootRun --args='--spring.profiles.active=develop'` — the `develop` profile is required; there is no datasource config in the default `application.yaml`.

## Local setup

- The `develop` profile expects PostgreSQL at `localhost:5432` with database `suricata_app` and user `suricata_appuser` / password `sur1c4t4` — either installed locally (e.g. Homebrew) or via the `docker run postgres:18.6` command documented in `README.md`. Note: PG 18+ images mount the data volume at `/var/lib/postgresql`, not the pre-18 `/var/lib/postgresql/data`.
- Flyway runs on startup via a custom bean (`FlywayConfiguration`, `baselineOnMigrate(true)`) that reads `spring.datasource.*` directly — changes to datasource properties affect both JPA and migrations.
- The app serves under context path `/suricata-app` (develop profile). Controller mappings are relative to it, e.g. `POST /suricata-app/solicitud-monitoreo`. Springdoc UI is at `/suricata-app/swagger-ui/index.html`.

## Conventions

- Domain language is Spanish: classes (`SolicitudMonitoreo`), endpoints, validation messages, test `@DisplayName`s. Keep new code consistent.
- Layering: `web/controllers` → `services` (interface + `Impl`) → `repositories` (Spring Data JPA) → `models`. DTOs are `record`s in `web/model`.
- Entities extend `EntidadBase` (abstract `@Entity`, `TABLE_PER_CLASS` inheritance) which provides the UUID `id` and `created_at`/`updated_at`.
- IDs are assigned in `@PrePersist` via `com.fasterxml.uuid` time-based generator — never set IDs in production code.
- Entity↔DTO mapping uses MapStruct with `componentModel = SPRING`. String↔UUID conversion must go through `MapperUtils` via `@Mapping(expression=..., imports={MapperUtils.class})`; MapStruct won't convert them automatically.
- Lombok is used heavily (`@Getter`, `@Setter`, `@Builder`, `@RequiredArgsConstructor`). In `build.gradle`, Lombok must stay before MapStruct on the `annotationProcessor` path — order matters.
- `docs/models/productos.puml` describes planned entities (`ProductoMonitoreado`, `Sitio`, `ProductoSitio`, `RegistroPrecioProducto`, `ProductoReporte`). Only `SolicitudMonitoreo` is implemented so far.

## Testing

- JUnit 5 + Mockito + AssertJ. Two patterns in use: pure unit tests with `@ExtendWith(MockitoExtension.class)` (mappers wired with `@Spy` + `Mappers.getMapper(...)`), and controller slices with `@WebMvcTest`.
- Spring Boot 4 imports: use `org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest` and `org.springframework.test.context.bean.override.mockito.MockitoBean` — not the deprecated `@MockBean`.
- `SuricataApplicationTests.contextLoads` is `@Disabled` because it needs a live DB. DB-backed integration tests are planned but don't exist yet — keep new tests DB-free.
- Existing style: arrange/act/assert comments, constants for fixtures.

## Migrations

- Flyway SQL in `src/main/resources/db/migration`, versioned names like `V1.01__initial-entities-added.sql`.
- Never edit an already-applied migration; add a new versioned file. The existing file keeps a commented "REVERT SCRIPT" block at the bottom — follow that convention for new migrations.

## Workflow

- Feature branches + PRs; do not commit directly to `main`.
- `HELP.md` is the gitignored Spring Initializr leftover — don't rely on it.
