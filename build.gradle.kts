// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}

kotlin {
    jvmToolchain(11)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.hibernate.core)
    implementation(libs.flyway.core)
    implementation(libs.jakarta.persistence.api)
    testImplementation(libs.h2)
    testImplementation(libs.junit)
}