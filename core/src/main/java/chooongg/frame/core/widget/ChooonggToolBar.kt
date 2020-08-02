package chooongg.frame.core.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import chooongg.frame.core.R
import chooongg.frame.utils.getActivity
import chooongg.frame.utils.gone
import chooongg.frame.utils.setStatusBarLightMode
import chooongg.frame.utils.visible
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.android.synthetic.main.view_title_center.view.*

class ChooonggToolBar @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialToolbar(context, attrs, defStyleAttr) {

    private var customCenterView: View? = null
    private var mTitle: CharSequence? = null
    private var mSubtitle: CharSequence? = null
    private var titleColor: ColorStateList? = null
    private var subTitleColor: ColorStateList? = null

    private var isCenter = false

    init {
        setContentInsetsRelative(0, 0)
        contentInsetStartWithNavigation = 0
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.ChooonggToolBar, 0, 0)
            setTitleStyle(a.getBoolean(a.getIndex(R.styleable.ChooonggToolBar_centerTitle), true))
            a.recycle()
        }
    }

    fun setTitleStyle(isCenter: Boolean) {
        this.isCenter = isCenter
        if (isCenter) {
            customCenterView = View.inflate(context, R.layout.view_title_center, this)
            customCenterView?.title_tv_title?.setTextColor(titleColor)
            customCenterView?.title_tv_subtitle?.setTextColor(subTitleColor)
            title = mTitle
            subtitle = mSubtitle
        } else {
            removeView(customCenterView)
        }
    }

    override fun setTitle(title: CharSequence?) {
        mTitle = title
        if (isCenter) {
            super.setTitle(null)
            if (mTitle.isNullOrEmpty()) {
                customCenterView?.title_tv_title?.gone()
            } else {
                customCenterView?.title_tv_title?.text = mTitle
                customCenterView?.title_tv_title?.visible()
            }
        } else super.setTitle(title)
    }

    override fun setSubtitle(subtitle: CharSequence?) {
        mSubtitle = subtitle
        if (isCenter) {
            super.setSubtitle(null)
            if (mSubtitle.isNullOrEmpty()) {
                customCenterView?.title_tv_subtitle?.gone()
            } else {
                customCenterView?.title_tv_subtitle?.text = mSubtitle
                customCenterView?.title_tv_subtitle?.visible()
            }
        } else super.setSubtitle(subtitle)
    }

    override fun setTitleTextColor(color: ColorStateList) {
        titleColor = color
        super.setTitleTextColor(color)
    }

    override fun setSubtitleTextColor(color: ColorStateList) {
        subTitleColor = color
        super.setSubtitleTextColor(color)
    }

    override fun setNavigationIcon(icon: Drawable?) {
        if (titleColor != null) {
            icon?.setTintList(titleColor)
        }
        super.setNavigationIcon(icon)
    }

    override fun setBackground(background: Drawable?) {
        super.setBackground(background)
        if (background is ColorDrawable) {
            val activity = context.getActivity()
            if (background.color == Color.WHITE) {
                activity?.setStatusBarLightMode(true)
                activity?.window?.statusBarColor = Color.WHITE
            } else {
                activity?.setStatusBarLightMode(false)
            }
        }
    }
}