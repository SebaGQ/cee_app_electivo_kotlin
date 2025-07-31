# Contributor (Agent) Guide – CEE Voting App

## 1  Repo Layout

```
workspace/
 ├─ cee_app_electivo_kotlin/     # ← this repo root (REPO_DIR)
 │   ├─ app/                     # Android module
 │   ├─ src/main/resources/      # Flyway migrations
 │   ├─ build.gradle.kts         # Root build file (Kotlin DSL)
 │   └─ settings.gradle.kts      # Gradle modules list
```

---

## 2  Dev‑Environment Tips

| Topic                 | Command / Hint                                                                            |
| --------------------- | ----------------------------------------------------------------------------------------- |
| **Jump to a path**    | `cd app/src/main/java/com/kotlin/cee_app` – main sources live here. |
| **Add libs**          | `./gradlew :app:dependencies` to inspect; edit `build.gradle.kts` then `./gradlew build`. |
| **New DB migration**  | Place SQL in `resources/db/migration/V<timestamp>__name.sql` (Flyway).                    |
| **Generate entity**   | Use Kotlin `data class` + Room annotations.                                      |
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

| Phase | Scope                          | Key packages             |
| ----- | ------------------------------ | ------------------------ |
| 1     | Base app y capa de datos       | `data`, `repository`     |
| 2     | Pantallas de autenticación     | `ui.auth`, `SessionManager` |
| 3     | Gestión de votaciones          | `ui.elections`, `repository` |
| 4     | Registro de votos              | `ui.elections`, `work`   |
| 5     | Dashboard de resultados        | `ui.results`             |
| 6     | Administración de usuarios     | `ui.users`               |
| 7     | Tareas de fondo y pruebas      | `work`, `tests`          |

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

* Kotlin 1.9 + JVM 17 target.
* `ktlint + detekt` are enforced – run them before pushing.
* Null‑safety: prefer nullable types over `lateinit`.
* Use `suspend` + Coroutines for DB/service calls; expose Flow for streams.
* Commit messages: `<RF‑ID> short description`, e.g. `RF3.2 validate voting dates`.

---
