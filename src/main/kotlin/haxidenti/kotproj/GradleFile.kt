package haxidenti.kotproj

// Create gradle file
fun newGradleSrc(author: String, packageName: String, projectName: String): String {
    return """
        import java.net.URI
        
        val MAIN_CLASS = "$packageName.MainKt"
        val AUTHOR = "$author"
        val VERSION = "1.0.0"
        val CLI_NAME = "$projectName"
        
        plugins {
            kotlin("jvm") version "$KOTLIN_VER"
            `maven-publish`
            id("com.github.johnrengelman.shadow") version "8.1.1"
            id("application")
        }
        
        group = AUTHOR
        version = "1.0.0"
        
        repositories {
            mavenCentral()
            mavenLocal()
            maven { url = URI("https://jitpack.io") }
        }
        
        dependencies {
            // Additional libs
            implementation("com.google.code.gson:gson:2.10.1")
            
            // Internal libs
            implementation(kotlin("stdlib-jdk8"))
            testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
            testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
        }
        
        application {
            mainClass.set(MAIN_CLASS)
        }
        
        tasks.register("cli") {
            group = "cli"
            dependsOn("shadowJar")
            doFirst {
                val task = tasks.getByName("shadowJar")
                task.actions[0].execute(this)

                val libs = File("./build/libs")
                val jar = libs.walk()
                    .firstOrNull { it.nameWithoutExtension.endsWith("-all") } ?: return@doFirst
                val rootJar = File("./${'$'}CLI_NAME.jar")
                val rootScript = File(rootJar.nameWithoutExtension)
                rootScript.delete()
                rootScript.writeText(
                    "#!/bin/bash\n"
                    + "SCRIPTPATH=\"\${'$'}( cd -- \"\${'$'}(dirname \"\${'$'}0\")\" >/dev/null 2>&1 ; pwd -P )\"\n"
                    + "java -cp \"\${'$'}SCRIPTPATH/${'$'}{rootJar.name}\" ${'$'}MAIN_CLASS \"\${'$'}@\""
                )
                rootJar.delete()
                jar.copyTo(rootJar)
                File("build").deleteRecursively()
            }
        }
        
        tasks.test {
            useJUnitPlatform()
        }
        
        publishing {
            publications {
                create<MavenPublication>("maven") {
                    groupId = AUTHOR
                    artifactId = project.name
                    version = VERSION
                    
                    from(components["java"])
                }
            }
        }
    """.trimIndent()
}