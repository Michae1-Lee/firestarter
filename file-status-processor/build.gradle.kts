plugins {
	java
	id("org.springframework.boot") version "3.5.5"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.file-status-processor"
version = "0.0.1-SNAPSHOT"
description = "file-status-processor"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

extra["springCloudVersion"] = "2025.0.0"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.cloud:spring-cloud-starter-config")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.kafka:spring-kafka")
	testImplementation ("org.testcontainers:testcontainers:1.19.8")
	testImplementation ("org.testcontainers:junit-jupiter:1.19.8")
	testImplementation ("org.testcontainers:kafka:1.19.8")
	testImplementation ("org.testcontainers:mongodb:1.19.8")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.kafka:spring-kafka-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation ("org.mockito:mockito-core:5.11.0")
	testImplementation ("org.mockito:mockito-junit-jupiter:5.11.0")
	testImplementation ("org.awaitility:awaitility:4.2.0")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
