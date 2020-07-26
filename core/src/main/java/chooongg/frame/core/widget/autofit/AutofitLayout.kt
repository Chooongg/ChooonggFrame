package chooongg.frame.core.widget.autofit

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import chooongg.frame.core.R
import java.util.*

/**
 * A [ViewGroup] that re-sizes the text of it's children to be no larger than the mWidth of the
 * view.
 *
 * @attr ref R.styleable.AutofitTextView_sizeToFit
 * @attr ref R.styleable.AutofitTextView_minTextSize
 * @attr ref R.styleable.AutofitTextView_precision
 */
class AutofitLayout : FrameLayout {

    private var mEnabled: Boolean = false
    private var mMinTextSize: Float = 0f
    private var mPrecision: Float = 0f
    private val mHelpers = WeakHashMap<View, AutofitHelper>()

    constructor(context: Context) : super(context) {
        init(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(context, attrs, defStyle)
    }

    @SuppressLint("CustomViewStyleable")
    private fun init(context: Context, attrs: AttributeSet?, defStyle: Int) {
        var sizeToFit = true
        var minTextSize = -1
        var precision = -1f

        if (attrs != null) {
            val ta = context.obtainStyledAttributes(
                attrs,
                R.styleable.AutofitTextView,
                defStyle,
                0
            )
            sizeToFit = ta.getBoolean(R.styleable.AutofitTextView_autofit_sizeToFit, sizeToFit)
            minTextSize = ta.getDimensionPixelSize(
                R.styleable.AutofitTextView_autofit_minTextSize,
                minTextSize
            )
            precision = ta.getFloat(R.styleable.AutofitTextView_autofit_precision, precision)
            ta.recycle()
        }

        mEnabled = sizeToFit
        mMinTextSize = minTextSize.toFloat()
        mPrecision = precision
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        super.addView(child, index, params)
        val textView = child as TextView
        val helper = AutofitHelper.create(
            textView
        )
            .setEnabled(mEnabled)
        if (mPrecision > 0) {
            helper.setPrecision(mPrecision)
        }
        if (mMinTextSize > 0) {
            helper.setMinTextSize(TypedValue.COMPLEX_UNIT_PX, mMinTextSize)
        }
        mHelpers[textView] = helper
    }

    /**
     * Returns the [AutofitHelper] for this child View.
     */
    fun getAutofitHelper(textView: TextView): AutofitHelper? {
        return mHelpers[textView]
    }

    /**
     * Returns the [AutofitHelper] for this child View.
     */
    fun getAutofitHelper(index: Int): AutofitHelper? {
        return mHelpers[getChildAt(index)]
    }
}
