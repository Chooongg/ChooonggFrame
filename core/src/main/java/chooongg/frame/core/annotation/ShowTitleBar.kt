package chooongg.frame.core.annotation

import androidx.annotation.IntDef
import java.lang.annotation.Inherited

@Inherited
@Target(AnnotationTarget.CLASS)
annotation class ShowTitleBar(
    val isShow: Boolean = true,
    val isCenter: Boolean = false,
    @TitleStyle val style: Int = PRIMARY
) {
    companion object {
        const val PRIMARY = 0
        const val PRIMARY_SURFACE = 1
        const val SURFACE = 2
    }

    @IntDef(PRIMARY, PRIMARY_SURFACE, SURFACE)
    annotation class TitleStyle
}