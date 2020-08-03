package chooongg.base.loadState.target

import chooongg.base.loadState.LoadLayout
import chooongg.frame.core.loadState.callback.Callback

interface ITarget {

    fun withEquals(target: Any?): Boolean

    fun replaceView(target: Any, onReloadListener: ((Class<out Callback>) -> Unit)?): LoadLayout

}