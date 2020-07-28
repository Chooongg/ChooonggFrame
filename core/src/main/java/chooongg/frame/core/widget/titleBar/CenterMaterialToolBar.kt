package chooongg.frame.core.widget.titleBar

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.View
import chooongg.frame.core.R
import chooongg.frame.utils.resString
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.android.synthetic.main.view_title_center.view.*

class CenterMaterialToolBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialToolbar(context, attrs, defStyleAttr) {

    private var titleText: CharSequence? = null
    private var titleTextColor: ColorStateList? = null
    private var subtitleText: CharSequence? = null
    private var subtitleTextColor: ColorStateList? = null

    init {
        setContentInsetsRelative(0, 0)
        View.inflate(context, R.layout.view_title_center, this)
        if (titleTextColor != null) title_tv_title.setTextColor(titleTextColor)
        if (titleText != null) title_tv_title.text = titleText
        if (subtitleTextColor != null) title_tv_subtitle.setTextColor(subtitleTextColor)
        if (subtitleText != null) title_tv_subtitle.text = subtitleText
    }

    override fun setTitleTextColor(color: ColorStateList) {
        super.setTitleTextColor(color)
        titleTextColor = color
    }

    override fun setSubtitleTextColor(color: ColorStateList) {
        super.setSubtitleTextColor(color)
        subtitleTextColor = color
    }

    override fun setTitle(resId: Int) {
        titleText = resString(resId)
    }

    override fun setTitle(title: CharSequence?) {
        titleText = title
    }

    override fun setSubtitle(resId: Int) {
        subtitleText = resString(resId)
    }

    override fun setSubtitle(subtitle: CharSequence?) {
        subtitleText = subtitle
    }
}