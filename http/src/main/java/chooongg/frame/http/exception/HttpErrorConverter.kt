package chooongg.frame.http.exception

import chooongg.frame.http.BuildConfig

/**
 * 错误类型转换成语义化文字
 */
abstract class HttpErrorConverter {

    abstract fun convertRelease(type: HttpException.Type): String
    abstract fun convertDebug(type: HttpException.Type): String

    fun convert(type: HttpException.Type) =
        if (BuildConfig.DEBUG) convertDebug(type) else convertRelease(type)
}