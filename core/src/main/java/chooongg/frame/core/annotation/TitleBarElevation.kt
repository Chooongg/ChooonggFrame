package chooongg.frame.core.annotation

import androidx.annotation.IntDef
import java.lang.annotation.Inherited

@Inherited
@Target(AnnotationTarget.CLASS)
annotation class TitleBarElevation(@DividerStyle val value: Int = ELEVATION) {

    companion object {
        const val NONE = 0
        const val ELEVATION = 1
    }

    @IntDef(NONE, ELEVATION)
    annotation class DividerStyle
}