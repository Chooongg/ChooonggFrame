package chooongg.frame.core.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import kotlin.math.abs

class SquareFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // if height < width : call super method with heightSpec for all sides
        if (abs(heightMeasureSpec) < abs(widthMeasureSpec)) {
            super.onMeasure(heightMeasureSpec, heightMeasureSpec)
        } else {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec)
        }
    }
}