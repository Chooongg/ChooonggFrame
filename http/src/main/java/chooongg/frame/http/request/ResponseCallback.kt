package chooongg.frame.http.request

import chooongg.frame.http.exception.HttpException

interface ResponseCallback<RESPONSE, DATA> {
    suspend fun onStart() = Unit
    suspend fun onResponse(response: RESPONSE?) = Unit
    fun onSuccess(data: DATA?)
    fun configError(error: Throwable) = onError(HttpException(error))
    fun onError(error: HttpException) = Unit
    fun onEnd(isSuccess: Boolean) = Unit
}