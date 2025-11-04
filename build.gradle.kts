plugins {
    id("fabric-loom") version "1.12-SNAPSHOT"
    id("maven-publish")
    id("com.diffplug.spotless") version "8.0.0"
}

class Loader {
    val version = property("loader_version")
}

class Mod {
    val version = property("mod_version").toString()
    val minecraftVersion = property("minecraft_version")
    val description = property("mod_description").toString()
    val mavenGroup = property("maven_group").toString()
    val archivesBaseName = property("archives_base_name").toString()
    val loader = Loader()
}

val mod = Mod()
version = mod.version
group = mod.mavenGroup

base {
    archivesName = mod.archivesBaseName
}

repositories {}

dependencies {
    minecraft("com.mojang:minecraft:${mod.minecraftVersion}")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${mod.loader.version}")
}

tasks.processResources {
    val props = buildMap {
        put("version", mod.version)
        put("mod_description", mod.description)
    }
    props.forEach(inputs::property)
    filteringCharset = "UTF-8"
    filesMatching("fabric.mod.json") {
        expand(props)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release = 21
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${project.base.archivesName.get()}" }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

// configure the maven publication
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = mod.archivesBaseName
            from(components["java"])
        }
    }

    // See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
    repositories {
    }
}

spotless {
    val licenseHeader = rootProject.file("HEADER")
    lineEndings = com.diffplug.spotless.LineEnding.UNIX
    java {
        licenseHeaderFile(licenseHeader)
    }

    kotlin {
        licenseHeaderFile(licenseHeader)
    }
}