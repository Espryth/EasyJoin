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

    api("com.thewinterframework:paper:1.0.4") {
        exclude(group = "io.papermc.paper", module = "paper-api")
        exclude(group = "com.google.guava", module = "guava")
        exclude(group = "com.google.code.gson", module = "gson")
    }
    annotationProcessor("com.thewinterframework:paper:1.0.4")

    api("com.thewinterframework:configuration:1.0.2") {
        exclude(group = "io.papermc.paper", module = "paper-api")
        exclude(group = "org.spongepowered", module = "configurate-core")
        exclude(group = "org.spongepowered", module = "configurate-hocon")
    }
    annotationProcessor("com.thewinterframework:configuration:1.0.2")

    api("org.spongepowered:configurate-core:4.2.0")
    api("org.spongepowered:configurate-hocon:4.2.0")
    api("com.thewinterframework:command:1.0.1") {
        exclude(group = "io.papermc.paper", module = "paper-api")
        exclude(group = "org.incendo", module = "cloud-paper")
        exclude(group = "org.incendo", module = "cloud-annotations")
        exclude(group = "org.incendo", module = "cloud-bukkit")
        exclude(group = "org.incendo", module = "cloud-core")
    }
    annotationProcessor("com.thewinterframework:command:1.0.1")

    api("org.incendo:cloud-paper:2.0.0-beta.10")
    api("org.incendo:cloud-annotations:2.0.0")
    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("net.skinsrestorer:skinsrestorer-api:15.5.2")

    api("org.jdbi:jdbi3-core:3.45.4")
    api("org.jdbi:jdbi3-sqlobject:3.45.4")
    api("com.zaxxer:HikariCP:6.2.1")
    //implementation("net.kyori:adventure-text-serializer-legacy:4.26.1")
    //implementation("net.kyori:adventure-text-minimessage:4.26.1")
}


tasks.shadowJar {
    minimize {
        exclude(dependency("com.thewinterframework:.*"))
        exclude(dependency("com.google.inject:.*"))
        exclude(dependency("org.incendo:.*"))
        exclude(dependency("org.spongepowered:.*"))
        exclude(dependency("org.jdbi:.*"))
        exclude(dependency("com.zaxxer:.*"))
    }

    relocate("com.thewinterframework", "me.espryth.easyjoin.libs.winter")
    relocate("com.google.inject", "me.espryth.easyjoin.libs.guice")
    relocate("org.incendo.cloud", "me.espryth.easyjoin.libs.cloud")
    relocate("javax.inject", "me.espryth.easyjoin.libs.inject")
    relocate("aopalliance", "me.espryth.easyjoin.libs.aopalliance")
    relocate("io.leangen.geantyref", "me.espryth.easyjoin.libs.geantyref")
    relocate("org.spongepowered.configurate", "me.espryth.easyjoin.libs.configurate")
    relocate("org.jdbi", "me.espryth.easyjoin.libs.jdbi")
    relocate("com.zaxxer.hikari", "me.espryth.easyjoin.libs.hikari")

    exclude("org/checkerframework/**")
    exclude("META-INF/maven/org.checkerframework/**")
    exclude("com/google/errorprone/**")
    exclude("com/google/j2objc/**")

    exclude("META-INF/MANIFEST.MF")
    exclude("META-INF/*.SF")
    exclude("META-INF/*.DSA")
    exclude("META-INF/*.RSA")
    exclude("META-INF/versions/**")
    exclude("META-INF/maven/**")
    exclude("META-INF/proguard/**")
    exclude("META-INF/native-image/**")
    exclude("module-info.class")
    exclude("**/module-info.class")
    exclude("META-INF/*.kotlin_module")

    archiveClassifier.set("")

    mergeServiceFiles()

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
