package chooongg.frame.simple

import android.app.Application
import chooongg.frame.ChooonggFrame

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ChooonggFrame.initialize(this)
    }

}