package chooongg.frame.core.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import chooongg.frame.core.annotation.FragmentTitle
import chooongg.frame.core.interfaces.Init
import chooongg.frame.log.L

abstract class ChooonggFragment : Fragment(), Init {

    companion object {
        fun getTitle(clazz: Class<out ChooonggFragment>) =
            if (clazz.isAnnotationPresent(FragmentTitle::class.java)) {
                clazz.getAnnotation(FragmentTitle::class.java)!!.value
            } else null

    }

    private var contentView: View? = null

    private var isCreated = false

    var isFirstLoad = true
        private set

    abstract fun lazyLoad()

    open fun onReselected() = Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return try {
            inflater.inflate(getContentLayout(), container, false).apply {
                contentView = this
                isCreated = true
            }
        } catch (e: Exception) {
            L.e("${javaClass.simpleName} setContentLayout operation there is an exception", e)
            null
        }
    }

    override fun initContent(savedInstanceState: Bundle?) = Unit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isCreated.not()) return
        try {
            initConfig(savedInstanceState)
        } catch (e: Exception) {
            L.e("${javaClass.simpleName} initConfig() there is an exception", e)
            return
        }
        try {
            initContent(savedInstanceState)
        } catch (e: Exception) {
            L.e("${javaClass.simpleName} initContent() there is an exception", e)
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
}