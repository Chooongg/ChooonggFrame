package chooongg.frame.simple

import android.os.Bundle
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import chooongg.frame.core.activity.ChooonggActivity
import chooongg.frame.core.annotation.ContentLayout
import chooongg.frame.core.annotation.TitleBar
import chooongg.frame.http.request.DefaultResponseCallback
import chooongg.frame.http.request.request
import chooongg.frame.simple.api.APIResponse
import chooongg.frame.simple.api.TestAPI
import chooongg.frame.utils.doOnClick
import chooongg.frame.utils.launchIO
import kotlinx.android.synthetic.main.activity_main.*

@ContentLayout(R.layout.activity_main)
@TitleBar(true, true, TitleBar.SURFACE)
class MainActivity : ChooonggActivity() {

    override fun initConfig(savedInstanceState: Bundle?) {
        chooonggToolbar?.setNavigationIcon(R.drawable.ic_arrow_back)
        val editText = EditText(context)
    }

    override fun initContent(savedInstanceState: Bundle?) {
        iv_image.doOnClick {
            lifecycleScope.launchIO {
                TestAPI.service.sendSms("15533906327", 1)
                    .request(object : DefaultResponseCallback<APIResponse<Any>> {
                        override suspend fun onSuccess(data: APIResponse<Any>?) {

                        }
                    })
            }
        }
    }

}