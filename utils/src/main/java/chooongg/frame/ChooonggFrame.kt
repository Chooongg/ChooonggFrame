package chooongg.frame

import android.app.Application
import chooongg.frame.manager.ActivityTaskManager
import chooongg.frame.manager.AppManager
import chooongg.frame.manager.LoggerManager
import com.orhanobut.logger.Logger
import com.tencent.mmkv.MMKV

object ChooonggFrame {

    const val TAG = "ChooonggFrame"

    fun initialize(application: Application) {
        AppManager.initialize(application)
        application.registerActivityLifecycleCallbacks(ActivityTaskManager)
        MMKV.initialize(application)
        LoggerManager.initialize()
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
            Logger.e("找不到该类", e)
        }
    }

    private fun loadHttp(application: Application) {
        try {
            val loadClass =
                application.classLoader.loadClass("chooongg.frame.http.ChooonggHttp")
            val method = loadClass.getDeclaredMethod("initialize", Application::class.java)
            method.invoke(loadClass, application)
        } catch (e: Exception) {
            Logger.e("找不到该类", e)
        }
    }
}