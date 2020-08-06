package chooongg.frame.http.interceptor

import chooongg.frame.manager.LoggerManager
import com.orhanobut.logger.LogcatLogStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.TimeUnit

private val httpPrettyFormatStrategy by lazy {
    PrettyFormatStrategy.newBuilder()
        .tag("HTTP")
        .showThreadInfo(false)
        .methodCount(0)
        .logStrategy(LogcatLogStrategy())
        .build()
}

class LoggingInterceptor : Interceptor {

    private val BR = System.getProperty("line.separator") ?: "\n"

    private val JSON_INDENT = 2

    private val OOM_OMITTED = BR + "Output omitted because of Object size."

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (LoggerManager.isEnable().not()) return chain.proceed(request)

        // Request
        httpPrettyFormatStrategy.log(Logger.DEBUG, "Request", buildString {
            val methodLength = request.method.length
            append(request.method).append(' ')
            for (i in 0 until 10 - methodLength) append(' ')
            append(request.url.toString()).append(BR).append(BR)
            // Headers
            if (request.headers.size > 0) {
                append("Headers    ")
                request.headers.forEachIndexed { index, pair ->
                    if (index != 0) append("           ")
                    append(pair.first).append(" = ").append(pair.second).append(BR)
                }
                append(BR)
            }
            // Body
            val requestBody = request.body
            if (requestBody != null) {
                append("Body       ")
                append(BR)
            }
        })


        val startNs = System.nanoTime()
        // Response
        val response = chain.proceed(request)
        httpPrettyFormatStrategy.log(Logger.DEBUG, "Response", buildString {
            append("Response   ").append(request.url.toString()).append(BR).append(BR)
            append("IsSuccess  ").append(response.isSuccessful)
            val receivedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
            append("  - Received in: ").append(receivedMs).append("ms").append(BR).append(BR)
            // Headers
            if (response.headers.size > 0) {
                append("Headers    ")
                response.headers.forEachIndexed { index, pair ->
                    if (index != 0) append("           ")
                    append(pair.first).append(" = ").append(pair.second).append(BR)
                }
                append(BR)
            }
            // Body
            val requestBody = request.body
            if (requestBody != null) {
                append("Body       ")
                append(BR)
            }
        })
        return response
    }

    private fun getJsonString(msg: String): String {
        val message: String
        message = try {
            when {
                msg.startsWith("{") -> {
                    val jsonObject = JSONObject(msg)
                    jsonObject.toString(JSON_INDENT)
                }
                msg.startsWith("[") -> {
                    val jsonArray = JSONArray(msg)
                    jsonArray.toString(JSON_INDENT)
                }
                else -> {
                    msg
                }
            }
        } catch (e: JSONException) {
            msg
        } catch (e1: OutOfMemoryError) {
            OOM_OMITTED
        }
        return message
    }
}