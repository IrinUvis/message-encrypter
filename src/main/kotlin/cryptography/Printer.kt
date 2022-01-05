package cryptography

class Printer {
    companion object {
        fun info(message: Any?) {
            println("INFO: $message")
        }

        fun error(message: Any?) {
            println("ERROR: $message")
        }

        fun standard(message: Any?) {
            println(message)
        }
    }
}