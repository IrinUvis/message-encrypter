package cryptography

import java.io.File
import java.io.IOException
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
        val jarParentPath = File(this::class.java.protectionDomain.codeSource.location.toURI()).toPath().parent
        val configFilePath = jarParentPath.resolve("config.txt")
        if (Files.exists(configFilePath)) {
            try {
                var contents = Files.readAllBytes(configFilePath).decodeToString()
                val imagesDirectoryPath = contents.substring(
                    contents.indexOf(ABSOLUTE_IMAGES_PATH_CONFIG_PARAM) + ABSOLUTE_IMAGES_PATH_CONFIG_PARAM.length + 1,
                    contents.indexOf(SEPARATOR),
                )
                contents = contents.substring(ABSOLUTE_IMAGES_PATH_CONFIG_PARAM.length + imagesDirectoryPath.length + 3)
                val inputDirectoryName = contents.substring(
                    contents.indexOf(INPUT_IMAGES_FOLDER_NAME_PARAM) + INPUT_IMAGES_FOLDER_NAME_PARAM.length + 1,
                    contents.indexOf(SEPARATOR),
                )
                contents = contents.substring(INPUT_IMAGES_FOLDER_NAME_PARAM.length + inputDirectoryName.length + 3)
                val outputDirectoryName = contents.substring(
                    contents.indexOf(OUTPUT_IMAGES_FOLDER_NAME_PARAM) + OUTPUT_IMAGES_FOLDER_NAME_PARAM.length + 1,
                    contents.indexOf(SEPARATOR),
                )
                val inputImagesDirPath = Paths.get("$imagesDirectoryPath\\$inputDirectoryName")
                val outputImagesDirPath = Paths.get("$imagesDirectoryPath\\$outputDirectoryName")
                return if (inputImagesDirPath.isAbsolute and inputImagesDirPath.isDirectory()
                    and outputImagesDirPath.isAbsolute and outputImagesDirPath.isDirectory()) {
                    imagesAbsolutePath = imagesDirectoryPath
                    inputImagesFolderName = inputDirectoryName
                    outputImagesFolderName = outputDirectoryName
                    true
                } else {
                    false
                }
            } catch (e: Exception) {
                println("ERROR: Config file is corrupted!")
            }
        }
        println("Setup needs to be performed!")
        return false
    }

    fun setup(recommendedSettings: Boolean): Boolean {
        val jarParentPath = File(this::class.java.protectionDomain.codeSource.location.toURI()).toPath().parent
        val configFilePath = jarParentPath.resolve("config.txt")
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
            } catch (e: IOException) {
                Files.deleteIfExists(rootPath)
                println("ERROR: Can't create specified images root directory!")
                return false
            }
            try {
                if (!Files.exists(inputImagesPath)) {
                    Files.createDirectory(inputImagesPath)
                }
            } catch (e: IOException) {
                Files.deleteIfExists(inputImagesPath)
                println("ERROR: Can't create specified input directory!")
                return false
            }
            try {
                if (!Files.exists(outputImagesPath)) {
                    Files.createDirectory(outputImagesPath)
                }
            } catch (e: IOException) {
                Files.deleteIfExists(outputImagesPath)
                println("ERROR: Can't create specified output directory!")
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
            println("ERROR: Can't save provided setup information!")
            Files.deleteIfExists(configFilePath)
            return false
        }
        return true
    }
}
