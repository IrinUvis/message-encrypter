@file:OptIn(ExperimentalUnsignedTypes::class)

package cryptography

import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

class MainHelper {
    var configurationIsFine = false

    fun decodeMessageFromImage() {
        println("Name of image with encrypted message [in ${config.imagesAbsolutePath}\\${config.outputImagesFolderName}]: ")
        val inputImgName: String
        try {
            inputImgName = Util.readFilenameWithExtension()
        } catch (e: IncorrectExtensionException) {
            println(e.message)
            return
        }
        println("Password:")
        val password = Util.readInput()
        try {
            val inputImg: BufferedImage = ImageIO.read(
                File("${config.imagesAbsolutePath}\\${config.outputImagesFolderName}\\$inputImgName")
            )
            val hiddenMsg : UByteArray = cryptographer.decodeFromBlueColor(inputImg, password)
            println("Message:")
            println(hiddenMsg.toByteArray().toString(Charsets.UTF_8))
        } catch (ex : IOException) {
            println("ERROR: Can't read input file!")
            return
        }
    }

    fun encodeMessageInImage() {
        println("Name of original image from [in ${config.imagesAbsolutePath}\\${config.inputImagesFolderName}]: ")
        val inputImgName: String
        try {
            inputImgName = Util.readFilenameWithExtension()
        } catch (e: IncorrectExtensionException) {
            println(e.message)
            return
        }
        println("Name that should be given to image with encoded message [in ${config.imagesAbsolutePath}\\${config.outputImagesFolderName}]: ")
        val outputImgName: String
        try {
            outputImgName = Util.readFilenameWithExtension()
        } catch (e: IncorrectExtensionException) {
            println(e.message)
            return
        }
        println("Message to hide:")
        val msgToHide = Util.readInput().encodeToByteArray().toUByteArray()
        println("Password:")
        val password = Util.readInput()
        try {
            val inputImg: BufferedImage = ImageIO.read(
                File("${config.imagesAbsolutePath}/${config.inputImagesFolderName}/$inputImgName"))
            if ((msgToHide.size + 3) * 8 > inputImg.width * inputImg.height) {
                println("The input image is not large enough to hold this message.")
            } else {
                val outputImage = cryptographer.encodeInBlueColor(inputImg, msgToHide, password)
                ImageIO.write(
                    outputImage,
                    "png",
                    File("${config.imagesAbsolutePath}/${config.outputImagesFolderName}/$outputImgName"))
                println("Message saved in $outputImgName image.")
            }
        } catch (ex : IOException) {
            println("ERROR: Can't read input file!")
        }
    }

    fun performSetup(recommended: Boolean) {
        if (config.setup(recommended)) {
            configurationIsFine = config.readConfig()
            config.printConfigInfo()
        } else {
            return
        }
    }

    fun displayHelp() {
        println("HELP")
    }

    fun printConfigInfo() {
        config.printConfigInfo()
    }
}