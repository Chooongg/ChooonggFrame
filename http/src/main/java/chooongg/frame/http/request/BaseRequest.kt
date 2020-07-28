package chooongg.frame.http.request

import kotlinx.coroutines.CoroutineScope

fun <RESPONSE> CoroutineScope.request(api: () -> RESPONSE, callback: HttpCallback<RESPONSE>) {

}