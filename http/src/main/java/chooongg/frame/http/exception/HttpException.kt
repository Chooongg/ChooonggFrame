package chooongg.frame.http.exception

import android.net.ParseException
import android.util.MalformedJsonException
import chooongg.frame.utils.NetworkUtils
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import org.json.JSONException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

class HttpException : RuntimeException {

    object Converter {
        private var converter: HttpErrorConverter = DefaultErrorConverter()

        fun convert(type: Type): String = converter.convert(type)

        /**
         * 修改转换器
         */
        fun changeConverter(converter: HttpErrorConverter) {
            this.converter = converter
        }
    }

    var type: Type
        private set(value) {
            field = value
            code = type.value.toString()
            messageCopy = Converter.convert(type)
        }
    var code: String = ""
        private set
    private var messageCopy: String = ""

    constructor() : super() {
        this.type = Type.UN_KNOWN
    }

    constructor(type: Type) : super() {
        this.type = type
    }

    constructor(code: Int) : super() {
        var tempType = Type.UN_KNOWN
        for (i in Type.values().indices) {
            if (Type.values()[i].value == code) {
                tempType = Type.values()[i]
                break
            }
        }
        this.type = tempType
    }

    constructor(code: String, message: String) : super(message) {
        this.type = Type.CUSTOM
        this.code = code
        this.messageCopy = message
    }

    constructor(message: String) : super(message) {
        this.type = Type.CUSTOM
        this.messageCopy = message
    }

    constructor(message: String, cause: Throwable) : super(message, cause) {
        this.type = Type.CUSTOM
        this.messageCopy = message
    }

    constructor(e: Throwable) : super(e.toString(), e) {
        if (e is HttpException) {
            this.type = e.type
        } else {
            this.type = when {
                !NetworkUtils.isNetworkConnected() -> Type.NETWORK
                e is ConnectException
                        || e is UnknownHostException -> Type.CONNECT
                e is SocketTimeoutException -> Type.TIMEOUT
                e is retrofit2.HttpException -> {
                    var tempType = Type.UN_KNOWN
                    for (i in Type.values().indices) {
                        if (Type.values()[i].value == e.code()) {
                            tempType = Type.values()[i]
                            break
                        }
                    }
                    tempType
                }
                e is JsonParseException
                        || e is JsonSyntaxException
                        || e is JSONException
                        || e is ParseException
                        || e is NullPointerException
                        || e is MalformedJsonException -> Type.PARSE
                e is SSLHandshakeException -> Type.SSL
                else -> Type.UN_KNOWN
            }
        }
    }

    override val message: String?
        get() = messageCopy

    enum class Type(val value: Int) {
        UN_KNOWN(-1), CUSTOM(-2),
        NETWORK(-3), TIMEOUT(-4),
        PARSE(-5),
        SSL(-6),
        EMPTY(-7),
        CONNECT(-8),
        HTTP400(400),
        HTTP401(401),
        HTTP403(403),
        HTTP404(404),
        HTTP405(405),
        HTTP406(406),
        HTTP407(407),
        HTTP408(408),
        HTTP409(409),
        HTTP410(410),
        HTTP411(411),
        HTTP412(412),
        HTTP413(413),
        HTTP414(414),
        HTTP415(415),
        HTTP416(416),
        HTTP417(417),
        HTTP500(500),
        HTTP501(501),
        HTTP502(502),
        HTTP503(503),
        HTTP504(504),
        HTTP505(505);
    }

    class DefaultErrorConverter : HttpErrorConverter() {
        override fun convertRelease(type: Type) = when (type) {
            Type.CUSTOM -> "用户自定义错误"
            Type.NETWORK -> "请检查网络连接"
            Type.TIMEOUT -> "请求连接超时"
            Type.PARSE -> "数据出现异常"
            Type.SSL -> "验证失败"
            Type.EMPTY -> "没有数据"
            Type.HTTP400, Type.HTTP401, Type.HTTP403, Type.HTTP404, Type.HTTP405, Type.HTTP406, Type.HTTP407, Type.HTTP408,
            Type.HTTP409, Type.HTTP410, Type.HTTP411, Type.HTTP412, Type.HTTP413, Type.HTTP414, Type.HTTP415, Type.HTTP416,
            Type.HTTP417 -> "请求出现错误"
            Type.HTTP500, Type.HTTP501, Type.HTTP502,
            Type.HTTP503, Type.HTTP504, Type.HTTP505 -> "服务器遇到错误"
            else -> "未知错误"
        }

        override fun convertDebug(type: Type) = when (type) {
            Type.CUSTOM -> "用户自定义错误"
            Type.NETWORK -> "网络不可用"
            Type.TIMEOUT -> "请求连接超时"
            Type.PARSE -> "请求实体解析失败"
            Type.SSL -> "SSL证书验证失败"
            Type.EMPTY -> "Body为空"
            Type.HTTP400 -> "400:服务器不理解请求的语法"
            Type.HTTP401 -> "401:请求要求身份验证"
            Type.HTTP403 -> "403:服务器拒绝请求"
            Type.HTTP404 -> "404:服务器找不到请求的资源"
            Type.HTTP405 -> "405:禁用请求中指定的方法"
            Type.HTTP406 -> "406:无法使用请求的内容特性响应请求的资源"
            Type.HTTP407 -> "407:请求要求使用代理授权身份"
            Type.HTTP408 -> "408:服务器等候请求时发生超时"
            Type.HTTP409 -> "409:服务器再完成请求时发生冲突"
            Type.HTTP410 -> "410:请求的资源已删除"
            Type.HTTP411 -> "411:服务器不接受不含有效内容长度标头字段的请求"
            Type.HTTP412 -> "412:服务器未满足请求中设置的前提条件"
            Type.HTTP413 -> "413:请求实体过大超出服务器的处理能力"
            Type.HTTP414 -> "414:请求的URI过长"
            Type.HTTP415 -> "415:请求格式不受支持"
            Type.HTTP416 -> "416:请求范围不符合要求"
            Type.HTTP417 -> "417:请求未满足期望值"
            Type.HTTP500 -> "500:服务器遇到错误"
            Type.HTTP501 -> "501:服务器不具备完成请求的功能"
            Type.HTTP502 -> "502:服务器网关错误"
            Type.HTTP503 -> "503:服务器暂时不可用"
            Type.HTTP504 -> "504:服务器网关超时"
            Type.HTTP505 -> "505:服务器不支持HTTP协议"
            else -> "未知错误"
        }
    }
}