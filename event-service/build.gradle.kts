import com.google.protobuf.gradle.*

plugins {
    alias(deps.plugins.kotlin.jvm)
    alias(deps.plugins.spring.boot)
    alias(deps.plugins.spring.dep)
    kotlin("plugin.spring") version "1.9.24"
    application
}

group = "com.ki.events"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

application {
    mainClass.set("com.ki.events.EventsApplication")
}

dependencies {
    implementation(project(":service-api"))
    implementation(project(":event-data-model"))

    // Spring
    implementation(deps.bundles.spring)
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // gRPC
    implementation(deps.bundles.grpc)
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")


    // Testing
    testImplementation(deps.bundles.testing)
    testImplementation(kotlin("test"))
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
    jvmToolchain(17)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

sourceSets {
    main {
        kotlin {
            srcDir("build/generated/source/proto/main/java")
            srcDir("build/generated/source/proto/main/grpc")
        }
    }
}
