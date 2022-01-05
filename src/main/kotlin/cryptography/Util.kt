package cryptography

import java.io.File


class Util {
    companion object {
        fun readInput(): String {
            print("> ")
            return readLine()!!.trim()
        }

        fun readFilenameWithExtension(isInput: Boolean): String {
            val input = readInput()
            return if (isInput) {
                when {
                    input.endsWith(".png") -> input
                    File("${config.getImageDirPath(isInput)}\\$input.png").exists() -> "$input.png"
                    else -> throw IncorrectFileExtensionException("The image has to have .png extension!")
                }
            } else {
                when {
                    input.endsWith(".png") -> input
                    else -> "$input.png"
                }
            }
        }
    }
}