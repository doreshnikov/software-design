import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.10"
    `java-library`
}
group = "me.doreshnikov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")

    val scalaVersion = "2.12"
    val akkaVersion = "2.5.18"

    implementation("com.typesafe.akka", "akka-actor_$scalaVersion", akkaVersion)
    testImplementation("com.typesafe.akka", "akka-testkit_$scalaVersion", akkaVersion)

    implementation("org.http4k", "http4k-core", "4.4.1.0")
    implementation("org.http4k", "http4k-client-apache", "4.4.1.0")
    implementation("org.http4k", "http4k-server-apache", "4.4.1.0")
    implementation("com.beust", "klaxon", "5.5")
}

kotlin {
    sourceSets["main"].apply {
        kotlin.srcDir("main")
    }
    sourceSets["test"].apply {
        kotlin.srcDir("test")
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}