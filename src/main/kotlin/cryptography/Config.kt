package cryptography

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.isDirectory

class Config {
    companion object {
        const val ABSOLUTE_IMAGES_PATH_CONFIG_PARAM = "imagesFolderPath"
        const val INPUT_IMAGES_FOLDER_NAME_PARAM = "inputImagesFolderName"
        const val OUTPUT_IMAGES_FOLDER_NAME_PARAM = "outputImagesFolderName"
        private const val SEPARATOR = ";"
    }

    var imagesAbsolutePath: String = ""
    var inputImagesFolderName: String = ""
    var outputImagesFolderName: String = ""

    fun printConfigInfo() {
        println("Current input images path is: $imagesAbsolutePath\\$inputImagesFolderName")
        println("Current output images path is: $imagesAbsolutePath\\$outputImagesFolderName")
    }

    fun readConfig(): Boolean {
        val projectPath = Paths.get("").toAbsolutePath()
        val configFilePath = projectPath.resolve("src/main/resources/config.txt")
        if (!Files.exists(configFilePath)) {
            println("Setup needs to be performed!")
            return false
        }
        var contents = Files.readAllBytes(configFilePath).decodeToString()
        imagesAbsolutePath = contents.substring(
            contents.indexOf(ABSOLUTE_IMAGES_PATH_CONFIG_PARAM) + ABSOLUTE_IMAGES_PATH_CONFIG_PARAM.length + 1,
            contents.indexOf(SEPARATOR),
        )
        contents = contents.substring(ABSOLUTE_IMAGES_PATH_CONFIG_PARAM.length + imagesAbsolutePath.length + 3)
        inputImagesFolderName = contents.substring(
            contents.indexOf(INPUT_IMAGES_FOLDER_NAME_PARAM) + INPUT_IMAGES_FOLDER_NAME_PARAM.length + 1,
            contents.indexOf(SEPARATOR),
        )
        contents = contents.substring(INPUT_IMAGES_FOLDER_NAME_PARAM.length + inputImagesFolderName.length + 3)
        outputImagesFolderName = contents.substring(
            contents.indexOf(OUTPUT_IMAGES_FOLDER_NAME_PARAM) + OUTPUT_IMAGES_FOLDER_NAME_PARAM.length + 1,
            contents.indexOf(SEPARATOR),
        )
        val inputImagesDirPath = Paths.get("$imagesAbsolutePath\\$inputImagesFolderName")
        val outputImagesDirPath = Paths.get("$imagesAbsolutePath\\$outputImagesFolderName")
        return (inputImagesDirPath.isAbsolute and inputImagesDirPath.isDirectory()
                and outputImagesDirPath.isAbsolute and outputImagesDirPath.isDirectory())
    }

    fun setup(recommendedSettings: Boolean): Boolean {
        val projectPath = Paths.get("").toAbsolutePath()
        val configFilePath = projectPath.resolve("src/main/resources/config.txt")
        println("Images folder: ")
        val imagesFolderPath = Util.readInput()
        val inputImagesFolderName: String
        val outputImagesFolderName: String
        if (recommendedSettings) {
            inputImagesFolderName = "input"
            outputImagesFolderName = "output"
        } else {
            println("Input images folder name:")
            inputImagesFolderName = Util.readInput()
            println("Output images folder name:")
            outputImagesFolderName = Util.readInput()
        }
        try {
            val rootPath = Paths.get(imagesFolderPath)
            val inputImagesPath = rootPath.resolve(inputImagesFolderName)
            val outputImagesPath = rootPath.resolve(outputImagesFolderName)
            try {
                if (!Files.exists(rootPath)) {
                    Files.createDirectory(rootPath)
                }
                if (!Files.exists(inputImagesPath)) {
                    Files.createDirectory(inputImagesPath)
                }
                if (!Files.exists(outputImagesPath)) {
                    Files.createDirectory(outputImagesPath)
                }
            } catch (e: Exception) {
                Files.deleteIfExists(rootPath)
                Files.deleteIfExists(inputImagesPath)
                Files.deleteIfExists(outputImagesPath)
                println("ERROR: Can't create specified input and output directories!")
                return false
            }
        } catch (e: Exception) {
            println("ERROR: Incorrect input has been specified!")
            return false
        }
        val contents = """
                    imagesFolderPath=$imagesFolderPath;
                    inputImagesFolderName=$inputImagesFolderName;
                    outputImagesFolderName=$outputImagesFolderName;
                """.trimIndent()
        val bytes = contents.encodeToByteArray()
        try {
            Files.deleteIfExists(configFilePath)
            Files.createFile(configFilePath)
            Files.write(configFilePath, bytes)
        } catch (e: Exception) {
            Files.deleteIfExists(configFilePath)
            println("ERROR: Can't save provided setup information!")
            return false
        }
        return true
    }
}
