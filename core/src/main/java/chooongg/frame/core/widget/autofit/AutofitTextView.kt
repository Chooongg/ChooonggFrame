package chooongg.frame.core.widget.autofit

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView

/**
 * A [AppCompatTextView] that re-sizes its text to be no larger than the mWidth of the view.
 *
 * @attr ref R.styleable.AutofitTextView_sizeToFit
 * @attr ref R.styleable.AutofitTextView_minTextSize
 * @attr ref R.styleable.AutofitTextView_precision
 */
class AutofitTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatTextView(context, attrs, defStyle),
    AutofitHelper.OnTextSizeChangeListener {

    var autofitHelper: AutofitHelper? = null
        private set

    /**
     * Returns whether or not the text will be automatically re-sized to fit its constraints.
     */
    /**
     * If true, the text will automatically be re-sized to fit its constraints; if false, it will
     * act like a normal TextView.
     */
    var isSizeToFit
        get() = autofitHelper!!.isEnabled()
        set(sizeToFit) {
            autofitHelper!!.setEnabled(sizeToFit)
        }

    /**
     * Returns the maximum size (in pixels) of the text in this View.
     */
    /**
     * Set the maximum text size to the given value, interpreted as "scaled pixel" units. This size
     * is adjusted based on the current density and user font size preference.
     *
     * @param size The scaled pixel size.
     * @attr ref android.R.styleable#TextView_textSize
     */
    var maxTextSize: Float
        get() = autofitHelper!!.getMaxTextSize()
        set(size) {
            autofitHelper!!.setMaxTextSize(size)
        }

    /**
     * Returns the minimum size (in pixels) of the text in this View.
     */
    val minTextSize: Float
        get() = autofitHelper!!.getMinTextSize()

    /**
     * Returns the amount of precision used to calculate the correct text size to fit within its
     * bounds.
     */
    /**
     * Set the amount of precision used to calculate the correct text size to fit within its
     * bounds. Lower precision is more precise and takes more time.
     */
    var precision: Float
        get() = autofitHelper!!.getPrecision()
        set(precision) {
            autofitHelper!!.setPrecision(precision)
        }

    init {
        autofitHelper = AutofitHelper.create(
            this,
            attrs,
            defStyle
        )
            .addOnTextSizeChangeListener(this)
    }

    /**
     * {@inheritDoc}
     */
    override fun setTextSize(unit: Int, size: Float) {
        super.setTextSize(unit, size)
        if (autofitHelper != null) {
            autofitHelper!!.setTextSize(unit, size)
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun setLines(lines: Int) {
        super.setLines(lines)
        if (autofitHelper != null) {
            autofitHelper!!.setMaxLines(lines)
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun setMaxLines(maxLines: Int) {
        super.setMaxLines(maxLines)
        if (autofitHelper != null) {
            autofitHelper!!.setMaxLines(maxLines)
        }
    }

    /**
     * Sets the property of this field (sizeToFit), to automatically resize the text to fit its
     * constraints.
     */
    fun setSizeToFit() {
        isSizeToFit = true
    }

    /**
     * Set the maximum text size to a given unit and value. See TypedValue for the possible
     * dimension units.
     *
     * @param unit The desired dimension unit.
     * @param size The desired size in the given units.
     * @attr ref android.R.styleable#TextView_textSize
     */
    fun setMaxTextSize(unit: Int, size: Float) = autofitHelper!!.setMaxTextSize(unit, size)

    /**
     * Set the minimum text size to the given value, interpreted as "scaled pixel" units. This size
     * is adjusted based on the current density and user font size preference.
     *
     * @param minSize The scaled pixel size.
     * @attr ref me.grantland.R.styleable#AutofitTextView_minTextSize
     */
    fun setMinTextSize(minSize: Int): AutofitHelper {
        return autofitHelper!!.setMinTextSize(TypedValue.COMPLEX_UNIT_SP, minSize.toFloat())
    }

    /**
     * Set the minimum text size to a given unit and value. See TypedValue for the possible
     * dimension units.
     *
     * @param unit    The desired dimension unit.
     * @param minSize The desired size in the given units.
     * @attr ref me.grantland.R.styleable#AutofitTextView_minTextSize
     */
    fun setMinTextSize(unit: Int, minSize: Float) = autofitHelper!!.setMinTextSize(unit, minSize)

    override fun onTextSizeChange(textSize: Float, oldTextSize: Float) {
    }
}
