import com.google.cloud.tools.gradle.appengine.standard.AppEngineStandardExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Properties

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.31"

    // Spring
    id("org.springframework.boot") version "2.1.5.RELEASE"
    id("io.spring.dependency-management") version "1.0.7.RELEASE"
    id("org.jetbrains.kotlin.plugin.spring") version "1.3.31"

    // AppEngine deploy
    id("net.thauvin.erik.gradle.semver") version "0.9.9-beta"
    war
    id("com.google.cloud.tools.appengine").version("2.0.0-rc5")
    
}

repositories {
    jcenter()
    mavenCentral()
}

group = "com.dasiusp.cleber"

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Spring
    implementation("org.springframework.boot:spring-boot-starter-web")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    
    // PDF
    implementation(group = "com.itextpdf", name = "itextpdf", version = "5.5.13")

    testImplementation(group= "org.apache.pdfbox", name= "pdfbox", version= "2.0.15")

    // KotlinTest
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.3.2")
    testImplementation("io.kotlintest:kotlintest-extensions-spring:3.3.2")

    // Mockk
    testImplementation("io.mockk:mockk:1.9.3")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}


tasks.withType<Test> {
    useJUnitPlatform()
}

val props = Properties().apply {
    load(file("version.properties").inputStream())
}

configure<AppEngineStandardExtension> {
    deploy {
        projectId = "cleber-244103"
        stopPreviousVersion = true
        promote = true
        version = "${props["version.major"]}-${props["version.minor"]}-${props["version.patch"]}"
    }
}
springBoot {
    mainClassName = "endpoint.Application"
}