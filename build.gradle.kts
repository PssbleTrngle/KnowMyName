plugins {
    id 'fabric-loom' version '0.12-SNAPSHOT'
    id 'maven-publish'
    id 'com.matthewprenger.cursegradle' version '1.4.0'
    id "com.modrinth.minotaur" version "2.+"
}

sourceCompatibility = JavaVersion.VERSION_17
targetCompatibility = JavaVersion.VERSION_17

def ENV = System.getenv()

def group = 'com.possible_triangle'

repositories {
    // Add repositories to retrieve artifacts from in here.
    // You should only use this when depending on other mods because
    // Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
    // See https://docs.gradle.org/current/userguide/declaring_repositories.html
    // for more information about repositories.
    gradlePluginPortal()
}

dependencies {
    // To change the versions see the gradle.properties file
    minecraft "com.mojang:minecraft:${project.minecraft_version}"
    mappings "net.fabricmc:yarn:${project.yarn_mappings}:v2"
    modImplementation "net.fabricmc:fabric-loader:${project.loader_version}"

    // Fabric API. This is technically optional, but you probably want it anyway.
    modImplementation "net.fabricmc.fabric-api:fabric-api:${project.fabric_version}"
}

processResources {
    inputs.property "version", project.version

    filesMatching("fabric.mod.json") {
        expand "version": project.version
    }
}

tasks.withType(JavaCompile).configureEach {
    // Minecraft 1.18 (1.18-pre2) upwards uses Java 17.
    it.options.release = 17
}

java {
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()
}

jar {
    from("LICENSE") {
        rename { "${it}_${project.mod_id}" }
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/pssbletrngle/${project.mod_id}")
            version = project.mod_version
            credentials {
                username = ENV.GITHUB_ACTOR
                password = ENV.GITHUB_TOKEN
            }
        }
    }
    publications {
        gpr(MavenPublication) {
            groupId = group
            artifactId = project.mod_id
            version = project.mod_version
            from(components.java)
        }
    }
}

def possible_minecraft_versions = project.possible_minecraft_versions.split(",").toList()

if (ENV.CURSEFORGE_TOKEN) curseforge {
    apiKey = ENV.CURSEFORGE_TOKEN
    project {
        id = project.curseforge_project_id
        possible_minecraft_versions.each {
            addGameVersion it
        }
        addGameVersion 'Fabric'
        changelog = ENV.CHANGELOG
        changelogType = 'markdown'
        releaseType = project.release_type
        mainArtifact(jar) {
            displayName = "Version ${project.mod_version}"
        }
    }
}

if (ENV.MODRINTH_TOKEN) modrinth {
    token = ENV.MODRINTH_TOKEN
    projectId = project.modrinth_project_id
    versionNumber = project.mod_version
    versionName = "Version ${project.mod_version}"
    changelog = ENV.CHANGELOG
    gameVersions = possible_minecraft_versions
    loaders = ["fabric"]
    versionType = project.release_type
    uploadFile = jar
}
