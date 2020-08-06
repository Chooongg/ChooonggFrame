package chooongg.frame.http.manager

import com.google.gson.Gson

object GsonManager {

    val gson by lazy { Gson() }

}

val gson get() = GsonManager.gson