package chooongg.frame.core.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import chooongg.frame.core.activity.ChooonggActivity
import chooongg.frame.core.interfaces.Init
import chooongg.frame.permission.handlePermissionsResult
import com.orhanobut.logger.Logger

abstract class ChooonggFragment : Fragment(), Init {

    inline val fragment get() = this

    protected lateinit var contentView: View
        private set

    private var isCreated = false

    var isFirstLoad = true
        private set

    abstract fun lazyLoad()

    open fun onReselected() = Unit

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return try {
            inflater.inflate(getContentLayout(), container, false).apply {
                contentView = this
                if (getWindowBackgroundRes() != null) {
                    contentView.setBackgroundResource(getWindowBackgroundRes()!!)
                }
                isCreated = true
            }
        } catch (e: Exception) {
            Logger.e(e, "${javaClass.simpleName} setContentView()")
            null
        }
    }

    override fun initContent(savedInstanceState: Bundle?) = Unit

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isCreated.not()) return
        try {
            initConfig(savedInstanceState)
        } catch (e: Exception) {
            Logger.e(e, "${javaClass.simpleName} initConfig()")
            return
        }
        try {
            initContent(savedInstanceState)
        } catch (e: Exception) {
            Logger.e(e, "${javaClass.simpleName} initContent()")
            return
        }
    }

    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            isFirstLoad = false
            lazyLoad()
        }
    }

    fun showLoading(message: CharSequence? = null, isClickable: Boolean = false) {
        if (activity is ChooonggActivity) (activity as ChooonggActivity).showLoading(
            message,
            isClickable
        )
    }

    fun showLoading(resId: Int) {
        if (activity is ChooonggActivity) (activity as ChooonggActivity).showLoading(resId)
    }

    fun hideLoading() {
        if (activity is ChooonggActivity) (activity as ChooonggActivity).hideLoading()
    }

    final override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        handlePermissionsResult(requestCode, permissions, grantResults)
    }
}