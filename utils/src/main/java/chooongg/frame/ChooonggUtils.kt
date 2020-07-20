package chooongg.frame

import android.app.Application
import chooongg.frame.manager.AppManager

object ChooonggUtils {

    fun initialize(application: Application) {
        AppManager.initialize(application)
    }
}