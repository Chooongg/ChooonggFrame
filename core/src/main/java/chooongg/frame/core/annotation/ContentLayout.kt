package chooongg.frame.core.annotation

import androidx.annotation.LayoutRes

@Target(AnnotationTarget.CLASS)
annotation class ContentLayout(@LayoutRes val value: Int)