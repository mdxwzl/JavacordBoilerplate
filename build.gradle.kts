import org.gradle.api.JavaVersion.VERSION_17

plugins {
    kotlin("jvm") version "2.0.20"
    id("com.gradleup.shadow") version "8.3.6"
    application
}

val entrypoint = "tech.mdxwzl.MainKt"
group = entrypoint.substringBeforeLast(".")
version = "1.0-SNAPSHOT"

application {
    mainClass = entrypoint
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.javacord:javacord:3.8.0")
    implementation("net.oneandone.reflections8:reflections8:0.11.5")
    implementation("io.github.cdimascio:dotenv-kotlin:6.5.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
}

java {
    sourceCompatibility = VERSION_17
    targetCompatibility = VERSION_17
    withSourcesJar()
}

tasks.test {
    useJUnit()
}

kotlin {
    jvmToolchain(17)
}
