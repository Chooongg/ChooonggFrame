package chooongg.frame.http.request

interface DefaultResponseCallback<RESPONSE> : ResponseCallback<RESPONSE, RESPONSE> {
    override suspend fun onResponse(response: RESPONSE) {
        onSuccess(response)
    }
}