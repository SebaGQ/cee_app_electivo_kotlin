# CEE APP

Esta es una aplicación de ejemplo para Android escrita en **Kotlin**. Utiliza la plantilla de "Navigation Drawer" de Android Studio y está configurada con las siguientes versiones:

- **Gradle** 8.11.1
- **Android Gradle Plugin** 8.9.1
- **Kotlin** 2.0.21
- **compileSdk / targetSdk**: 35
- **minSdk**: 24
- **JVM target**: 11

La aplicación implementa tres pantallas simples (Home, Gallery y Slideshow) que muestran texto estático y pueden navegarse mediante el menú lateral.

## Estructura

```
app/
 ├─ src/main/java/com/kotlin/cee_app/
 │   ├─ MainActivity.kt
 │   └─ ui/
 │       ├─ home/
 │       │   ├─ HomeFragment.kt
 │       │   └─ HomeViewModel.kt
 │       ├─ gallery/
 │       │   ├─ GalleryFragment.kt
 │       │   └─ GalleryViewModel.kt
 │       └─ slideshow/
 │           ├─ SlideshowFragment.kt
 │           └─ SlideshowViewModel.kt
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
