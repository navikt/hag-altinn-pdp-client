
rootProject.name = "altinn-pdp-client"

pluginManagement {
    val kotlinVersion: String by settings
    val kotlinterVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
        id("org.jmailen.kotlinter") version kotlinterVersion
        id("maven-publish")
    }
}
