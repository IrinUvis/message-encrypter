package cryptography

val cryptographer = Cryptographer()
val config = Config()
val mainHelper = MainHelper()

fun main() {
    mainHelper.configurationIsFine = config.readConfig()
    if (mainHelper.configurationIsFine) {
        config.printConfigInfo()
    }
    while(true) {
        if (mainHelper.configurationIsFine) {
            println("Specify action (hide, show, setup, setup recommended, config info, help, exit):")
            when (val cmd = Util.readInput()) {
                "hide" -> mainHelper.encodeMessageInImage()
                "show" -> mainHelper.decodeMessageFromImage()
                "setup" -> mainHelper.performSetup(false)
                "setup recommended" -> mainHelper.performSetup(true)
                "config info" -> mainHelper.printConfigInfo()
                "help" -> mainHelper.displayHelp()
                "exit" -> break
                else -> println("Wrong task: $cmd")
            }
        } else {
            println("Specify action (setup, setup recommended, help, exit):")
            when (val cmd = Util.readInput()) {
                "setup" -> mainHelper.performSetup(false)
                "setup recommended" -> mainHelper.performSetup(true)
                "help" -> mainHelper.displayHelp()
                "exit" -> break
                else -> println("Wrong task: $cmd")
            }
        }
    }
    println("Bye!")
}





