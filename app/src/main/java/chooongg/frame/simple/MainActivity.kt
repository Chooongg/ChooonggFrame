package chooongg.frame.simple

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import chooongg.frame.core.activity.ChooonggActivity
import chooongg.frame.core.annotation.ContentLayout
import chooongg.frame.core.annotation.TranslucentStatusBar
import chooongg.frame.http.TestAPI
import chooongg.frame.http.request.http

@ContentLayout(R.layout.activity_main)
@TranslucentStatusBar
class MainActivity : ChooonggActivity() {

    override fun initConfig(savedInstanceState: Bundle?) {
        lifecycleScope.http<String> {
            api { TestAPI.service().test() }
            request {
                response {

                }
            }
        }
        lifecycleScope.launchWhenCreated {  }
    }

    override fun initContent(savedInstanceState: Bundle?) {
    }
}