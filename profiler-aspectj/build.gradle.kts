plugins {
    java
    id("io.freefair.aspectj.post-compile-weaving") version "5.3.0"
}

group = "me.doreshnikov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    jcenter()
}

dependencies {
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}