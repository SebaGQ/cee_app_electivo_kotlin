# Contributor (Agent) Guide – CEE Voting App

## 1  Repo Layout

```
workspace/
 ├─ cee_app_electivo_kotlin/     # ← this repo root (REPO_DIR)
 │   ├─ app/                     # Android module (UI demo – Navigation Drawer)
 │   ├─ build.gradle.kts         # Root build file (uses Kotlin DSL)
 │   └─ settings.gradle.kts      # Gradle modules list
```

---

## 2  Dev‑Environment Tips

| Topic                 | Command / Hint                                                                            |
| --------------------- | ----------------------------------------------------------------------------------------- |
| **Jump to a path**    | `cd app/src/main/kotlin/com/cee/…` – code lives here, not in the template demo.           |
| **Add libs**          | `./gradlew :app:dependencies` to inspect; edit `build.gradle.kts` then `./gradlew build`. |
| **New DB migration**  | Place SQL in `resources/db/migration/V<timestamp>__name.sql` (Flyway).                    |
| **Generate entity**   | Use Kotlin `data class` + JPA/Hibernate annotations.                                      |
| **Run a single test** | `./gradlew test --tests "*VotingServiceTest"`                                             |
| **Static analysis**   | `./gradlew lintKotlin detekt` (configured).                                               |
| **Format**            | `./gradlew ktlintFormat` – commit after formatting.                                       |

---

## 3  Branch & PR Policy

* **Git Flow**: create branches as `feature/phase<𝐍>-rf<𝐌>‑short‑slug` (e.g. `feature/phase3-rf3.1-create-voting`).
* One RF per PR whenever possible.
* Every PR **must**: compile (`./gradlew build`), pass tests, and add tests for new logic.

---

## 4  Architecture Roadmap

The backlog is split into **seven phases** matching the functional spec:

| Phase | Scope                                    | Key packages                         |
| ----- | ---------------------------------------- | ------------------------------------ |
| 1     | Project skeleton, entities, repositories | `model`, `repository`, `config`      |
| 2     | Users & auth (BCrypt + JWT)              | `security`, `service.auth`           |
| 3     | Admin creates votings                    | `controller.admin`, `service.voting` |
| 4     | Student votes                            | `controller.user`, `service.vote`    |
| 5     | Results dashboards                       | `service.results`, `dto`             |
| 6     | Advanced admin ops                       | `controller.admin`                   |
| 7     | Notifications, tests, optimisations      | various                              |

Each agent should **focus on one RF at a time**. Update this table if you create additional modules.

---

## 5  Database Conventions

* **PostgreSQL** in dev (H2 for unit tests).
* Use **Flyway** → filenames `V<yyyymmddHHMMSS>__<short_name>.sql`.
* SQL in migrations **only**; no schema changes inside application code.

---

## 6  Testing Instructions

1. Local run:

   ```bash
   ./gradlew clean build lintKotlin detekt
   ```
2. Focus a single test:  `./gradlew test --tests "*SomeTestName"`.
3. UI tests go under `src/androidTest/kotlin` (use Espresso).

---

## 7  Coding Standards

* Kotlin 2.0 + JVM 11 target.
* `ktlint + detekt` are enforced – run them before pushing.
* Null‑safety: prefer nullable types over `lateinit`.
* Use `suspend` + Coroutines for DB/service calls; expose Flow for streams.
* Commit messages: `<RF‑ID> short description`, e.g. `RF3.2 validate voting dates`.

---
