package chooongg.frame.http.request

import chooongg.frame.http.exception.HttpException

interface HttpConverter<T, E> {

    @Throws(HttpException::class)
    fun convert(t: T): E

}