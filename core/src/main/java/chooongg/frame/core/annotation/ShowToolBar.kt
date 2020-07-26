package chooongg.frame.core.annotation

import android.view.View
import chooongg.frame.core.widget.titleBar.CenterMaterialToolBar
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
annotation class ShowToolBar(
    val isShow: Boolean = true,
    val toolBarStyle: KClass<out View> = CenterMaterialToolBar::class
)