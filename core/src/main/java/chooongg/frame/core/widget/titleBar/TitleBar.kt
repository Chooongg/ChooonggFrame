package chooongg.frame.core.widget.titleBar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import chooongg.frame.core.R
import chooongg.frame.utils.*
import kotlinx.android.synthetic.main.title_bar.view.*

class TitleBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var lineDividerView: View? = null

    init {
        orientation = VERTICAL
        fitsSystemWindows = true
        View.inflate(context, R.layout.title_bar, this)
    }

    fun navigation(@DrawableRes id: Int, listener: (View) -> Unit) = apply {
        title_navigation.visible()
        title_navigation.setIconResource(id)
        title_navigation.text = null
        title_navigation.doOnClick(listener)
    }

    fun navigation(text: CharSequence, listener: (View) -> Unit) = apply {
        title_navigation.visible()
        title_navigation.icon = null
        title_navigation.text = text
        title_navigation.doOnClick(listener)
    }

    fun navigation(@DrawableRes id: Int, text: CharSequence, listener: (View) -> Unit) = apply {
        title_navigation.visible()
        title_navigation.setIconResource(id)
        title_navigation.text = text
        title_navigation.doOnClick(listener)
    }

    fun navigationHide() = apply {
        title_navigation.gone()
        title_navigation.setOnClickListener(null)
    }

    private fun addLineDivider() {
        if (lineDividerView != null) return
        lineDividerView = View(context)
        lineDividerView!!.setBackgroundResource(resColor(R.color.colorDivider))
        addView(
            lineDividerView, childCount - 1,
            LayoutParams(LayoutParams.MATCH_PARENT, resDimenOffset(R.dimen.dividerSmall))
        )
    }

    private fun removeLineDivider() {
        if (lineDividerView == null) return
        removeView(lineDividerView)
        lineDividerView = null
    }
}