plugins {
    kotlin("jvm") version "2.0.21"
}

group = "cn.howxu.kotlin_httpserver"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.logging.log4j:log4j-core:2.24.3")
}

kotlin {
    jvmToolchain(21)
}