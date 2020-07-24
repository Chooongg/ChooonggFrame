package chooongg.frame

import android.app.Application
import chooongg.frame.log.L
import chooongg.frame.manager.AppManager

object ChooonggFrame {

    const val TAG = "ChooonggFrame"

    fun initialize(application: Application) {
        AppManager.initialize(application)
        loadLibrary(application)
    }

    /**
     * 加载同级别动态库
     */
    private fun loadLibrary(application: Application) {
        loadCore(application)
        loadHttp(application)
    }

    private fun loadCore(application: Application) {
        try {
            val loadClass =
                application.classLoader.loadClass("chooongg.frame.core.ChooonggCore")
            val method = loadClass.getMethod("initialize", Application::class.java)
            method.invoke(loadClass, application)
        } catch (e: Exception) {
            L.e("找不到该类")
        }
    }

    private fun loadHttp(application: Application) {
        try {
            val loadClass =
                application.classLoader.loadClass("chooongg.frame.http.ChooonggHttp")
            val method = loadClass.getDeclaredMethod("initialize", Application::class.java)
            method.invoke(loadClass, application)
        } catch (e: Exception) {
            L.e("找不到该类")
        }
    }
}