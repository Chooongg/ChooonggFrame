package chooongg.frame.core.loadState.callback

import android.content.Context
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import chooongg.frame.utils.inVisible
import chooongg.frame.utils.resInt
import chooongg.frame.utils.visible

class SuccessCallback(
    view: View,
    context: Context,
    onReloadListener: ((Class<out Callback>) -> Unit)?
) : Callback(view, context, onReloadListener) {

    override fun getViewLayout() = 0
    override fun onViewCreated(context: Context, view: View) = Unit
    override fun onAttach(context: Context, view: View) = Unit
    override fun onDetach(context: Context, view: View) = Unit

    fun show() = obtainRootView().visible()
    fun hide() = obtainRootView().inVisible()

    fun showWithCallback(successVisible: Boolean) {
        if (successVisible) show() else hide()
    }

    override fun getAnimation(): Animation {
        return AnimationSet(true).apply {
            addAnimation(AlphaAnimation(0f, 1f))
            duration = context.resInt(android.R.integer.config_mediumAnimTime).toLong()
            interpolator = FastOutSlowInInterpolator()
        }
    }
}