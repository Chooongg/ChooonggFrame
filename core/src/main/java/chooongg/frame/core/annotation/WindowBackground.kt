package chooongg.frame.core.annotation

import androidx.annotation.LayoutRes

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class WindowBackground(@LayoutRes val value: Int)