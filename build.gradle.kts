import build.buf.gradle.CompressionFormat
import build.buf.gradle.GENERATED_DIR
import build.buf.gradle.ImageFormat

plugins {
    alias(deps.plugins.kotlin.jvm)
    alias(deps.plugins.spring.boot)
    alias(deps.plugins.spring.dep)
    kotlin("plugin.spring") version "1.9.24"
    id("build.buf") version "0.9.1"
    application
}

group = "com.ki"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

tasks.named("compileJava").configure { dependsOn("bufGenerate") }
tasks.named("compileKotlin").configure { dependsOn("bufGenerate") }

sourceSets["main"].java { srcDir("$buildDir/bufbuild/$GENERATED_DIR/gen/jvm") }
sourceSets["main"].kotlin { srcDir("$buildDir/bufbuild/$GENERATED_DIR/gen/jvm") }

repositories {
    mavenCentral()
}

application {
    mainClass.set("com.ki.events.EventsApplication")
}

dependencies {
    // Spring
    implementation(deps.bundles.spring)
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // gRPC
    implementation(deps.bundles.grpc)
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("build.buf:protovalidate:0.2.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.2")
    implementation("org.springframework.boot:spring-boot-starter-aop")

    // Testing
    testImplementation(deps.bundles.testing)
    testImplementation(kotlin("test"))

    implementation("io.grpc:grpc-kotlin-stub:1.4.1")
    implementation("io.grpc:grpc-protobuf:1.66.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.3")
    implementation("com.google.protobuf:protobuf-kotlin:3.25.3")
    implementation("com.google.protobuf:protobuf-java:3.25.3")

    if (JavaVersion.current().isJava9Compatible) {
        // Workaround for @javax.annotation.Generated
        // see: https://github.com/grpc/grpc-java/issues/3633
        implementation("javax.annotation:javax.annotation-api:1.3.1")
    }
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

buf {
    configFileLocation = rootProject.file("buf.yaml")
    enforceFormat = true // true by default
    generate {
        includeImports = true
    }
    build {
        imageFormat = ImageFormat.JSON // JSON by default
        compressionFormat = CompressionFormat.GZ // null by default (no compression)
    }
}
