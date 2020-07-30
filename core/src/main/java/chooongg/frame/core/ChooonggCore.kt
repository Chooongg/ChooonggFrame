package chooongg.frame.core

import android.app.Application
import android.util.Log
import chooongg.frame.ChooonggFrame
import chooongg.frame.throwable.ChooonggFrameException
import com.tencent.mmkv.MMKV

object ChooonggCore {

    private var isInitialized = false

    @JvmStatic
    @Suppress("unused")
    fun initialize(application: Application) {
        if (!isInitialized) {
            isInitialized = true
            MMKV.initialize(application)
            Log.d(ChooonggFrame.TAG, "ChooonggCore Initialize Finish!")
        } else throw ChooonggFrameException("ChooonggCore don't repeat initialize")
    }
}