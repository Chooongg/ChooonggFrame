package chooongg.frame.simple

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import chooongg.frame.core.activity.ChooonggActivity
import chooongg.frame.core.annotation.ContentLayout
import chooongg.frame.core.annotation.TranslucentStatusBar
import chooongg.frame.http.TestAPI
import chooongg.frame.http.request.HttpCallback
import chooongg.frame.http.request.http

@ContentLayout(R.layout.activity_main)
@TranslucentStatusBar
class MainActivity : ChooonggActivity() {

    override fun initConfig(savedInstanceState: Bundle?) {
        lifecycleScope.http<String> {
            api { TestAPI.service().test() }
            request(RequestCallback {

            })
        }
        lifecycleScope.launchWhenCreated { }
    }

    override fun initContent(savedInstanceState: Bundle?) {
    }

    class RequestCallback<RESPONSE>(block: RequestCallback<RESPONSE>.() -> Unit) :
        HttpCallback<RequestCallback<RESPONSE>, RESPONSE>(block) {



    }
}