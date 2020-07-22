package chooongg.frame.http.exception

class HttpException : Throwable {

    constructor() : super(null, null)

    constructor(message: String?) : super(message, null)

    constructor(cause: Throwable?) : super(cause?.toString(), cause)

    init {

    }


}