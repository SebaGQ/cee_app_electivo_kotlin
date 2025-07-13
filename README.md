# CEE Voting App

Esta aplicación de ejemplo, escrita en **Kotlin**, implementa un sistema de votaciones con autenticación básica y gestión local mediante **Room**. El proyecto utiliza el _Navigation Drawer_ de Android Studio y compila con las siguientes versiones:

- **Gradle** 8.11.1
- **Android Gradle Plugin** 8.9.1
- **Kotlin** 2.0.21
- **compileSdk / targetSdk**: 35
- **minSdk**: 24
- **JVM target**: 11

Las versiones de dependencias se especifican en `gradle/libs.versions.toml` y el wrapper se encuentra en `gradle/wrapper/gradle-wrapper.properties`.

## Árbol principal

```
app/
 └─ src/main/
     ├─ java/com/kotlin/cee_app/
     │   ├─ LoginActivity.kt
     │   ├─ SignUpActivity.kt
     │   ├─ MainActivity.kt
     │   ├─ data/
     │   │   ├─ AppDatabase.kt
     │   │   ├─ ElectionRepository.kt
     │   │   ├─ UserRepository.kt
     │   │   ├─ SessionManager.kt
     │   │   ├─ entities
     │   │   │   ├─ UsuarioEntity.kt
     │   │   │   ├─ AdminEntity.kt
     │   │   │   ├─ VotacionEntity.kt
     │   │   │   ├─ OpcionEntity.kt
     │   │   │   ├─ VotoEntity.kt
     │   │   │   └─ SimpleEntity.kt
     │   │   └─ daos
     │   │       ├─ UsuarioDao.kt
     │   │       ├─ AdminDao.kt
     │   │       ├─ VotacionDao.kt
     │   │       ├─ OpcionDao.kt
     │   │       ├─ VotoDao.kt
     │   │       └─ SimpleDao.kt
     │   └─ ui/
     │       ├─ elections/...
     │       ├─ results/...
     │       └─ users/...
     └─ res/
         ├─ layout/ (xml de pantallas y listas)
         └─ values/dimens.xml
```

## Funcionalidades principales

A continuación se listan las funciones implementadas y los archivos más relevantes que las componen (solo se mencionan entidades, DAOs, clases Kotlin, layouts y `dimens`).

### Autenticación

- **Clases:** `LoginActivity.kt`, `SignUpActivity.kt`, `SessionManager.kt`, `UserRepository.kt`.
- **Entidades:** `UsuarioEntity.kt`, `AdminEntity.kt`.
- **DAOs:** `UsuarioDao.kt`, `AdminDao.kt`.
- **Layouts:** `activity_login.xml`, `activity_sign_up.xml`.
- **Dimensiones:** `res/values/dimens.xml` y sus variantes.

### Gestión de usuarios (solo admins)

- **Clases:** `UsersFragment.kt`, `CreateUserFragment.kt`, `UserAdapter.kt`, `UsersViewModel.kt`, `CreateUserViewModel.kt`.
- **Entidades:** `UsuarioEntity.kt`.
- **DAO:** `UsuarioDao.kt`.
- **Layouts:** `fragment_users.xml`, `fragment_create_user.xml`, `item_usuario.xml`.
- **Dimensiones:** `res/values/dimens.xml`.

### Gestión y participación en votaciones

- **Clases:** `ElectionsFragment.kt`, `CreateElectionFragment.kt`, `VoteDialogFragment.kt`, `VoteDetailFragment.kt`, `VoteConfirmationFragment.kt`, `VotacionAdapter.kt`, `VoteDetailViewModel.kt`, `CreateElectionViewModel.kt`, `ElectionsViewModel.kt`.
- **Entidades:** `VotacionEntity.kt`, `OpcionEntity.kt`, `VotoEntity.kt`.
- **DAOs:** `VotacionDao.kt`, `OpcionDao.kt`, `VotoDao.kt`.
- **Layouts:** `fragment_elections.xml`, `fragment_create_election.xml`, `dialog_vote.xml`, `fragment_vote_detail.xml`, `fragment_vote_confirmation.xml`, `item_votacion.xml`, `item_vote_option.xml`.
- **Dimensiones:** `res/values/dimens.xml`.

### Resultados de votaciones

- **Clases:** `ResultsFragment.kt`, `DashboardAdapter.kt`, `OpcionResultAdapter.kt`, `ResultsViewModel.kt`, `DashboardUtils.kt`.
- **Entidades:** `VotacionEntity.kt`, `OpcionEntity.kt`, `VotoEntity.kt`.
- **DAOs:** `VotacionDao.kt`, `OpcionDao.kt`, `VotoDao.kt`.
- **Layouts:** `fragment_results.xml`, `item_dashboard.xml`, `item_result_option.xml`.
- **Dimensiones:** `res/values/dimens.xml`.

## Compilación

```bash
./gradlew build
```

Para ejecutar pruebas unitarias:

```bash
./gradlew test
```

Este repositorio no incluye un `keystore`; las builds de `release` no están firmadas por defecto.
