import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
        jcenter()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.10")
    }
}

plugins {
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10"
    `java-library`
}
group = "me.doreshnikov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.0")
    implementation("io.ktor:ktor-client-core:1.2.4")
    implementation("io.ktor:ktor-client-apache:1.2.4")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    testImplementation("com.xebialabs.restito:restito:0.9.3")
    testImplementation("org.slf4j:slf4j-jdk14:1.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")
}

kotlin.sourceSets {
    main {
        kotlin.setSrcDirs(listOf("main/kotlin"))
    }
    test {
        kotlin.setSrcDirs(listOf("test/kotlin"))
    }
}
sourceSets.main {
    resources.srcDir("main/resources")
}
sourceSets.test {
    resources.srcDir("test/resources")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}