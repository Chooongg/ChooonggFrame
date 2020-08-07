package chooongg.frame.simple

import android.app.Application
import chooongg.frame.ChooonggFrame
import chooongg.frame.core.loadState.LoadUtils
import chooongg.frame.core.loadState.callback.DefaultLoadingCallback

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ChooonggFrame.initialize(this)
        LoadUtils.Builder().setDefaultCallback(DefaultLoadingCallback::class)
            .addCallback(DefaultLoadingCallback::class)
            .commit()
    }

}