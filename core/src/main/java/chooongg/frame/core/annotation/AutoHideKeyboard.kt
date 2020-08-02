package chooongg.frame.core.annotation

import java.lang.annotation.Inherited

@Inherited
@Target(AnnotationTarget.CLASS)
annotation class AutoHideKeyboard(val value: Boolean = true)