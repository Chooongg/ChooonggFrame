package chooongg.frame.core.loadState.target

import android.app.Activity
import chooongg.frame.core.R
import chooongg.frame.core.loadState.LoadLayout
import chooongg.frame.core.loadState.callback.Callback
import chooongg.frame.core.loadState.callback.SuccessCallback
import chooongg.frame.core.widget.ChooonggToolBar
import chooongg.frame.utils.contentView

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
            if (contentParent.getChildAt(i) !is ChooonggToolBar) {
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
        loadLayout.setupSuccessLayout(SuccessCallback(oldContent, activity, null))
        loadLayout.id = R.id.load_layout
        contentParent.addView(loadLayout, childIndex, oldLayoutParams)
        return loadLayout
    }
}