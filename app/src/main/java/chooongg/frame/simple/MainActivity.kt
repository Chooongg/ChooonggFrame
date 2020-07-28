package chooongg.frame.simple

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import chooongg.frame.core.activity.ChooonggActivity
import chooongg.frame.core.annotation.ContentLayout
import chooongg.frame.core.annotation.TranslucentStatusBar

@ContentLayout(R.layout.activity_main)
@TranslucentStatusBar
class MainActivity : ChooonggActivity() {

    override fun initConfig(savedInstanceState: Bundle?) {
        lifecycleScope
    }

    override fun initContent(savedInstanceState: Bundle?) {
    }
}