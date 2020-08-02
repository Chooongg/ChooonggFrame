package chooongg.frame.core.widget.roundedImageView

import androidx.annotation.IntDef
import chooongg.frame.core.widget.roundedImageView.Corner.Companion.BOTTOM_LEFT
import chooongg.frame.core.widget.roundedImageView.Corner.Companion.BOTTOM_RIGHT
import chooongg.frame.core.widget.roundedImageView.Corner.Companion.TOP_LEFT
import chooongg.frame.core.widget.roundedImageView.Corner.Companion.TOP_RIGHT

@Retention(AnnotationRetention.SOURCE)
@IntDef(
    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT
)
annotation class Corner {
    companion object {
        const val TOP_LEFT = 0
        const val TOP_RIGHT = 1
        const val BOTTOM_RIGHT = 2
        const val BOTTOM_LEFT = 3
    }
}