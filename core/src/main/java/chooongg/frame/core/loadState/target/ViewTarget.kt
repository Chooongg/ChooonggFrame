package chooongg.frame.core.loadState.target

import android.view.View
import android.view.ViewGroup
import chooongg.frame.core.R
import chooongg.frame.core.loadState.LoadLayout
import chooongg.frame.core.loadState.callback.Callback
import chooongg.frame.core.loadState.callback.SuccessCallback

class ViewTarget : ITarget {
    override fun withEquals(target: Any?) = target is View

    override fun replaceView(
        target: Any,
        onReloadListener: ((Class<out Callback>) -> Unit)?
    ): LoadLayout {
        val oldContent = target as View
        val background = oldContent.background
        val contentParent = oldContent.parent as? ViewGroup
        var childIndex = 0
        val childCount = contentParent?.childCount ?: 0
        for (i in 0 until childCount) {
            if (contentParent!!.getChildAt(i) == oldContent) {
                childIndex = i
                break
            }
        }
        contentParent?.removeView(oldContent)
        val oldlayoutParams = oldContent.layoutParams
        val loadLayout = LoadLayout(oldContent.context, onReloadListener)
        loadLayout.background = background
        loadLayout.setupSuccessLayout(
            SuccessCallback(
                oldContent,
                oldContent.context,
                onReloadListener
            )
        )
        loadLayout.id = R.id.load_layout
        contentParent?.addView(loadLayout, childIndex, oldlayoutParams)
        return loadLayout
    }
}