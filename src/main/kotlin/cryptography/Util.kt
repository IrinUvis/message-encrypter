package cryptography

class Util {
    companion object {
        fun readInput(): String {
            print("> ")
            return readLine()!!.trim()
        }

        fun readFilenameWithExtension(): String {
            val input = readInput()
            if (input.endsWith(".png")) {
                return input
            }
            throw IncorrectExtensionException("ERROR: The image has to have .png extension!")
        }
    }
}