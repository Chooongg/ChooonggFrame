package chooongg.frame.http

import retrofit2.Response
import java.net.URL

interface TestAPI {
    companion object {
        fun service() = ChooonggHttp.getAPI(TestAPI::class.java, URL(""))
    }

    fun test(): Response<String>
}