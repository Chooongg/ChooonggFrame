package chooongg.frame

import android.app.Application
import chooongg.frame.manager.AppManager

object ChooonggFrame {

    const val TAG = "CHOOONGG_FRAME"

    fun initialize(application: Application) {
        AppManager.initialize(application)
    }
}