@file:OptIn(ExperimentalUnsignedTypes::class)

package cryptography

import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

class MainHelper {
    fun decodeMessageFromImage() {
        Printer.standard("Name of image with encrypted message [in ${config.imagesAbsolutePath}\\${config.outputImagesFolderName}]: ")
        val inputImgName: String
        try {
            inputImgName = Util.readFilenameWithExtension(false)
        } catch (e: IncorrectFileExtensionException) {
            Printer.error(e.message)
            return
        }
        Printer.standard("Password:")
        val password = Util.readInput()
        try {
            val inputImg: BufferedImage = ImageIO.read(
                File("${config.imagesAbsolutePath}\\${config.outputImagesFolderName}\\$inputImgName")
            )
            val hiddenMsg : UByteArray = cryptographer.decodeFromBlueColor(inputImg, password)
            Printer.standard("Message: ${hiddenMsg.toByteArray().toString(Charsets.UTF_8)}")
        } catch (ex : IOException) {
            Printer.error("Can't read input file!")
            return
        }
    }

    fun encodeMessageInImage() {
        Printer.standard("Name of original image from [in ${config.imagesAbsolutePath}\\${config.inputImagesFolderName}]: ")
        val inputImgName: String
        try {
            inputImgName = Util.readFilenameWithExtension(true)
        } catch (e: IncorrectFileExtensionException) {
            Printer.error(e.message)
            return
        }
        Printer.standard("Name that should be given to image with encoded message [in ${config.imagesAbsolutePath}\\${config.outputImagesFolderName}]: ")
        val outputImgName: String
        try {
            outputImgName = Util.readFilenameWithExtension(false)
        } catch (e: IncorrectFileExtensionException) {
            Printer.error(e.message)
            return
        }
        Printer.standard("Message to hide:")
        val msgToHide = Util.readInput().encodeToByteArray().toUByteArray()
        Printer.standard("Password:")
        val password = Util.readInput()
        try {
            val inputImg: BufferedImage = ImageIO.read(
                File("${config.imagesAbsolutePath}/${config.inputImagesFolderName}/$inputImgName"))
            if ((msgToHide.size + 3) * 8 > inputImg.width * inputImg.height) {
                Printer.error("The input image is not large enough to hold this message.")
            } else {
                val outputImage = cryptographer.encodeInBlueColor(inputImg, msgToHide, password)
                ImageIO.write(
                    outputImage,
                    "png",
                    File("${config.imagesAbsolutePath}/${config.outputImagesFolderName}/$outputImgName"))
                Printer.info("Message saved in $outputImgName image.")
            }
        } catch (ex : IOException) {
            Printer.error("Can't read input file!")
        }
    }

    fun performSetup(recommended: Boolean) {
        if (config.setup(recommended)) {
            config.loadConfig()
            config.printConfigInfo()
        } else {
            return
        }
    }

    fun printConfigInfo() {
        config.printConfigInfo()
    }

    fun printImagesList(isInput: Boolean) {
        config.printImagesList(isInput)
    }
}