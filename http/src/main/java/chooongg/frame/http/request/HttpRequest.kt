package chooongg.frame.http.request

import androidx.lifecycle.LifecycleCoroutineScope
import chooongg.frame.http.exception.HttpException
import chooongg.frame.throwable.ChooonggFrameException
import chooongg.frame.utils.withIO
import chooongg.frame.utils.withMain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
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

class HttpRequest<RESPONSE> internal constructor() {

    private var api: (suspend () -> RESPONSE?)? = null

    fun api(block: suspend () -> RESPONSE?) {
        api = block
    }

    suspend fun <DATA> request(callback: ResponseCallback<RESPONSE, DATA>) {
        if (api == null) throw ChooonggFrameException("Request operation not implemented api method!")
        withMain { callback.onStart() }
        try {
            withMain { callback.onResponse(api!!.invoke()) }
        } catch (e: Throwable) {
            withMain { callback.configError(HttpException(e)) }
        }
        withMain { callback.onEnd(true) }
    }
}

suspend fun <RESPONSE, DATA> Call<RESPONSE>.request(callback: ResponseCallback<RESPONSE, DATA>) {
    withMain { callback.onStart() }
    withIO {
        enqueue(object : Callback<RESPONSE> {
            override fun onResponse(call: Call<RESPONSE>, response: Response<RESPONSE>) {
                suspend {
                    if (response.isSuccessful) {
                        withMain { callback.onResponse(response.body()) }
                    } else {
                        withMain { callback.configError(HttpException(response.code())) }
                    }
                }
            }

            override fun onFailure(call: Call<RESPONSE>, t: Throwable) {
                suspend {
                    withMain { callback.configError(HttpException(t)) }
                }
            }
        })
    }
}