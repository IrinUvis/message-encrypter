package cryptography

import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.exists
import kotlin.io.path.isDirectory


class Config {
    companion object {
        const val ABSOLUTE_IMAGES_PATH_CONFIG_PARAM = "imagesFolderPath"
        const val INPUT_IMAGES_FOLDER_NAME_PARAM = "inputImagesFolderName"
        const val OUTPUT_IMAGES_FOLDER_NAME_PARAM = "outputImagesFolderName"
        private const val SEPARATOR = ";"
    }

    var configurationIsFine = false
    var imagesAbsolutePath: String = ""
    var inputImagesFolderName: String = ""
    var outputImagesFolderName: String = ""

    fun getImageDirPath(isInput: Boolean): String {
        return if (isInput)
            "$imagesAbsolutePath\\$inputImagesFolderName"
        else
            "$imagesAbsolutePath\\$outputImagesFolderName"
    }

    fun printConfigInfo() {
        Printer.info("Current input images path is: ${getImageDirPath(true)}")
        Printer.info("Current output images path is: ${getImageDirPath(false)}")
    }

    fun printImagesList(input: Boolean) {
        val path =
            if (input) Paths.get(getImageDirPath(true))
            else Paths.get(getImageDirPath(false))
        if (path.exists() && path.isDirectory()) {
            Printer.info("Files with .png extension in $path:")
            path.toFile()
                .walk()
                .filter { it.isFile }
                .filter { it.extension == "png" }
                .map { it.name }
                .forEach { Printer.standard("- $it") }
        } else {
            Printer.error("The setup needs to be redone!")
            configurationIsFine = false
        }
    }

    fun loadConfig() {
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
                if (inputImagesDirPath.isAbsolute and inputImagesDirPath.isDirectory()
                    and outputImagesDirPath.isAbsolute and outputImagesDirPath.isDirectory()
                ) {
                    imagesAbsolutePath = imagesDirectoryPath
                    inputImagesFolderName = inputDirectoryName
                    outputImagesFolderName = outputDirectoryName
                    configurationIsFine = true

                } else {
                    configurationIsFine = false
                }
                return
            } catch (e: Exception) {
                Printer.error("Config file is corrupted!")
                configurationIsFine = false
                return
            }
        }
        Printer.info("Setup needs to be performed!")
        configurationIsFine = false

    }

    fun setup(recommendedSettings: Boolean): Boolean {
        val jarParentPath = File(this::class.java.protectionDomain.codeSource.location.toURI()).toPath().parent
        val configFilePath = jarParentPath.resolve("config.txt")
        Printer.standard("Images folder: ")
        val imagesFolderPath = Util.readInput()
        val inputImagesFolderName: String
        val outputImagesFolderName: String
        if (recommendedSettings) {
            inputImagesFolderName = "input"
            outputImagesFolderName = "output"
        } else {
            Printer.standard("Input images directory name [inside $imagesFolderPath]:")
            inputImagesFolderName = Util.readInput()
            Printer.standard("Output images folder name [inside $imagesFolderPath]:")
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
                Printer.error("Can't create specified images root directory!")
                return false
            }
            try {
                if (!Files.exists(inputImagesPath)) {
                    Files.createDirectory(inputImagesPath)
                }
            } catch (e: IOException) {
                Files.deleteIfExists(inputImagesPath)
                Printer.error("Can't create specified input directory!")
                return false
            }
            try {
                if (!Files.exists(outputImagesPath)) {
                    Files.createDirectory(outputImagesPath)
                }
            } catch (e: IOException) {
                Files.deleteIfExists(outputImagesPath)
                Printer.error("Can't create specified output directory!")
                return false
            }
        } catch (e: Exception) {
            Printer.error("Incorrect input has been specified!")
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
            Printer.error("Can't save provided setup information!")
            Files.deleteIfExists(configFilePath)
            return false
        }
        return true
    }
}
