package chooongg.frame.http.request

import chooongg.frame.http.exception.HttpException

interface ResponseCallback<RESPONSE, DATA> {
    fun onStart()
    fun onResponse(response: RESPONSE?)
    fun onSuccess(data: DATA?)
    fun configError(error: Throwable)
    fun onError(error: HttpException)
    fun onEnd(isSuccess:Boolean)
}