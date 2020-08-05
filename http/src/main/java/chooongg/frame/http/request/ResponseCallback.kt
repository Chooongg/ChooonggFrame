package chooongg.frame.http.request

import chooongg.frame.http.exception.HttpException

interface ResponseCallback<RESPONSE, DATA> {
    suspend fun onStart() = Unit
    suspend fun onResponse(response: RESPONSE) = Unit
    suspend fun onSuccess(data: DATA?)
    suspend fun configError(error: Throwable) = onError(HttpException(error))
    suspend fun onError(error: HttpException) = Unit
    suspend fun onEnd(isSuccess: Boolean) = Unit
}