package chooongg.frame.http.request

import chooongg.frame.http.exception.HttpException

open class HttpCallback<RESPONSE> {

    private constructor()

    constructor(block: (HttpCallback<RESPONSE>) -> Unit) {
        block(HttpCallback())
    }

    /**
     * 重写请从这里配置错误信息并进行分发
     */
    internal fun configError(e: Throwable) {
        onError(HttpException(e))
    }

    private var startBlock: (() -> Unit)? = null
    private var responseBlock: (RESPONSE?.() -> Unit)? = null
    private var errorBlock: ((HttpException) -> Unit)? = null
    private var endBlock: (() -> Unit)? = null

    fun start(block: () -> Unit) {
        startBlock = block
    }

    fun response(block: RESPONSE?.() -> Unit) {
        responseBlock = block
    }

    fun error(block: (HttpException) -> Unit) {
        errorBlock = block
    }

    fun end(block: () -> Unit) {
        endBlock = block
    }

    internal fun onStart() {
        startBlock?.invoke()
    }

    internal fun onResponse(response: RESPONSE?) {
        responseBlock?.invoke(response)
    }

    internal fun onError(error: HttpException) {
        errorBlock?.invoke(error)
    }

    internal fun onEnd() {
        endBlock?.invoke()
    }
}