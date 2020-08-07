package chooongg.frame.http.interceptor

import chooongg.frame.manager.LoggerManager
import com.orhanobut.logger.LogcatLogStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import okhttp3.*
import okhttp3.internal.http.promisesBody
import okio.Buffer
import okio.GzipSource
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.EOFException
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
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
                val params = HashMap<String, String>()
                when (requestBody) {
                    is FormBody -> {
                        for (i in 0 until requestBody.size) {
                            params[requestBody.encodedName(i)] = requestBody.encodedValue(i)
                        }
                    }
                    is MultipartBody -> {
                        append("is MultipartBody")
                    }
                    else -> append("I won't support it")
                }
                var isFirstLine = true
                params.forEach {
                    if (!isFirstLine) append("           ")
                    append(it.key).append(" = ").append(it.value).append(BR)
                    if (isFirstLine) isFirstLine = false
                }
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
            append("Body       ")
            append(getResponseBody(response))
        })
        return response
    }

    private fun bodyToString(request: RequestBody?): String {
        try {
            val buffer = Buffer()
            if (request != null) request.writeTo(buffer) else return ""
            return buffer.readUtf8()
        } catch (e: IOException) {
            return "did not work"
        }
    }

    private fun getResponseBody(response: Response): String {
        val responseBody = response.body!!
        val headers = response.headers
        val contentLength = responseBody.contentLength()
        if (!response.promisesBody()) {
            return "End request - Promises Body"
        } else if (bodyHasUnknownEncoding(response.headers)) {
            return "encoded body omitted"
        } else {
            val source = responseBody.source()
            source.request(Long.MAX_VALUE) // Buffer the entire body.
            var buffer = source.buffer

            var gzippedLength: Long? = null
            if ("gzip".equals(headers["Content-Encoding"], ignoreCase = true)) {
                gzippedLength = buffer.size
                GzipSource(buffer.clone()).use { gzippedResponseBody ->
                    buffer = Buffer()
                    buffer.writeAll(gzippedResponseBody)
                }
            }

            val contentType = responseBody.contentType()
            val charset: Charset = contentType?.charset(StandardCharsets.UTF_8)
                ?: StandardCharsets.UTF_8

            if (!buffer.isProbablyUtf8()) {
                return "End request - binary ${buffer.size}:byte body omitted"
            }

            if (contentLength != 0L) {
                return getJsonString(buffer.clone().readString(charset))
            }

            return if (gzippedLength != null) {
                "End request - ${buffer.size}:byte, $gzippedLength-gzipped-byte body"
            } else {
                "End request - ${buffer.size}:byte body"
            }
        }
    }

    private fun getJsonString(msg: String): String {
        val message: String
        message = try {
            when {
                msg.startsWith("{") -> {
                    val jsonObject = JSONObject(msg)
                    msg + BR + jsonObject.toString(JSON_INDENT)
                }
                msg.startsWith("[") -> {
                    val jsonArray = JSONArray(msg)
                    msg + BR + jsonArray.toString(JSON_INDENT)
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

    private fun bodyHasUnknownEncoding(headers: Headers): Boolean {
        val contentEncoding = headers["Content-Encoding"] ?: return false
        return !contentEncoding.equals("identity", ignoreCase = true) &&
                !contentEncoding.equals("gzip", ignoreCase = true)
    }

    private fun Buffer.isProbablyUtf8(): Boolean {
        try {
            val prefix = Buffer()
            val byteCount = size.coerceAtMost(64)
            copyTo(prefix, 0, byteCount)
            for (i in 0 until 16) {
                if (prefix.exhausted()) {
                    break
                }
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
            }
            return true
        } catch (_: EOFException) {
            return false // Truncated UTF-8 sequence.
        }
    }
}