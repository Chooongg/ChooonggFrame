package chooongg.frame.core.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import com.google.android.material.textview.MaterialTextView

class MarqueeTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : MaterialTextView(context, attrs, defStyle) {

    init {
        ellipsize = TextUtils.TruncateAt.MARQUEE
        marqueeRepeatLimit = Integer.MAX_VALUE
    }

    override fun isFocused() = true
    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        if (hasWindowFocus) super.onWindowFocusChanged(hasWindowFocus)
    }
}