plugins {
  id("org.springframework.boot") version "2.2.1.RELEASE"
  id("io.spring.dependency-management") version "1.0.8.RELEASE"
  id("java")
  id("com.gorylenko.gradle-git-properties") version "2.2.0"
  checkstyle
}

group = "cn.cas.common"
version = "1.0.0"

repositories {
  mavenCentral()
}

configure<JavaPluginConvention> {
  setSourceCompatibility(1.8)
  setTargetCompatibility(1.8)
}

val developmentOnly by configurations.creating
configurations.runtimeClasspath.extendsFrom(developmentOnly)

dependencies {
  implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:2.1.1")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.security:spring-security-test")
  implementation("org.projectlombok:lombok")

  developmentOnly("org.springframework.boot:spring-boot-devtools")
  runtimeOnly("com.h2database:h2")
  runtimeOnly("mysql:mysql-connector-java")
  testImplementation("org.springframework.boot:spring-boot-starter-test") {
    exclude("org.junit.vintage", "junit-vintage-engine")
  }

  annotationProcessor("org.projectlombok:lombok")
  testAnnotationProcessor("org.projectlombok:lombok")
  implementation("commons-codec:commons-codec:1.13")
  implementation("commons-io:commons-io:2.6")
  implementation("org.apache.commons:commons-lang3:3.9")
}

tasks.withType<Test> {
  useJUnitPlatform()
}

springBoot {
  buildInfo()
}

tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
  environment("spring.profiles.active", "dev")
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
  archiveName = "boot-react.jar"
  dependsOn(":frontend:build")
  from("../frontend/dist") {
    eachFile {
      println("Copy file to jar: ${name}")
    }
    into("static")
  }
  launchScript()
}

checkstyle {
  configFile = file("${configDir}/google_checks.xml")
  dependencies {
    checkstyle("com.puppycrawl.tools:checkstyle:8.31")
    checkstyle("com.github.sevntu-checkstyle:sevntu-checks:1.37.1")
  }
}
