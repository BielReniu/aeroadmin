pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    // Permet gestionar automàticament JDKs via Foojay
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

dependencyResolutionManagement {
    repositories {
        // Repositori on trobaràs el conector MySQL i la llibreria ASCII-Table
        mavenCentral()
    }
}

rootProject.name = "aeroadmin"
include(
    "app",
    "utilities",
    "model",
    "repositories",
    "jdbc",
    "jpa",
    "server",
    "clients:console"
)
