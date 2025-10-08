plugins {
    buildlogic.`kotlin-common-conventions-no-detekt`
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
}

tasks.test {
    doFirst {
        project.projectDir.resolve("src/test/resources")?.listFiles()?.forEach {
            it.deleteRecursively()
        }
    }
}