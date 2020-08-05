package chooongg.frame.http.request

interface DefaultResponseCallback<RESPONSE> : ResponseCallback<RESPONSE, RESPONSE> {
    override fun onResponse(response: RESPONSE) {
        onSuccess(response)
    }
}