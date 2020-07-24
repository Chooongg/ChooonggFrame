package chooongg.frame.simple

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import chooongg.frame.throwable.ChooonggFrameException

class TestView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatTextView(context, attrs, defStyle) {

    init {
        throw ChooonggFrameException("测试异常")
    }
}