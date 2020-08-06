package chooongg.frame.http.request

import chooongg.frame.http.exception.HttpException
import chooongg.frame.manager.LoggerManager
import chooongg.frame.utils.launchMain
import chooongg.frame.utils.withMain
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import retrofit2.Call

fun <RESPONSE> CoroutineScope.retrofitDefault(dsl: DefaultRetrofitCoroutineDsl<RESPONSE>.() -> Unit): Job {
    val retrofitCoroutineDsl = DefaultRetrofitCoroutineDsl<RESPONSE>()
    retrofitCoroutineDsl.dsl()
    return retrofitCoroutineDsl.request(this)
}

abstract class RetrofitCoroutineDsl<RESPONSE, DATA> {

    lateinit var api: Call<RESPONSE?>

    private var onStart: (() -> Unit)? = null

    protected var onResponse: ((RESPONSE) -> Unit)? = null

    protected var onSuccess: ((DATA?) -> Unit)? = null

    private var onFailed: ((HttpException) -> Unit)? = null

    private var onEnd: ((Boolean) -> Unit)? = null

    init {
        LoggerManager.changeFormatStrategy(
            LoggerManager.getDefaultPrettyFormatBuilder()
                .tag("ChooonggHttp")
                .methodCount(1)
                .methodOffset(3)
                .build()
        )
        Logger.d("RequestFrom")
        LoggerManager.changeDefault()
    }

    internal open fun clean() {
        onStart = null
        onResponse = null
        onSuccess = null
        onFailed = null
        onEnd = null
    }

    fun onStart(block: () -> Unit) {
        this.onStart = block
    }

    fun onResponse(block: (RESPONSE) -> Unit) {
        this.onResponse = block
    }

    fun onSuccess(block: (data: DATA?) -> Unit) {
        this.onSuccess = block
    }

    fun configFailed(error: Throwable) {
        onFailed?.invoke(HttpException(error))
    }

    fun onFailed(block: (error: HttpException) -> Unit) {
        this.onFailed = block
    }

    fun onEnd(block: (isSuccess: Boolean) -> Unit) {
        this.onEnd = block
    }

    fun request(coroutineScope: CoroutineScope): Job {
        return coroutineScope.launchMain {
            onStart?.invoke()
            val work = async(Dispatchers.IO) {
                try {
                    api.execute()
                } catch (e: Exception) {
                    withMain { configFailed(e) }
                    e.printStackTrace()
                    null
                }
            }
            work.invokeOnCompletion { _ ->
                if (work.isCancelled) {
                    api.cancel()
                    clean()
                }
            }
            val response = work.await()
            response?.let {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        withMain {
                            onResponse?.invoke(body)
                            onEnd?.invoke(true)
                        }
                    } else {
                        withMain {
                            configFailed(HttpException(HttpException.Type.EMPTY))
                            onEnd?.invoke(false)
                        }
                    }
                } else {
                    withMain {
                        configFailed(HttpException(response.code()))
                        onEnd?.invoke(false)
                    }
                }
            }
        }
    }
}

class DefaultRetrofitCoroutineDsl<RESPONSE> : RetrofitCoroutineDsl<RESPONSE, RESPONSE>() {
    init {
        onResponse = {
            onSuccess?.invoke(it)
        }
    }
}