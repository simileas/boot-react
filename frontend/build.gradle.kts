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
  version = "10.16.0"
  npmVersion = "6.4.1"
  distBaseUrl = "https://npm.taobao.org/mirrors/node/"
  download = true
  workDir = file("${project.buildDir}/node")
  npmWorkDir = file("${project.buildDir}/npm")
  nodeModulesDir = file("${project.projectDir}")
}

tasks.register<NpmTask>("npmInstallVerbose") {
  setArgs(listOf("install", "-verbose"))
}

tasks.register<NpmTask>("bundle") {
  dependsOn("npmInstallVerbose")
  setArgs(listOf("run", "build"))
}

tasks.named<DefaultTask>("build") {
  dependsOn("bundle")
}

tasks.register<NpmTask>("npmStartVerbose") {
  setArgs(listOf("start", "-verbose"))
}
