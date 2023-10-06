package haxidenti.kotproj

import java.nio.file.Files
import java.nio.file.Paths

fun generateProject(
    projectName: String,
    packageName: String,
    author: String,
) {
    // New directory
    Files.createDirectories(Paths.get(projectName))

    // Write new gradle build script
    Files.write(
        Paths.get(projectName, "build.gradle.kts"),
        newGradleSrc(author, packageName, projectName).toByteArray()
    )

    // new file settings.gradle.kts
    val settingsFile = """
        rootProject.name = "$projectName"
    """.trimIndent()

    // Write Gradle settings file
    Files.write(Paths.get(projectName, "settings.gradle.kts"), settingsFile.toByteArray())

    // Create sources
    val srcDir = Paths.get(projectName, "src", "main", "kotlin", *packageName.split(".").toTypedArray())
    Files.createDirectories(srcDir)
    val testDir = Paths.get(projectName, "src", "test", "kotlin", *packageName.split(".").toTypedArray())
    Files.createDirectories(testDir)

    // Main.kt
    Files.write(
        srcDir.resolve("Main.kt"), """
            package $packageName
            
            fun main(args: Array<String>) {
                println("Hello, Kotlin!")
            }
        """.trimIndent().toByteArray()
    )
}