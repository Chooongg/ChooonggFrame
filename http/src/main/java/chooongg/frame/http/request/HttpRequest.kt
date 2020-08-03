package chooongg.frame.http.request

import androidx.lifecycle.LifecycleCoroutineScope
import chooongg.frame.http.exception.HttpException
import chooongg.frame.throwable.ChooonggFrameException
import chooongg.frame.utils.withIO
import chooongg.frame.utils.withMain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response

fun <RESPONSE> CoroutineScope.http(block: suspend HttpRequest<RESPONSE>.() -> Unit): Job {
    return launch { withIO { block.invoke(HttpRequest()) } }
}

fun <RESPONSE> LifecycleCoroutineScope.httpWhenCreated(block: suspend HttpRequest<RESPONSE>.() -> Unit): Job {
    return launchWhenCreated { withIO { block.invoke(HttpRequest()) } }
}

fun <RESPONSE> LifecycleCoroutineScope.httpWhenStarted(block: suspend HttpRequest<RESPONSE>.() -> Unit): Job {
    return launchWhenStarted { withIO { block.invoke(HttpRequest()) } }
}

fun <RESPONSE> LifecycleCoroutineScope.httpWhenResumed(block: suspend HttpRequest<RESPONSE>.() -> Unit): Job {
    return launchWhenResumed { withIO { block.invoke(HttpRequest()) } }
}

class HttpRequest<RESPONSE>() {

    private var api: (suspend () -> Response<RESPONSE>)? = null

    fun api(block: suspend () -> Response<RESPONSE>) {
        api = block
    }

    suspend fun request(callback: DefaultResponseCallback<RESPONSE>) {
        if (api == null) throw ChooonggFrameException("Request operation not implemented api method!")
        withMain { callback.onStart() }
        try {
            val response = api!!.invoke()
            if (response.isSuccessful) {
                withMain {
                    try {
                        callback.onResponse(response.body())
                    } catch (e: Throwable) {
                        callback.configError(HttpException(e))
                    }
                }
            } else {
                withMain { callback.configError(HttpException(response.code())) }
            }
        } catch (e: Throwable) {
            callback.configError(HttpException(e))
        }
        withMain { callback.onEnd(true) }
    }
}
