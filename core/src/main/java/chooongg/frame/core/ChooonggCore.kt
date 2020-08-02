package chooongg.frame.core

import android.app.Application
import android.util.Log
import chooongg.frame.ChooonggFrame
import chooongg.frame.throwable.ChooonggFrameException

@Suppress("unused")
object ChooonggCore {

    private var isInitialized = false

    @JvmStatic
    @Suppress("unused", "UNUSED_PARAMETER")
    fun initialize(application: Application) {
        if (!isInitialized) {
            isInitialized = true
            Log.d(ChooonggFrame.TAG, "ChooonggCore Initialize Finish!")
        } else throw ChooonggFrameException("ChooonggCore don't repeat initialize")
    }
}