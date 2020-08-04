package chooongg.frame.simple.api

import chooongg.frame.http.ChooonggHttp
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.net.URL

interface TestAPI {
    companion object {
        val service by lazy {
            ChooonggHttp.getAPI(TestAPI::class.java, URL("https://api.yunxiaoqu.net/"))
        }
    }

    @POST("sendSms")
    @FormUrlEncoded
    fun sendSms(
        @Field("phone") phone: CharSequence,
        @Field("type") type: Int,
        @Field("app_version_type") versionType: CharSequence = "queqiao"
    ): Call<APIResponse<Any>?>
}