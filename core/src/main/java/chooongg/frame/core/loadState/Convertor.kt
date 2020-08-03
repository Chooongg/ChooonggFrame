package chooongg.base.loadState

import chooongg.frame.core.loadState.callback.Callback
import kotlin.reflect.KClass

interface Convertor<T> {
    fun map(t: T): KClass<out Callback>
}