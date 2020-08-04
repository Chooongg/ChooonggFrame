package chooongg.frame.http.request

import chooongg.frame.http.exception.HttpException
import chooongg.frame.utils.withIO
import chooongg.frame.utils.withMain
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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