package chooongg.frame.utils

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.IntRange

object ColorUtils {

    /**
     * 是否是深色
     */
    fun isColorDark(@ColorInt color: Int): Boolean {
        val darkness =
            1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
        return darkness >= 0.5
    }

    /**
     * 修改颜色透明度
     */
    fun changeAlpha(color: Int, @IntRange(from = 0, to = 255) alpha: Int): Int {
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color))
    }


    /**
     * 计算从startColor过度到endColor过程中百分比为franch时的颜色值
     * @param startColor 起始颜色 int类型
     * @param endColor 结束颜色 int类型
     * @param franch franch 百分比0.5
     * @return 返回int格式的color
     */
    fun caculateColor(startColor: Int, endColor: Int, franch: Float): Int {
        val strStartColor = "#" + Integer.toHexString(startColor)
        val strEndColor = "#" + Integer.toHexString(endColor)
        return Color.parseColor(caculateColor(strStartColor, strEndColor, franch))
    }

    /**
     * 计算从startColor过度到endColor过程中百分比为franch时的颜色值
     * @param startColor 起始颜色 （格式#FFFFFFFF）
     * @param endColor 结束颜色 （格式#FFFFFFFF）
     * @param franch 百分比0.5
     * @return 返回String格式的color（格式#FFFFFFFF）
     */
    fun caculateColor(startColor: String, endColor: String, franch: Float): String? {
        val startAlpha = startColor.substring(1, 3).toInt(16)
        val startRed = startColor.substring(3, 5).toInt(16)
        val startGreen = startColor.substring(5, 7).toInt(16)
        val startBlue = startColor.substring(7).toInt(16)
        val endAlpha = endColor.substring(1, 3).toInt(16)
        val endRed = endColor.substring(3, 5).toInt(16)
        val endGreen = endColor.substring(5, 7).toInt(16)
        val endBlue = endColor.substring(7).toInt(16)
        val currentAlpha = ((endAlpha - startAlpha) * franch + startAlpha).toInt()
        val currentRed = ((endRed - startRed) * franch + startRed).toInt()
        val currentGreen = ((endGreen - startGreen) * franch + startGreen).toInt()
        val currentBlue = ((endBlue - startBlue) * franch + startBlue).toInt()
        return ("#" + getHexString(currentAlpha) + getHexString(currentRed)
                + getHexString(currentGreen) + getHexString(currentBlue))
    }

    /**
     * 将10进制颜色值转换成16进制。
     */
    fun getHexString(value: Int): String? {
        var hexString = Integer.toHexString(value)
        if (hexString.length == 1) {
            hexString = "0$hexString"
        }
        return hexString
    }
}