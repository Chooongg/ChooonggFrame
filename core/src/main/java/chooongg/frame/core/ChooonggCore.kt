package chooongg.frame.core

import android.app.Application
import chooongg.frame.log.L
import chooongg.frame.throwable.ChooonggFrameException

object ChooonggCore {

    private var isInitialized = false

    @JvmStatic
    @Suppress("unused")
    fun initialize(application: Application) {
        if (!isInitialized) {
            isInitialized = true
            L.e("测试初始化Core")
        } else throw ChooonggFrameException("ChooonggCore不可重复初始化")
    }
}