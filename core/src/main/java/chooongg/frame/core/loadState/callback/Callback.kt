package chooongg.frame.core.loadState.callback

import android.content.Context
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import chooongg.frame.utils.doOnClick
import chooongg.frame.utils.resInt
import java.io.*

abstract class Callback : Serializable {

    private var rootView: View? = null
    protected lateinit var context: Context
    private var onReloadListener: ((Class<out Callback>) -> Unit)? = null
    var successViewVisible = false

    @LayoutRes
    protected abstract fun getViewLayout(): Int

    private fun onBuildView(context: Context): View? = null

    protected abstract fun onViewCreated(context: Context, view: View)

    abstract fun onAttach(context: Context, view: View)

    open fun onReloadEvent(): View? = rootView

    open fun onTextChange(): TextView? = null

    open fun onImageChange(): AppCompatImageView? = null

    abstract fun onDetach(context: Context, view: View)

    constructor()

    internal constructor(
        view: View,
        context: Context,
        onReloadListener: ((Class<out Callback>) -> Unit)?
    ) {
        this.rootView = view
        this.context = context
        this.onReloadListener = onReloadListener
    }

    fun setCallback(context: Context, onReloadListener: ((Class<out Callback>) -> Unit)?) = apply {
        this.context = context
        this.onReloadListener = onReloadListener
    }

    fun getRootView(): View {
        val resId: Int = getViewLayout()
        if (resId == 0 && rootView != null) {
            return rootView!!
        }

        if (onBuildView(context) != null) {
            rootView = onBuildView(context)
        }

        if (rootView == null) {
            rootView = View.inflate(context, getViewLayout(), null)
        }
        onViewCreated(context, rootView!!)
        onReloadEvent()?.doOnClick {
            onReloadListener?.invoke(javaClass)
        }
        return rootView!!
    }

    open fun isEnableAnimation() = true

    open fun getAnimation(): Animation {
        return AnimationSet(true).apply {
            addAnimation(AlphaAnimation(0f, 1f))
            addAnimation(
                ScaleAnimation(
                    0.8f, 1f, 0.8f, 1f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
                )
            )
            duration = context.resInt(android.R.integer.config_mediumAnimTime).toLong()
            interpolator = FastOutSlowInInterpolator()
        }
    }

    fun obtainRootView(): View {
        if (rootView == null) {
            rootView = View.inflate(context, getViewLayout(), null)
        }
        return rootView!!
    }

    fun copy(): Callback {
        val bao = ByteArrayOutputStream()
        var obj: Any? = null
        try {
            val oos = ObjectOutputStream(bao)
            oos.writeObject(this)
            oos.close()
            val bis = ByteArrayInputStream(bao.toByteArray())
            val ois = ObjectInputStream(bis)
            obj = ois.readObject()
            ois.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return obj as Callback
    }
}