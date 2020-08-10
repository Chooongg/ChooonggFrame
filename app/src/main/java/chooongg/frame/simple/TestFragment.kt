package chooongg.frame.simple

import android.os.Bundle
import chooongg.frame.core.annotation.ContentLayout
import chooongg.frame.core.fragment.ChooonggFragment
import com.fondesa.recyclerviewdivider.dividerBuilder
import kotlinx.android.synthetic.main.fragment_test.*

@ContentLayout(R.layout.fragment_test)
class TestFragment : ChooonggFragment() {
    override fun lazyLoad() {
    }

    override fun initConfig(savedInstanceState: Bundle?) {
        requireContext().dividerBuilder().build().addTo(recycler_view)
//        LoadUtils.getDefault().register(fragment) {
//
//        }
    }
}