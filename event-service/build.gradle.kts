import com.google.protobuf.gradle.*

plugins {
    alias(deps.plugins.kotlin.jvm)
    alias(deps.plugins.protobuf)
    alias(deps.plugins.proto.consume)
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
    protobuf(project(":service-api"))
    //proto domain converter dependencies
    implementation("io.gitlab.protobuf-tools:proto-domain-converter:1.3.1")
    annotationProcessor("io.gitlab.protobuf-tools:proto-domain-converter:1.3.1")


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

// todo
//protoConsume {
//    config {
//        generateLanguage("java")
//        useValidation()
//        useGrpc()
//    }
//}
//
//firstProtoTask.dependsOn ":service-api:assemble"
//firstProtoTask.dependsOn ":event-data-model:assemble"

protobuf {
    protoc {
        artifact = deps.plugins.protoc.get().toString()
    }

    plugins {
        id("grpc") {
            setArtifact(deps.plugins.grpcjava.get().toString())
        }
        id("grpckt") {
            setArtifact(deps.plugins.grpckotlin.get().toString())
        }
    }
    generateProtoTasks {
        all().forEach {
            if (it.name.startsWith("generateTestProto")) {
                it.dependsOn(":service-api:assemble")
                it.dependsOn(":event-data-model:assemble")
                it.dependsOn("jar")
            }

            it.plugins {
                id("grpc")
                id("grpckt")
            }
        }
    }
}

//tasks.clean {
//    project.delete("$projectDir/build/generated")
//}
