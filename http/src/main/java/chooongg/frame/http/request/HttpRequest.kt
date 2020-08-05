package chooongg.frame.http.request

import chooongg.frame.http.exception.HttpException
import chooongg.frame.manager.LoggerManager
import chooongg.frame.utils.launchIO
import chooongg.frame.utils.withMain
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

fun CoroutineScope.http(
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job = launchIO(start, block)

class TestCallback<RESPONSE, DATA> {
    constructor()
    constructor(co: CoroutineScope, block: suspend (Continuation<RESPONSE>) -> Unit) {
        suspendCoroutine<RESPONSE> {
            block.invoke(it)
            Response<DATA>
        }
    }
}

suspend fun <RESPONSE, DATA> Call<RESPONSE?>.request(callback: ResponseCallback<RESPONSE, DATA>) {

    LoggerManager.changeFormatStrategy(
        LoggerManager.getDefaultPrettyFormatBuilder()
            .tag("ChooonggHttp")
            .methodCount(1)
            .methodOffset(1)
            .build()
    )
    Logger.d("RequestFrom")
    LoggerManager.changeDefault()
    withMain { callback.onStart() }
    try {
        val response = suspendCoroutine<RESPONSE> {
            enqueue(object : Callback<RESPONSE?> {
                override fun onResponse(call: Call<RESPONSE?>, response: Response<RESPONSE?>) {
                    try {
                        if (response.isSuccessful) {
                            val body = response.body()
                            if (body == null) {
                                it.resumeWithException(HttpException(HttpException.Type.EMPTY))
                            } else {
                                it.resume(body)
                            }
                        } else {
                            it.resumeWithException(HttpException(response.code()))
                        }
                    } catch (e: Exception) {
                        it.resumeWithException(e)
                    }
                }

                override fun onFailure(call: Call<RESPONSE?>, t: Throwable) {
                    it.resumeWithException(HttpException(t))
                }
            })
        }
        withMain {
            callback.onResponse(response)
            callback.onEnd(true)
        }
    } catch (e: HttpException) {
        withMain {
            callback.configError(e)
            callback.onEnd(false)
        }
    }
}