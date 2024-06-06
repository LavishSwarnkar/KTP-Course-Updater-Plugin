plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.20"
    id("org.jetbrains.intellij") version "1.16.0"
}

group = "com.faangx.updater"
version = "1.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.yaml:snakeyaml:2.2")
    implementation("com.github.sya-ri:kgit:1.0.5")

    // Exclude SLF4J from dependencies
    configurations.all {
        exclude(group = "org.slf4j", module = "slf4j-api")
        exclude(group = "org.slf4j", module = "slf4j-simple")
    }
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2023.1.5")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf(/* Plugin Dependencies */))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("231")
        untilBuild.set("241.*")
    }

    signPlugin {
        certificateChainFile.set(
            File("secrets").resolve("CERTIFICATE_CHAIN.crt")
        )
        privateKeyFile.set(
            File("secrets").resolve("PRIVATE_KEY.pem")
        )
        password.set(
            File("secrets").resolve("PRIVATE_KEY_PWD.txt").readText()
        )
    }

    publishPlugin {
        token.set(
            File("secrets").resolve("PUBLISH_TOKEN.txt").readText()
        )
    }
}
