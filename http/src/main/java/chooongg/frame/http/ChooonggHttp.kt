package chooongg.frame.http

import android.app.Application
import android.util.Log
import chooongg.frame.ChooonggFrame
import chooongg.frame.entity.MemoryConstants
import chooongg.frame.http.manager.GsonManager
import chooongg.frame.manager.APP
import chooongg.frame.throwable.ChooonggFrameException
import chooongg.frame.utils.NetworkUtils
import chooongg.frame.utils.debug
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.net.URL
import java.util.concurrent.TimeUnit

object ChooonggHttp {

    const val DEFAULT_TIMEOUT = 30L//默认超时时间 分钟为单位
    const val DEFAULT_HTTP_CACHE_SIZE = 10L * MemoryConstants.MB//默认缓存大小

    var maxStale = 60 * 60 * 4 * 28
    var cacheSize = DEFAULT_HTTP_CACHE_SIZE
    var timeout = DEFAULT_TIMEOUT

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
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .cache(Cache(APP.cacheDir, cacheSize))
            .addInterceptor {
                var request = it.request()
                if (!NetworkUtils.isNetworkConnected()) {
                    val tempCacheControl = CacheControl.Builder()
                        .maxStale(maxStale, TimeUnit.SECONDS)
                        .onlyIfCached()
                        .build()
                    request = request.newBuilder()
                        .cacheControl(tempCacheControl)
                        .build()
                }
                it.proceed(request)
            }

        config?.invoke(clientBuilder)
        clientBuilder.addInterceptor(chooongg.frame.http.interceptor.LoggingInterceptor())
//        debug {
//            clientBuilder.addInterceptor(
//                LoggingInterceptor.Builder()
//                    .setLevel(Level.BASIC)
//                    .log(Log.DEBUG)
//                    .tag("ChooonggHttp")
//                    .build()
//            )
//        }
        return clientBuilder.build()
    }
}