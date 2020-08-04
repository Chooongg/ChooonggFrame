package chooongg.frame.simple

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import chooongg.frame.core.activity.ChooonggActivity
import chooongg.frame.core.annotation.ContentLayout
import chooongg.frame.core.annotation.TitleBar
import chooongg.frame.http.TestAPI
import chooongg.frame.http.request.DefaultResponseCallback
import chooongg.frame.http.request.request
import chooongg.frame.log.L
import chooongg.frame.utils.doOnClick
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@ContentLayout(R.layout.activity_main)
@TitleBar(true, true, TitleBar.SURFACE)
class MainActivity : ChooonggActivity() {

    override fun initConfig(savedInstanceState: Bundle?) {
        chooonggToolbar?.setNavigationIcon(R.drawable.ic_arrow_back)
//        lifecycleScope.http<String> {
//            api { TestAPI.service().test() }
//            request(DefaultResponseCallback {
//                start {
//                }
//                success {
//
//                }
//            })
//        }
        lifecycleScope.launch {
            TestAPI.service().test2().enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {

                }

                override fun onResponse(call: Call<String>, response: Response<String>) {

                }
            })
            TestAPI.service().test2().request(object : DefaultResponseCallback<String> {
                override fun onSuccess(data: String?) {

                }
            })
        }
    }

    override fun initContent(savedInstanceState: Bundle?) {
        tv_test.doOnClick {
            val intent = Intent(context, TwoActivity::class.java)
            L.e(intent.toString())
            startActivity(intent)
        }
    }
}