import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.apache.tools.ant.filters.ReplaceTokens
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.github.johnrengelman.shadow") version "6.1.0"
    kotlin("jvm") version "1.5.31"
    `maven-publish`
}

group = "me.rerere"
version = "1.0.7"

repositories {
    mavenCentral()
    maven(url = "https://hub.spigotmc.org/nexus/content/groups/public/")
    maven(url = "https://repo.dmulloy2.net/repository/public/")
    maven(url = "https://repo.codemc.io/repository/nms/")
    maven(url = "https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    compileOnly(group = "org.spigotmc", name = "spigot", version = "1.17.1-R0.1-SNAPSHOT")
    compileOnly(group = "org.spigotmc", name = "spigot-api", version = "1.17.1-R0.1-SNAPSHOT")
    compileOnly(group = "com.comphenix.protocol", name = "ProtocolLib", version = "4.7.0")
    compileOnly(group = "me.clip", name = "placeholderapi", version = "2.10.10")

    // Okhttp
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    withType<KotlinCompile>{
        kotlinOptions.jvmTarget = "1.8"
    }

    val fatJar by named("shadowJar", ShadowJar::class) {
        dependencies {
            exclude(dependency("org.slf4j:.*"))
        }
        minimize()
        relocate("kotlin", "me.rerere.virtualtag.thirdparty.kotlin")
        relocate("org.jetbrains", "me.rerere.virtualtag.thirdparty.org.jetbrains")
        relocate("org.intellij", "me.rerere.virtualtag.thirdparty.org.intellij")
        relocate("okhttp","me.rerere.virtualtag.thirdparty.okhttp")
        relocate("okio","me.rerere.virtualtag.thirdparty.okio")
    }

    artifacts {
        add("archives", fatJar)
    }

    processResources {
        from("src/main/resources") {
            include("**/*.yml")
            filter<ReplaceTokens>(
                "tokens" to mapOf(
                    "VERSION" to project.version
                )
            )
        }
        filesMatching("application.properties") {
            expand(project.properties)
        }
    }

    test {
        useJUnit()
    }

    build {
        dependsOn(shadowJar)
    }
}

configure<PublishingExtension> {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}
