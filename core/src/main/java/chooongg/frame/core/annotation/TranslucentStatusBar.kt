package chooongg.frame.core.annotation

@Target(AnnotationTarget.CLASS)
annotation class TranslucentStatusBar(val isEnable: Boolean = true, val isHideMask: Boolean = true)