rootProject.name = "ai-test"

pluginManagement {
    repositories {
        maven { url = uri("https://repo.spring.io/snapshot") }
        gradlePluginPortal()
    }
}

include("host")

include("weather-mcp")