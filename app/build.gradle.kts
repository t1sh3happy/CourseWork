plugins {
    id("buildsrc.convention.kotlin-jvm")
    kotlin("plugin.serialization") version "2.0.21"  // <-- УКАЖИТЕ ВАШУ ВЕРСИЮ KOTLIN
    application
}

dependencies {
    implementation(project(":utils"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
}

application {
    mainClass = "com.automation.app.AppKt"
}