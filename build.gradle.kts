/*
 * This file is part of the Valkyrja Framework package.
 *
 * (c) Melech Mizrachi <melechmizrachi@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

plugins {
    java
    `maven-publish`
    signing
}

group = "io.valkyrja"
version = "26.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.valkyrja:valkyrja:26.0.0")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            pom {
                name.set("Valkyrja Template")
                description.set("A Java package template for the Valkyrja Framework.")
                url.set("https://github.com/valkyrjaio/java-template")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("melechmizrachi")
                        name.set("Melech Mizrachi")
                        email.set("melechmizrachi@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/valkyrjaio/java-template.git")
                    developerConnection.set("scm:git:ssh://github.com/valkyrjaio/java-template.git")
                    url.set("https://github.com/valkyrjaio/java-template")
                }
            }
        }
    }
    repositories {
        maven {
            name = "MavenCentral"
            url = uri("https://central.sonatype.com/api/v1/publisher/upload")
        }
    }
}

signing {
    sign(publishing.publications["maven"])
}

// CI tasks — run from the project root without cd-ing into each CI directory

tasks.register<GradleBuild>("spotlessCheck") {
    group = "CI"
    description = "Check code formatting via Spotless"
    dir = file(".github/ci/spotless")
    tasks = listOf("spotlessCheck")
}

tasks.register<GradleBuild>("spotlessApply") {
    group = "CI"
    description = "Apply code formatting via Spotless"
    dir = file(".github/ci/spotless")
    tasks = listOf("spotlessApply")
}

tasks.register<GradleBuild>("archunit") {
    group = "CI"
    description = "Run ArchUnit architecture tests"
    dir = file(".github/ci/archunit")
    tasks = listOf("test")
}

tasks.register<GradleBuild>("errorprone") {
    group = "CI"
    description = "Run Error Prone static analysis"
    dir = file(".github/ci/errorprone")
    tasks = listOf("build")
}

tasks.register<GradleBuild>("spotbugs") {
    group = "CI"
    description = "Run SpotBugs static analysis"
    dir = file(".github/ci/spotbugs")
    tasks = listOf("check")
}

tasks.register<GradleBuild>("junit") {
    group = "CI"
    description = "Run JUnit unit tests"
    dir = file(".github/ci/junit")
    tasks = listOf("test")
}

listOf("spotless", "archunit", "errorprone", "spotbugs", "junit").forEach { ci ->
    tasks.register<GradleBuild>("${ci}OutdatedCheck") {
        group = "CI"
        description = "Check $ci dependencies for available updates"
        dir = file(".github/ci/$ci")
        tasks = listOf("dependencyUpdates")
    }
}

tasks.register("outdatedCheck") {
    group = "CI"
    description = "Check all CI dependencies for available updates"
    dependsOn("spotlessOutdatedCheck", "archunitOutdatedCheck", "errorproneOutdatedCheck", "spotbugsOutdatedCheck", "junitOutdatedCheck")
}

tasks.register("ci") {
    group = "CI"
    description = "Run all CI checks"
    dependsOn("spotlessCheck", "archunit", "errorprone", "spotbugs", "junit")
}