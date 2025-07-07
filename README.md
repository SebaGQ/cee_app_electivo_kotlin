# CEE APP

Esta es una aplicación de ejemplo para Android escrita en **Kotlin**. Utiliza la plantilla de "Navigation Drawer" de Android Studio y está configurada con las siguientes versiones:

- **Gradle** 8.11.1
- **Android Gradle Plugin** 8.9.1
- **Kotlin** 2.0.21
- **compileSdk / targetSdk**: 35
- **minSdk**: 24
- **JVM target**: 11

Las versiones de estas dependencias se definen en
`gradle/libs.versions.toml` y el wrapper de Gradle está configurado en
`gradle/wrapper/gradle-wrapper.properties`. Si necesitas compilar sin
conexión a Internet, asegúrate de contar con la distribución de Gradle
de forma local o modifica `distributionUrl` a una ruta `file://`.

La aplicación ahora carga únicamente la pantalla de **Elections**, que incluye su propia barra superior y de navegación inferior.

## Estructura

```
app/
 ├─ src/main/java/com/kotlin/cee_app/
 │   ├─ MainActivity.kt
 │   └─ ui/
 │       └─ elections/
 └─ src/test/... (pruebas unitarias)
```

## Cómo compilar

```bash
./gradlew build
```

Para ejecutar las pruebas unitarias puede usarse:

```bash
./gradlew test
```

Este repositorio no incluye un archivo `keystore`; por lo tanto, las builds de `release` no están firmadas por defecto.
