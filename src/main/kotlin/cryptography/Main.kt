package cryptography

val cryptographer = Cryptographer()
val config = Config()
val mainHelper = MainHelper()

fun main() {
    config.loadConfig()
    if (config.configurationIsFine) {
        config.printConfigInfo()
    }
    while(true) {
        if (config.configurationIsFine) {
            Printer.standard("Specify action (hide, show, setup [--rec], config info [--in | --out], exit):")
            when (val cmd = Util.readInput()) {
                "hide" -> mainHelper.encodeMessageInImage()
                "show" -> mainHelper.decodeMessageFromImage()
                "setup" -> mainHelper.performSetup(false)
                "setup --rec" -> mainHelper.performSetup(true)
                "config info" -> mainHelper.printConfigInfo()
                "config info --in" -> mainHelper.printImagesList(true)
                "config info --out" -> mainHelper.printImagesList(false)
                "exit" -> break
                else -> Printer.error("Wrong task: $cmd")
            }
        } else {
            Printer.standard("Specify action (setup, setup recommended, exit):")
            when (val cmd = Util.readInput()) {
                "setup" -> mainHelper.performSetup(false)
                "setup recommended" -> mainHelper.performSetup(true)
                "exit" -> break
                else -> Printer.error("Wrong task: $cmd")
            }
        }
    }
    Printer.standard("Bye!")
}





