plugins {
    java
    jacoco
    id("org.sonarqube") version "6.0.1.5171"
    id("org.springframework.boot") version "3.2.6"
    id("io.spring.dependency-management") version "1.1.7"
    id("co.uzzu.dotenv.gradle") version "2.0.0"
    id("de.undercouch.download") version "5.3.0"
}

sonar {
    properties {
        property("sonar.projectKey", "auth-service-rizzerve")
        property("sonar.projectName", "auth-service-rizzerve")
    }
}

group = "rizzerve"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.spring.io/release")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.1")
    }
}

dependencies {
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    implementation("io.micrometer:micrometer-core")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("me.paulschwarz:spring-dotenv:4.0.0")
    implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")

    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")
    runtimeOnly("org.postgresql:postgresql")

    testImplementation("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
    filter {
        excludeTestsMatching("*FunctionalTest")
    }
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}
