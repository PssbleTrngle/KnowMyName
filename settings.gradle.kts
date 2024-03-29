val mod_name: String by extra

pluginManagement {
    repositories {
        repositories {
            gradlePluginPortal()
            maven { url = uri("https://maven.fabricmc.net/") }
            maven { url = uri("https://repo.spongepowered.org/repository/maven-public/") }
        }
    }
}

rootProject.name = mod_name
include("common", "fabric", "forge")