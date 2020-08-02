package chooongg.frame.core.widget.maskEditText

import android.content.Context
import android.util.AttributeSet
import chooongg.frame.core.R
import com.google.android.material.textfield.TextInputEditText

/**
 * app:met_mask="+86(###) ### ## ##"
 */
class MaskEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextInputEditText(context, attrs, defStyleAttr) {

    var maskTextWatcher: MaskTextWatcher? = null

    var mask: String? = null
        set(value) {
            field = value
            if (value.isNullOrEmpty()) {
                removeTextChangedListener(maskTextWatcher)
            } else {
                maskTextWatcher =
                    MaskTextWatcher(
                        this,
                        mask!!
                    )
                addTextChangedListener(maskTextWatcher)
            }
        }

    val rawText: String?
        get() {
            val formatted = text
            return maskTextWatcher?.unformat(formatted) ?: formatted.toString()
        }

    init {
        attrs?.apply {
            val a = context.obtainStyledAttributes(this, R.styleable.MaskEditText)
            mask = a.getString(R.styleable.MaskEditText_met_mask)
            a.recycle()
        }
    }
}