package chooongg.frame.http

import java.net.URL

interface TestAPI {
    companion object {
        fun service() = ChooonggHttp.getAPI(TestAPI::class.java, URL(""))
    }

    suspend fun test(): String
}