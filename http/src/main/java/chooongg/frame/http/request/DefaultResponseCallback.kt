package chooongg.frame.http.request

import chooongg.frame.http.exception.HttpException

open class DefaultResponseCallback<RESPONSE> : ResponseCallback<RESPONSE, RESPONSE> {

    private constructor()

    constructor(block: DefaultResponseCallback<RESPONSE>.() -> Unit) {
        block(DefaultResponseCallback())
    }

    private var startBlock: (() -> Unit)? = null
    private var responseBlock: (RESPONSE?.() -> Unit)? = null
    private var successBlock: (RESPONSE?.() -> Unit)? = null
    private var errorBlock: ((HttpException) -> Unit)? = null
    private var endBlock: ((Boolean) -> Unit)? = null

    fun start(block: () -> Unit) {
        startBlock = block
    }

    fun response(block: RESPONSE?.() -> Unit) {
        responseBlock = block
    }

    fun success(block: RESPONSE?.() -> Unit) {
        successBlock = block
    }

    fun error(block: (HttpException) -> Unit) {
        errorBlock = block
    }

    fun end(block: (Boolean) -> Unit) {
        endBlock = block
    }

    override fun onStart() {
        startBlock?.invoke()
    }

    override fun onResponse(response: RESPONSE?) {
        responseBlock?.invoke(response)
    }

    override fun onSuccess(data: RESPONSE?) {
        successBlock?.invoke(data)
    }

    /**
     * 重写请从这里配置错误信息并进行分发
     */
    override fun configError(error: Throwable) {
        onError(HttpException(error))
    }

    override fun onError(error: HttpException) {
        errorBlock?.invoke(error)
    }

    override fun onEnd(isSuccess: Boolean) {
        endBlock?.invoke(isSuccess)
    }
}