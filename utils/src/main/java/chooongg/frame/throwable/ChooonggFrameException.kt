package chooongg.frame.throwable

/**
 * 框架异常
 */
class ChooonggFrameException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}