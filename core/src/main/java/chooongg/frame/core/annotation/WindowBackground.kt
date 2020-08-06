package chooongg.frame.core.annotation

import androidx.annotation.DrawableRes

@Target(AnnotationTarget.CLASS)
annotation class WindowBackground(@DrawableRes val value: Int)