package chooongg.frame.simple

import android.os.Bundle
import chooongg.frame.core.activity.ChooonggActivity
import chooongg.frame.core.annotation.ContentLayout
import chooongg.frame.core.annotation.TitleBar

@ContentLayout(R.layout.activity_main)
@TitleBar(true, true, TitleBar.SURFACE)
class TwoActivity : ChooonggActivity() {

    override fun initConfig(savedInstanceState: Bundle?) {
    }

    override fun initContent(savedInstanceState: Bundle?) {
    }
}