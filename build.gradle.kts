plugins {
    id("java")
    id("java-library")
    id("com.gradleup.shadow") version "9.3.0"
}

group = "me.espryth.easyjoin"
version = "3.0.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.extendedclip.com/releases/")
    maven("https://maven.enginehub.org/repo/")
    maven("https://repo.codemc.io/repository/maven-public/")
}


dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")

    compileOnlyApi("me.mapacheee:MapacheeeLib:1.0.0")
    annotationProcessor("me.mapacheee:MapacheeeLib:1.0.0")
    
    compileOnly("me.clip:placeholderapi:2.12.2")
    compileOnly("net.skinsrestorer:skinsrestorer-api:15.5.2")
    api("org.jdbi:jdbi3-core:3.45.4")
    api("org.jdbi:jdbi3-sqlobject:3.45.4")
    api("com.zaxxer:HikariCP:6.2.1")
}


tasks.shadowJar {

    manifest {
        attributes(
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version,
            "Multi-Release" to "false"
        )
    }

    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
