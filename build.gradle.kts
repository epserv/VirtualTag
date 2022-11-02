import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.apache.tools.ant.filters.ReplaceTokens
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    kotlin("jvm") version "1.7.10"
    `maven-publish`
}

group = "me.rerere"
version = "1.0.8"

repositories {
    mavenCentral()
    maven(url = "https://papermc.io/repo/repository/maven-public/")
    maven(url = "https://repo.dmulloy2.net/repository/public/")
    maven(url = "https://repo.codemc.io/repository/nms/")
    maven(url = "https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    val minecraftVersion = "1.17.1-R0.1-SNAPSHOT"
    compileOnly(group = "io.papermc.paper", name = "paper", version = minecraftVersion)
    compileOnly(group = "io.papermc.paper", name = "paper-api", version = minecraftVersion)
    val adventureVersion = "4.11.0"
    compileOnly(group = "net.kyori", name = "adventure-api", version = adventureVersion)
    compileOnly(group = "net.kyori", name = "adventure-text-minimessage", version = adventureVersion)
    compileOnly(group = "com.comphenix.protocol", name = "ProtocolLib", version = "4.7.0")
    compileOnly(group = "me.clip", name = "placeholderapi", version = "2.10.10")

    // Okhttp
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"

        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<KotlinCompile>{
        kotlinOptions.jvmTarget = "17"
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
        filteringCharset = "UTF-8"
        val properties = inputs.properties.map {
            it.key to it.value
        }.toMap(hashMapOf()).apply { this["version"] = version }
        filesMatching("plugin.yml") { expand(properties) }

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
