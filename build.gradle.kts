plugins {
	java
	id("org.springframework.boot") version "3.5.3"
	id("io.spring.dependency-management") version "1.1.7"
	checkstyle
	jacoco
	id("org.sonarqube") version "4.4.1.3373"
}

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(22)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation ("org.springframework.boot:spring-boot-starter-web")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.jacocoTestReport {
	reports {
		xml.required.set(true)
		html.required.set(true)
		html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
	}
}

sonar {
	properties {
		property("sonar.projectKey", "Shturman13_java-project-99")
		property("sonar.organization", "shturman13")
		property("sonar.host.url", "https://sonarcloud.io")
		property("sonar.coverage.jacoco.xmlReportPaths", "app/build/reports/jacoco/test/jacocoTestReport.xml")
		property("sonar.token", System.getenv("SONAR_TOKEN") ?: "") // Использует токен из окружения
//		property("sonar.gradle.skipCompile", "true")
	}
}
