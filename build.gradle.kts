import net.fabricmc.loom.api.LoomGradleExtensionAPI

plugins {
    id("net.somethingcatchy.gradle") version ("0.0.7")
}

val yarn_mappings: String by extra

fabric {
    mappings {
        dependencies.create("net.fabricmc:yarn:${yarn_mappings}:v2")
    }
}

enablePublishing()

val possibleMinecraftVersions = project.extra["possible_minecraft_versions"].toString().split(",")

uploadToCurseforge {
    minecraftVersions = possibleMinecraftVersions
}

uploadToModrinth {
    minecraftVersions = possibleMinecraftVersions
    syncBodyFromReadme()
}
