import org.gradle.api.tasks.compile.JavaCompile

plugins {
    java
    `java-library`
    alias(libs.plugins.shadow)
}

description = "EasyJoin - Customizable join and quit actions plugin"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.extendedclip.com/releases/")
    maven("https://maven.enginehub.org/repo/")
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://repo.faststats.dev/releases")
}

dependencies {
    // Paper & Core
    compileOnly(libs.paper.api)
    compileOnlyApi(libs.mapacheeelib)
    annotationProcessor(libs.mapacheeelib)

    // Hooks
    compileOnly(libs.placeholderapi)
    compileOnly(libs.skinsrestorer.api)

    // Database
    api(libs.jdbi.core)
    api(libs.jdbi.sqlobject)
    api(libs.hikaricp)
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(25)
    options.compilerArgs.add("-parameters")
}

tasks {
    jar {
        archiveClassifier.set("plain")
    }
    processResources {
        filteringCharset = "UTF-8"
        filesMatching(listOf("plugin.yml", "paper-plugin.yml")) {
            expand(
                mapOf(
                    "version" to project.version,
                    "description" to project.description
                )
            )
        }
    }
    shadowJar {
        archiveBaseName.set("EasyJoin")
        archiveClassifier.set("")
        archiveVersion.set(project.version.toString())

        manifest {
            attributes(
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version,
                "Multi-Release" to "false"
            )
        }

        mergeServiceFiles()
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }
    build {
        dependsOn(shadowJar)
    }
}
