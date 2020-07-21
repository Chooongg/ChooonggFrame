package chooongg.frame.manager

import android.app.Application
import chooongg.frame.throwable.ChooonggFrameException

inline val APP get() = AppManager.getApplication()

object AppManager {

    private var application: Application? = null


    fun initialize(application: Application) {
        if (this.application != null) throw ChooonggFrameException("你已初始化Application！")
        this.application = application
    }

    fun getApplication() =
        if (application != null) application!! else throw ChooonggFrameException("你已初始化Application！")

}