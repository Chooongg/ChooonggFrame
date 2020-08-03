package chooongg.frame.core.loadState.target

import chooongg.frame.core.loadState.LoadLayout
import chooongg.frame.core.loadState.callback.Callback

interface ITarget {

    fun withEquals(target: Any?): Boolean

    fun replaceView(target: Any, onReloadListener: ((Class<out Callback>) -> Unit)?): LoadLayout

}