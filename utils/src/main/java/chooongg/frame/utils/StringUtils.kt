package chooongg.frame.utils

fun String?.isSpace(): Boolean {
    if (this.isNullOrEmpty()) return true
    this.forEach {
        if (!Character.isWhitespace(it)) return false
    }
    return true
}

fun String?.isNotSpace() = !isSpace()