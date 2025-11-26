plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dep.mgmt)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    archiveFileName.set("weatherMcp.jar")
}

dependencies {
    implementation(platform(libs.spring.boot.bom))
    implementation(libs.spring.boot.starter.web)

    //implementation("org.springframework.ai:spring-ai-client-chat:1.1.0")
    //implementation("org.springframework.ai:spring-ai-mcp:1.1.0")
    implementation("org.springframework.ai:spring-ai-starter-mcp-server-webmvc:1.1.0")
    //implementation("org.springframework.ai:spring-ai-model:1.1.0")
    //implementation("org.springframework.ai:spring-ai-ollama:1.1.0")
    //implementation("org.springframework.ai:spring-ai-openai:1.1.0")

    //implementation("org.springframework.boot:spring-boot-starter-webflux")

    testImplementation(libs.spring.boot.starter.test)
}

tasks.withType<Test> {
    useJUnitPlatform()
}