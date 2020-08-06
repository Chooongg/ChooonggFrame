package chooongg.frame.simple

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import chooongg.frame.ChooonggFrame
import chooongg.frame.core.activity.ChooonggActivity
import chooongg.frame.core.annotation.ContentLayout
import chooongg.frame.core.annotation.TitleBar
import chooongg.frame.core.annotation.WindowBackground
import chooongg.frame.http.request.retrofitDefault
import chooongg.frame.simple.api.APIResponse
import chooongg.frame.simple.api.TestAPI
import chooongg.frame.utils.doOnClick
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

@ContentLayout(R.layout.activity_main)
@WindowBackground(R.color.colorBackground)
@TitleBar(true, true, TitleBar.SURFACE)
class MainActivity : ChooonggActivity() {

    override fun initConfig(savedInstanceState: Bundle?) {

    }

    override fun initContent(savedInstanceState: Bundle?) {
        iv_image.doOnClick {
            val launchIO = lifecycleScope.launch {
                retrofitDefault<APIResponse<Any>> {
                    api = TestAPI.service.sendSms("15533906327", 1)
                    onStart {
                        showLoading()
                    }
                    onSuccess {
                        Log.e(ChooonggFrame.TAG, "initConfig: 请求成功")
                    }
                    onFailed {
                        Log.e(ChooonggFrame.TAG, "initConfig: 请求失败")
                    }
                    onEnd {
                        hideLoading()
                    }
                }
            }
        }
    }
}