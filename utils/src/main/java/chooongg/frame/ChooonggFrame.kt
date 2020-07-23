package chooongg.frame

import android.app.Application
import chooongg.frame.manager.AppManager

object ChooonggFrame {

    const val TAG = "CHOOONGG_FRAME"

    fun initialize(application: Application) {
        AppManager.initialize(application)
        initializeThirdLibrary(application)
    }

    private fun initializeThirdLibrary(application: Application) {
        initCoreLibrary(application)
    }

    private fun initCoreLibrary(application: Application) {
        try {
            val clazz = Class.forName("chooongg.frame.core.ChooonggFrameCoreKt")
            val method = clazz.getMethod("initialize", Application::class.java)
            method.invoke(null, application)
        } catch (e: Exception) {
        }
    }
}