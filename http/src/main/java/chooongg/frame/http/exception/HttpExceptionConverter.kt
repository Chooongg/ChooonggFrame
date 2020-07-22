package chooongg.frame.http.exception

interface HttpExceptionConverter {
    fun converterRelease(type: HttpExceptionType): String
    fun converterDebug(type: HttpExceptionType): String
}