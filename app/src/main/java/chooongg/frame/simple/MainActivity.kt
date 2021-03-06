package chooongg.frame.simple

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import chooongg.frame.core.activity.ChooonggActivity
import chooongg.frame.core.annotation.ContentLayout
import chooongg.frame.core.annotation.TitleBar
import chooongg.frame.core.annotation.WindowBackground
import chooongg.frame.core.loadState.LoadUtils
import chooongg.frame.utils.doOnClick
import chooongg.frame.utils.withMain
import coil.api.load
import com.fondesa.recyclerviewdivider.addDivider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_test.*
import kotlinx.coroutines.launch

@ContentLayout(R.layout.activity_main)
@WindowBackground(R.color.colorBackground)
@TitleBar(true, true, TitleBar.SURFACE)
class MainActivity : ChooonggActivity() {

    override fun initConfig(savedInstanceState: Bundle?) {
        view_pager.view_pager.adapter = object : FragmentStateAdapter(activity) {
            override fun getItemCount() = 2
            override fun createFragment(position: Int) = TestFragment()
        }
        recycler_view.addDivider()
        val register = LoadUtils.getDefault().register(activity) {

        }
        register.showSuccess()
        lifecycleScope.launch {
            withMain {
                iv_image.load("https://img-blog.csdnimg.cn/20190821180619507.jpg") {
                    crossfade(true)
                    placeholder(R.drawable.ic_launcher_background)
                    iv_image.doOnClick {

                    }
                }
            }
        }
    }

    override fun initContent(savedInstanceState: Bundle?) {
//        iv_image.doOnClick {
//            val launchIO = lifecycleScope.launch {
//                retrofitDefault<APIResponse<Any>> {
//                    api = TestAPI.service.sendSms("15533906327", 1)
//                    onStart {
//                        showLoading()
//                    }
//                    onSuccess {
//                        Log.e(ChooonggFrame.TAG, "initConfig: 请求成功")
//                    }
//                    onFailed {
//                        Log.e(ChooonggFrame.TAG, "initConfig: 请求失败")
//                    }
//                    onEnd {
//                        hideLoading()
//                    }
//                }
//            }
//        }
    }
}