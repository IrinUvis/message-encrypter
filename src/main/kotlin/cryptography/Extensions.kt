package cryptography

fun ordinalIndexOf(str: String, toBeFound: String?, occurrence: Int): Int {
    var n = occurrence
    var pos = str.indexOf(toBeFound!!)
    while (--n > 0 && pos != -1) pos = str.indexOf(toBeFound, pos + 1)
    return pos
}

fun UByte.toBinaryString(): String {
    return String.format("%8s", this.toString(2)).replace(" ".toRegex(), "0")
}