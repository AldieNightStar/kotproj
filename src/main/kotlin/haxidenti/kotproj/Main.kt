package haxidenti.kotproj

import haxidenti.kotproj.DEFAULT_AUTHOR
import haxidenti.kotproj.generateProject

fun main(args: Array<String>) {
    if (args.size < 2) {
        println(
            """
                Please provide 2 arguments:
                    project_name package
                Examples:
                    proj1 com.project1     - will create proj1 with com.project1 package
            """.trimIndent()
        )
        return
    }
    val name = args[0]
    val second = args[1]
    generateProject(name, second, DEFAULT_AUTHOR)
    println("OK")
}


