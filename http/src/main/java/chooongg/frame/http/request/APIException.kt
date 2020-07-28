package chooongg.frame.http.request

import android.net.ParseException
import android.util.MalformedJsonException
import chooongg.frame.utils.NetworkUtils
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * 网络异常
 */
class APIException : Throwable {

    private val mMessage: String
    val code: String
    val type: ExceptionType

    enum class ExceptionType private constructor(code: Int, message: String) {
        CUSTOM(-1, "自定义"),
        EMPTY(-2, "没有数据"),
        UNKNOWN(-3, "未知错误"),
        ERROR_PARSE(-4, "解析错误"),
        ERROR_NETWORK(-5, "请检查网络连接"),
        ERROR_SSL(-6, "证书验证失败"),
        ERROR_TIMEOUT(-7, "连接超时"),
        NOT_LOGIN(-8, "用户未登录"),
        REQUEST_400(400, "请求失败"),
        REQUEST_401(401, "请求未授权"),
        REQUEST_403(403, "请求被拒绝"),
        REQUEST_404(404, "未找到该请求"),
        REQUEST_405(405, "请求方法被禁用"),
        REQUEST_406(406, "服务器不接收请求内容"),
        REQUEST_407(407, "需要推广员授权"),
        REQUEST_408(408, "请求超时"),
        REQUEST_409(409, "发生冲突"),
        REQUEST_410(410, "资源已删除"),
        REQUEST_411(411, "需要有效长度"),
        REQUEST_412(412, "未满足前提条件"),
        REQUEST_413(413, "请求实体过大"),
        REQUEST_414(414, "请求的 URI 过长"),
        REQUEST_415(415, "不支持的媒体类型"),
        REQUEST_416(416, "请求范围不符合要求"),
        REQUEST_417(417, "未满足期望值"),
        SERVICE_500(500, "服务器异常"),
        SERVICE_501(501, "服务器不具备完成请求的功能"),
        SERVICE_502(502, "网关错误"),
        SERVICE_503(503, "服务不可用"),
        SERVICE_504(504, "网关超时"),
        SERVICE_505(505, "HTTP版本不受支持");

        var code: Int = 0
            internal set
        var message: String
            internal set

        init {
            this.code = code
            this.message = message
        }
    }

    constructor(message: String) {
        this.code = ""
        this.mMessage = message
        type = ExceptionType.CUSTOM
    }

    constructor(code: Int, message: String) {
        this.code = code.toString()
        this.mMessage = message
        this.type = ExceptionType.CUSTOM
    }

    constructor(code: String, message: String) {
        this.code = code
        this.mMessage = message
        this.type = ExceptionType.CUSTOM
    }

    constructor(type: ExceptionType) {
        this.type = type
        this.code = type.code.toString()
        this.mMessage = type.message
    }

    constructor(code: Int) {
        var type: ExceptionType = ExceptionType.UNKNOWN
        var temp = false
        for (i in ExceptionType.values().indices) {
            if (ExceptionType.values()[i].code == code) {
                type = ExceptionType.values()[i]
                temp = true
                break
            }
        }
        if (!temp) {
            type = ExceptionType.CUSTOM
        }
        this.type = type
        this.code = code.toString()
        this.mMessage = this.type.message
    }

    fun eMessage(): String {
        if (type == ExceptionType.CUSTOM) {
            return mMessage
        }
        return if (type.code == -1
                || type.code == -2
                || type.code == -3
                || type.code == -4
                || type.code == -5
                || type.code == -6
                || type.code == -7
        ) {
            type.message
        } else {
            type.message
//            "${type.message}\n[错误码:${type.code}]"
        }
    }

    companion object {
        fun convertException(e: Throwable): APIException {
            if (e is APIException) return e
            val exception: APIException
            if (!NetworkUtils.isNetworkConnected()) {
                exception = APIException(ExceptionType.ERROR_NETWORK)
            } else if (e is UnknownHostException
                    || e is ConnectException) {
                exception = APIException(ExceptionType.ERROR_NETWORK)
            } else if (e is SocketTimeoutException) {
                exception = APIException(ExceptionType.ERROR_TIMEOUT)
            } else if (e is HttpException) {
                exception = APIException(e.code())
            } else if (e is JsonParseException
                    || e is JsonSyntaxException
                    || e is JSONException
                    || e is ParseException
                    || e is NullPointerException
                    || e is MalformedJsonException
            ) {
                exception = APIException(ExceptionType.ERROR_PARSE)
            } else if (e is javax.net.ssl.SSLHandshakeException) {
                exception = APIException(ExceptionType.ERROR_SSL)
            } else {
                exception = APIException(ExceptionType.UNKNOWN)
            }
            return exception
        }
    }
}
