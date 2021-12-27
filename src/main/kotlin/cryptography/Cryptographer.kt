@file:OptIn(ExperimentalUnsignedTypes::class)

package cryptography

import java.awt.Color
import java.awt.image.BufferedImage

class Cryptographer {
    private val threeEndingBytes = ubyteArrayOf(0u, 0u, 3u)

    fun encodeInBlueColor(inputImg: BufferedImage, msgToHide: UByteArray, password: String): BufferedImage {
        val outputImage = BufferedImage(inputImg.width, inputImg.height, BufferedImage.TYPE_INT_RGB)
        for (i in 0 until inputImg.height) {
            for (j in 0 until inputImg.width) {
                outputImage.setRGB(j, i, inputImg.getRGB(j, i))
            }
        }
        var encodedPassword = password.encodeToByteArray().toUByteArray()
        while (encodedPassword.size < msgToHide.size) {
            encodedPassword += encodedPassword
        }
        val encryptedMessageBytes = mutableListOf<UByte>()
        for (msgByteIndex in msgToHide.indices) {
            encryptedMessageBytes.add(msgToHide[msgByteIndex] xor encodedPassword[msgByteIndex])
        }
        encryptedMessageBytes.addAll(threeEndingBytes)
        var msgIndex = 0
        var bitIndex = 0
        loop@
        for (i in 0 until inputImg.height) {
            for (j in 0 until inputImg.width) {
                if (msgIndex >= encryptedMessageBytes.size) {
                    break@loop
                }
                else {
                    val pixelColor = Color(inputImg.getRGB(j, i))
                    val theBit = encryptedMessageBytes[msgIndex].toBinaryString()[bitIndex].digitToInt()
                    val newBlue = pixelColor.blue / 2 * 2 + theBit
                    outputImage.setRGB(j, i, Color(pixelColor.red, pixelColor.green, newBlue).rgb)
                    bitIndex++
                    if (bitIndex == 8) {
                        bitIndex = 0
                        msgIndex++
                    }
                }
            }
        }
        return outputImage
    }

    fun decodeFromBlueColor(inputImg: BufferedImage, password: String): UByteArray {
        val messageBytes = mutableListOf<UByte>()
        var bitString = ""
        for (i in 0 until inputImg.height) {
            for (j in 0 until inputImg.width) {
                val pixelColor = Color(inputImg.getRGB(j, i))
                bitString += pixelColor.blue % 2
                if (bitString.length == 8) {
                    messageBytes += bitString.toUByte(radix = 2)
                    bitString = ""
                }
            }
        }
        var encodedPassword = password.encodeToByteArray().toUByteArray()
        val messageImportantBytes = findTillEndPattern(messageBytes.toUByteArray())
        while (encodedPassword.size < messageImportantBytes.size) {
            encodedPassword += encodedPassword
        }
        val decryptedMessageBytes = mutableListOf<UByte>()
        for (msgByteIndex in messageImportantBytes.indices) {
            decryptedMessageBytes.add(messageImportantBytes[msgByteIndex] xor encodedPassword[msgByteIndex])
        }
        return decryptedMessageBytes.toUByteArray()
    }

    private fun findTillEndPattern(uByteArray: UByteArray): UByteArray {
        if (uByteArray.size < 3) return ubyteArrayOf()
        else {
            for (i in 0..uByteArray.lastIndex - 2) {
                if (threeEndingBytes.contentEquals(ubyteArrayOf(uByteArray[i], uByteArray[i + 1], uByteArray[i + 2])))
                    return uByteArray.sliceArray(0 until i)
            }
        }
        return ubyteArrayOf()
    }
}