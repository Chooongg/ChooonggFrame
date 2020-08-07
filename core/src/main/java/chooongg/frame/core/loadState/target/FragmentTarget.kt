package chooongg.frame.core.loadState.target

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import chooongg.frame.core.loadState.LoadLayout
import chooongg.frame.core.loadState.callback.Callback
import chooongg.frame.core.loadState.callback.SuccessCallback
import chooongg.frame.throwable.ChooonggFrameException

class FragmentTarget : ITarget {
    override fun withEquals(target: Any?) = target is Fragment

    override fun replaceView(
        target: Any,
        onReloadListener: ((Class<out Callback>) -> Unit)?
    ): LoadLayout {
        val fragment = target as Fragment
        val contentParent = fragment.requireView().parent as ViewGroup
        if (contentParent.childCount <= 0) throw ChooonggFrameException("parent child count is zero")
        val oldContent = contentParent.getChildAt(0)
        val background = oldContent.background
        contentParent.removeView(oldContent)
        val oldLayoutParams = oldContent.layoutParams
        val loadLayout = LoadLayout(fragment.requireContext(), onReloadListener)
        loadLayout.background = background
        loadLayout.setupSuccessLayout(
            SuccessCallback(
                oldContent,
                fragment.requireContext(),
                onReloadListener
            )
        )
        contentParent.addView(loadLayout, oldLayoutParams)
        return loadLayout
    }
}