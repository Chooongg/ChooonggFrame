package chooongg.frame.simple

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import chooongg.frame.core.activity.ChooonggActivity
import chooongg.frame.core.annotation.ContentLayout
import chooongg.frame.core.annotation.TitleBar
import chooongg.frame.http.request.DefaultResponseCallback
import chooongg.frame.http.request.request
import chooongg.frame.simple.api.TestAPI
import chooongg.frame.utils.doOnClick
import chooongg.frame.utils.launchIO
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@ContentLayout(R.layout.activity_main)
@TitleBar(true, true, TitleBar.SURFACE)
class MainActivity : ChooonggActivity() {

    override fun initConfig(savedInstanceState: Bundle?) {
        chooonggToolbar?.setNavigationIcon(R.drawable.ic_arrow_back)
        val editText = EditText(context)
    }

    override fun initContent(savedInstanceState: Bundle?) {
        iv_image.doOnClick {
//            val intent = Intent(context, TwoActivity::class.java)
//            L.e(intent.toString())
//            startActivity(intent)
            lifecycleScope.launchIO {
                TestAPI.service.sendSms("15533906327", 1)
                    .request(response {
                        Log.e("HTTP", "initContent: 成功")
                    })
            }
        }
    }

    inline fun <RESPONSE> response(crossinline successBlock: suspend RESPONSE?.() -> Unit): DefaultResponseCallback<RESPONSE> {
        val value = object : DefaultResponseCallback<RESPONSE> {
            override suspend fun onSuccess(data: RESPONSE?) {
                successBlock.invoke(data)
            }
        }
        return value
    }

    class Test<RESPONSE> : DefaultResponseCallback<RESPONSE> {

        private constructor()

        constructor(block: suspend Test<RESPONSE>.() -> Unit) {
            suspend {
                block(suspendCoroutine {
                    it.resume(Test())
                })
            }
        }

        private var successBlock: (suspend RESPONSE?.() -> Unit)? = null

        fun success(block: suspend RESPONSE?.() -> Unit) {
            successBlock = block
        }

        override suspend fun onSuccess(data: RESPONSE?) {
            successBlock?.invoke(data)
        }
    }
}