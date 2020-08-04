package chooongg.frame.http

import android.app.Application
import android.util.Log
import chooongg.frame.ChooonggFrame
import chooongg.frame.throwable.ChooonggFrameException
import chooongg.frame.utils.debug
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.net.URL

object ChooonggHttp {

    private var isInitialized = false

    @JvmStatic
    @Suppress("unused", "UNUSED_PARAMETER")
    fun initialize(application: Application) {
        if (!isInitialized) {
            isInitialized = true
            Log.d(ChooonggFrame.TAG, "ChooonggHttp Initialize Finish!")
        } else throw ChooonggFrameException("ChooonggHttp don't repeat initialize")
    }

    fun <T> getAPI(
        clazz: Class<T>,
        baseUrl: URL,
        config: (OkHttpClient.Builder.() -> Unit)? = null
    ): T {
        return getRetrofit(baseUrl, config).create(clazz)
    }

    fun getRetrofit(baseUrl: URL, config: (OkHttpClient.Builder.() -> Unit)? = null) =
        Retrofit.Builder().baseUrl(baseUrl)
            .client(getOkHttpClient(config))
            .addConverterFactory(GsonConverterFactory.create(GsonManager.gson))
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

    /**
     * 自定义HttpClient
     */
    fun getOkHttpClient(config: (OkHttpClient.Builder.() -> Unit)?): OkHttpClient {
        val clientBuilder = OkHttpClient().newBuilder()
        debug {
            clientBuilder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }
        config?.invoke(clientBuilder)
        return clientBuilder.build()
    }
}