package chooongg.frame.simple.api

import androidx.annotation.Keep

@Keep
data class APIResponse<T>(val code: Int?, val msg: String?, val data: T?)