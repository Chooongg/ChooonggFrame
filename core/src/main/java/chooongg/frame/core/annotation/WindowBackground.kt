package chooongg.frame.core.annotation

import androidx.annotation.LayoutRes

@Target(AnnotationTarget.CLASS)
annotation class WindowBackground(@LayoutRes val value: Int)