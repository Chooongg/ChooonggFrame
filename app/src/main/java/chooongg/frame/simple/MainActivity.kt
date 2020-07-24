package chooongg.frame.simple

import android.os.Bundle
import chooongg.frame.core.activity.ChooonggActivity
import chooongg.frame.core.annotation.ContentLayout
import chooongg.frame.throwable.ChooonggFrameException

@ContentLayout(R.layout.activity_main)
class MainActivity : ChooonggActivity() {

    override fun initConfig(savedInstanceState: Bundle?) {
        throw ChooonggFrameException("测试异常")
    }

    override fun initContent(savedInstanceState: Bundle?) {
        throw ChooonggFrameException("测试异常")
    }
}