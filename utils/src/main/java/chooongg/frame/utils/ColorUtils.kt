package chooongg.frame.utils

import android.graphics.Color
import androidx.annotation.ColorInt

object ColorUtils {

    /**
     * 是否是深色
     */
    fun isColorDark(@ColorInt color: Int): Boolean {
        val darkness =
            1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
        return darkness >= 0.5
    }
}