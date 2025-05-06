import org.gradle.api.JavaVersion.VERSION_17

plugins {
    kotlin("jvm") version "2.0.20"
}

val mainClass = "tech.mdxwzl.MainKt"
group = mainClass.substringBeforeLast(".")
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.javacord:javacord:3.8.0")
    implementation("net.oneandone.reflections8:reflections8:0.11.5")
    implementation("io.github.cdimascio:dotenv-kotlin:6.5.1")
}

java {
    sourceCompatibility = VERSION_17
    targetCompatibility = VERSION_17
    withSourcesJar()
}

tasks.test {
    useJUnit()
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes["Main-Class"] = mainClass
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
}

kotlin {
    jvmToolchain(17)
}
