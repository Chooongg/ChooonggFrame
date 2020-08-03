package chooongg.base.loadState.target

import android.app.Activity
import chooongg.base.loadState.LoadLayout
import chooongg.frame.core.loadState.callback.Callback
import chooongg.frame.core.loadState.callback.SuccessCallback
import chooongg.base.utils.contentView
import chooongg.base.widget.titleBar.AbsTitleBar

class ActivityTarget : ITarget {

    override fun withEquals(target: Any?) = target is Activity

    override fun replaceView(
        target: Any,
        onReloadListener: ((Class<out Callback>) -> Unit)?
    ): LoadLayout {
        val activity = target as Activity
        val contentParent = activity.contentView
        var childIndex = 0
        for (i in 0 until contentParent.childCount) {
            if (contentParent.getChildAt(i) !is AbsTitleBar) {
                childIndex = i
                break
            }
        }
        val oldContent = contentParent.getChildAt(childIndex)
        val background = oldContent.background
        contentParent.removeView(oldContent)
        val oldLayoutParams = oldContent.layoutParams
        val loadLayout = LoadLayout(activity, onReloadListener)
        loadLayout.background = background
        loadLayout.setupSuccessLayout(SuccessCallback(oldContent, activity, onReloadListener))
        contentParent.addView(loadLayout, childIndex, oldLayoutParams)
        return loadLayout
    }
}