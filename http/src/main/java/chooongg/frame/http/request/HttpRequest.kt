package chooongg.frame.http.request

import chooongg.frame.http.exception.HttpException
import chooongg.frame.throwable.ChooonggFrameException
import chooongg.frame.utils.withMain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response

fun <RESPONSE> CoroutineScope.http(block: suspend HttpRequest<RESPONSE>.() -> Unit): Job {
    return launch(Dispatchers.IO) {
        block.invoke(HttpRequest())
    }
}

class HttpRequest<RESPONSE>() {

    private var api: (() -> Response<RESPONSE>)? = null

    suspend fun api(block: () -> Response<RESPONSE>) {
        api = block
    }

    suspend fun request(block: HttpCallback<RESPONSE>.() -> Unit) {
        if (api == null) throw ChooonggFrameException("Request operation not implemented api method!")
        val callback = HttpCallback<RESPONSE>()
        block.invoke(callback)
        withMain { callback.onStart() }
        try {
            val response = api!!.invoke()
            if (response.isSuccessful) {
                withMain {
                    try {
                        callback.onResponse(response.body())
                    } catch (e: Throwable) {
                        callback.onError(HttpException(e))
                    }
                }
            } else {
                withMain { callback.onError(HttpException(response.code())) }
            }
        } catch (e: Throwable) {
            callback.onError(HttpException(e))
        }
        withMain { callback.onEnd() }
    }
}
