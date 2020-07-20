package chooongg.frame.throwable

/**
 * 框架异常
 */
class ChooonggFrameException(message: String) : RuntimeException(message) {

    override val message: String?
        get() = "——————————框架异常——————————${System.lineSeparator()}${super.message}"
}