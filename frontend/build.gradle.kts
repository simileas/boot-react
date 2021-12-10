import com.moowork.gradle.node.npm.NpmTask

plugins {
  java
  id("com.moowork.node") version ("1.3.1")
}

apply(plugin = "com.moowork.node")

tasks.named<Jar>("jar") {
  enabled = false
}

node() {
  version = "12.20.0"
  npmVersion = "6.14.8"
  download = true
  workDir = file("${project.buildDir}/node")
  npmWorkDir = file("${project.buildDir}/npm")
  nodeModulesDir = file("${project.projectDir}")
}

tasks.register<NpmTask>("bundle") {
  dependsOn("npmInstall")
  setArgs(listOf("run", "build"))
}

tasks.named<DefaultTask>("build") {
  dependsOn("bundle")
}

tasks.register<NpmTask>("npmStartVerbose") {
  setArgs(listOf("start", "-verbose"))
}
