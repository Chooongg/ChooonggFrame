package chooongg.base.loadState

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import chooongg.frame.core.loadState.callback.Callback
import chooongg.frame.core.loadState.callback.SuccessCallback
import chooongg.base.utils.isMainThread
import kotlin.reflect.KClass

class LoadLayout @JvmOverloads constructor(
    context: Context,
    private val onReloadListener: ((Class<out Callback>) -> Unit)? = null
) : FrameLayout(context) {

    companion object {
        private const val CALLBACK_CUSTOM_INDEX = 1
    }

    val callbacks = HashMap<KClass<out Callback>, Callback>()
    var preCallback: KClass<out Callback>? = null
        private set
    var currentCallback: KClass<out Callback>? = null
        private set

    fun setupSuccessLayout(callback: Callback) {
        addCallback(callback)
        val successView = callback.getRootView()
        addView(
            successView, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        currentCallback = SuccessCallback::class
    }

    fun setupCallback(callback: Callback) {
        callback.setCallback(context, onReloadListener)
        addCallback(callback)
    }

    fun addCallback(callback: Callback) {
        if (!callbacks.containsKey(callback::class)) {
            callbacks[callback::class] = callback
        }
    }

    fun showCallback(callback: KClass<out Callback>) {
        checkCallbackExist(callback)
        if (isMainThread()) showCallbackView(callback) else post { showCallbackView(callback) }
    }

    private fun showCallbackView(status: KClass<out Callback>) {
        if (preCallback != null) {
            if (preCallback == status) return
            callbacks[preCallback!!]!!.onDetach(context, callbacks[preCallback!!]!!.getRootView())
        }
        if (childCount > 1) removeViewAt(CALLBACK_CUSTOM_INDEX)
        callbacks.forEach {
            if (it.key == status) {
                val successCallback = callbacks[SuccessCallback::class] as SuccessCallback
                if (it.key == SuccessCallback::class) {
                    successCallback.show().apply {
                        if (successCallback.isEnableAnimation()) {
                            startAnimation(successCallback.getAnimation())
                        }
                    }
                } else {
                    successCallback.showWithCallback(callbacks[it.key]!!.successViewVisible)
                    val rootView = callbacks[it.key]!!.getRootView()
                    addView(rootView)
                    callbacks[it.key]!!.onAttach(context, rootView)
                    if (callbacks[it.key]!!.isEnableAnimation()) {
                        rootView.startAnimation(callbacks[it.key]!!.getAnimation())
                    }
                }
                preCallback = status
            }
        }
        currentCallback = status
    }

    fun setCallback(callback: KClass<out Callback>, transport: Transport) {
        checkCallbackExist(callback)
        transport.order(context, callbacks[callback]!!.obtainRootView())
    }

    private fun checkCallbackExist(callback: KClass<out Callback>) {
        if (!callbacks.containsKey(callback)) {
            throw IllegalArgumentException(
                String.format("The Callback (%s) is nonexistent.", callback.simpleName)
            )
        }
    }
}