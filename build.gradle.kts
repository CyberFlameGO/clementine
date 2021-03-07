plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "me.notom3ga"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://libraries.minecraft.net/")
}

dependencies {
    compileOnly("com.destroystokyo.paper", "paper-api", "1.16.5-R0.1-SNAPSHOT")
    implementation("com.mojang", "brigadier", "1.0.17")
    implementation("me.lucko", "commodore", "1.9")
}

tasks {
    withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.compilerArgs.add("-parameters")
    }

    withType<Javadoc> {
        options.encoding = Charsets.UTF_8.name()
    }

    shadowJar {
        archiveFileName.set("${rootProject.name}-${rootProject.version}.jar")
        exclude("com.mojang")
        include("me.lucko")
        relocate("me.lucko", "me.notom3ga.clementine.libs.me.lucko")
    }

    build {
        dependsOn(shadowJar)
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }

    repositories.maven {
        url = uri("https://repo.notom3ga.me/releases")
        credentials(PasswordCredentials::class)
    }
}