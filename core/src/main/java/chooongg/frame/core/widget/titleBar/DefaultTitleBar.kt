package chooongg.frame.core.widget.titleBar

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.appbar.MaterialToolbar

class DefaultTitleBar @JvmOverloads constructor(
    context: Context, private val attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialToolbar(context, attrs, defStyleAttr) {

    init {
        setContentInsetsRelative(0, 0)
    }
}