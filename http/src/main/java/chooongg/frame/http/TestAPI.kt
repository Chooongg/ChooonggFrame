package chooongg.frame.http

import retrofit2.Call
import java.net.URL

interface TestAPI {
    companion object {
        fun service() = ChooonggHttp.getAPI(TestAPI::class.java, URL(""))
    }

    suspend fun test(): String

    suspend fun test2(): Call<String>
}