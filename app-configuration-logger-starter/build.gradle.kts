plugins {
    `java-library`
}

group = "org.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {

    implementation("org.slf4j:slf4j-api:2.0.18")

    implementation("org.springframework.boot:spring-boot-autoconfigure:4.1.1")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:4.1.1")
}