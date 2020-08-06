package chooongg.frame.simple.api

import android.util.Base64
import okhttp3.*
import okio.Buffer
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.HashMap

class CommonParamsInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val oldRequest = chain.request()
        val body = oldRequest.body
        val params = HashMap<String, String?>()
        val apiTokenSB = StringBuilder()
        when (body) {
            is FormBody -> {
                for (i in 0 until body.size) params[body.name(i)] = body.value(i)
            }
            is MultipartBody -> {
                body.parts.forEach {
                    if (it.body.contentType()?.type?.contains("text") == true) {
                        val headers = it.headers
                        for (i in headers!!.names().indices) {
                            val value = headers.value(i)//valueform-data; name="article_type"
                            val replaceValue = "form-data; name="//这段在MultipartBody.Part源码中看到
                            if (value.contains(replaceValue)) {
                                val key =
                                    value.replace(replaceValue, "").replace("\"".toRegex(), "")
                                params[key] = bodyToString(it.body)
                                break
                            }
                        }
                    }
                }
            }
            else -> {
                val url = oldRequest.url
                val iterator = url.queryParameterNames.iterator()
                var i = 0
                while (iterator.hasNext()) {
                    params[iterator.next()] = url.queryParameterValue(i) ?: ""
                    i++
                }
            }
        }

        apiTokenSB.append("venderId").append(3)
            .append("versionCode").append("1.0.0")
            .append("uToken").append("")

        params.forEach { apiTokenSB.append(it.key).append(it.value) }
        val chars = apiTokenSB.toString().toCharArray()
        Arrays.sort(chars)
        val split = chars.contentToString()
            .replace("\\", "")
            .replace("[", "")
            .replace("]", "")
            .replace(", ", "")
            .split(Pattern.compile("/(?<!^)(?!$)/u"))
        apiTokenSB.clear()
        split.forEach { apiTokenSB.append(it) }

        val request = oldRequest.newBuilder().apply {
            addHeader("uToken", "")
            addHeader("versionCode", "1.0.0")
            addHeader("venderId", "3")
            addHeader("apiToken", md5(apiTokenSB.toString()))
        }.build()
        return chain.proceed(request)
    }

    // 加密
    private fun getBase64(str: String?): String {
        var result = ""
        if (str != null) {
            try {
                result = String(
                    Base64.encode(str.toByteArray(charset("utf-8")), Base64.NO_WRAP),
                    charset("utf-8")
                )
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
        }
        return result
    }

    private fun md5(text: String): String {
        try {
            //获取md5加密对象
            val instance: MessageDigest = MessageDigest.getInstance("MD5")
            //对字符串加密，返回字节数组
            val digest: ByteArray = instance.digest(text.toByteArray())
            val sb = StringBuffer()
            for (b in digest) {
                //获取低八位有效值
                val i: Int = b.toInt() and 0xff
                //将整数转化为16进制
                var hexString = Integer.toHexString(i)
                if (hexString.length < 2) {
                    //如果是一位的话，补0
                    hexString = "0$hexString"
                }
                sb.append(hexString)
            }
            return sb.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 获取表单的请求参数
     * @param request
     * @return
     */
    private fun doForm(request: Request): Map<String, String>? {
        var params: MutableMap<String, String>? = null
        var body: FormBody? = null
        try {
            body = request.body as FormBody?
        } catch (c: ClassCastException) {
        }

        if (body != null) {
            val size = body.size
            if (size > 0) {
                params = java.util.HashMap()
                for (i in 0 until size) {
                    params[body.name(i)] = body.value(i)
                }
            }
        }
        return params
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
}