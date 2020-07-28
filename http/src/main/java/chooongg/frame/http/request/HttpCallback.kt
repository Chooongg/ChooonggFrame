package chooongg.frame.http.request

import chooongg.frame.http.exception.HttpException

class HttpCallback<RESPONSE>() {

    fun response(block: (RESPONSE) -> Unit) {

    }

    internal fun error(error: HttpException) {

    }


}