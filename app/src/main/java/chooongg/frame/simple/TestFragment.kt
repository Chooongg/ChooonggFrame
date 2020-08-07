package chooongg.frame.simple

import android.os.Bundle
import chooongg.frame.core.annotation.ContentLayout
import chooongg.frame.core.fragment.ChooonggFragment
import chooongg.frame.core.loadState.LoadUtils

@ContentLayout(R.layout.fragment_test)
class TestFragment : ChooonggFragment() {
    override fun lazyLoad() {
    }

    override fun initConfig(savedInstanceState: Bundle?) {
//        LoadUtils.getDefault().register(fragment) {
//
//        }
    }
}